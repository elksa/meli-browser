package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity
import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.domain.utils.IScheduler
import com.elksa.sample.buscador.mercadolibre.interactors.ClearRecentSuggestionsUseCase
import com.elksa.sample.buscador.mercadolibre.interactors.FetchProductRecommendationsUseCase
import com.elksa.sample.buscador.mercadolibre.interactors.SearchProductsUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.modules.common.BaseViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.modules.common.DialogInfoUiModel
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductsListFragmentDirections.Companion.actionDestProductsListFragmentToDestProductDetailsFragment
import com.elksa.sample.buscador.mercadolibre.presentation.utils.concatenate
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBus
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.SearchProductEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.formatters.IMoneyFormatter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.SingleLiveEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationToDirectionEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val TAG = "ProductsListViewModel"
private const val PAGE_SIZE = 50
private const val PAGE_SIZE_RECOMMENDATIONS = 15

class ProductsListViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val fetchProductRecommendationsUseCase: FetchProductRecommendationsUseCase,
    private val clearRecentSuggestionsUseCase: ClearRecentSuggestionsUseCase,
    private val scheduler: IScheduler,
    private val logger: ILogger,
    private val eventBus: IEventBus,
    private val moneyFormatter: IMoneyFormatter
) : BaseViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var isEventListenerInitialized = false

    private var _productsList = MutableLiveData(listOf<ProductUiModel>())
    val productsList: LiveData<List<ProductUiModel>> get() = _productsList

    private var _iseEmptySearchVisible = MutableLiveData(false)
    val iseEmptySearchVisible: LiveData<Boolean> get() = _iseEmptySearchVisible

    private var _isLoaderVisible = MutableLiveData(false)
    val isLoaderVisible: LiveData<Boolean> get() = _isLoaderVisible

    /**
     * Event to for hiding the keyboard. In some cases when the keyboard is shown and the
     * user performs a voice search, the keyboard may be left opened.
     */
    private var _hideKeyboardEvent = SingleLiveEvent<Boolean>()
    val hideKeyboardEvent: LiveData<Boolean> get() = _hideKeyboardEvent

    /**
     * Event for deleting recent searches, notifies about the need for recent searches deletion with
     * the information of the dialog to be presented to the user.
     */
    private var _deleteRecentSearchesEvent = SingleLiveEvent<DialogInfoUiModel>()
    val deleteRecentSearchesEvent: LiveData<DialogInfoUiModel> get() = _deleteRecentSearchesEvent

    private var query: String? = null

    private val deleteRecentSearches = { clearRecentSuggestionsUseCase.clearRecentSuggestions() }

    /**
     * Initializes the listener for incoming queries and, caches the query and clears the cached
     * results if there comes a new query. Additionally if there are no results then triggers the
     * first search to populate the list.
     */
    fun init() {
        if (isEventListenerInitialized.not()) {
            compositeDisposable.add(
                eventBus.listen(SearchProductEvent::class.java).subscribe {
                    query = it.query
                    _productsList.value = listOf()
                    searchProducts()
                }
            )
            isEventListenerInitialized = true
        }
        if (query == null) {
            fetchProductRecommendations()
        } else if (productsList.value.isNullOrEmpty()) {
            searchProducts()
        }
    }

    fun doSearch() {
        if (query == null) fetchProductRecommendations() else searchProducts()
    }

    private fun getOffset() = _productsList.value?.size ?: 0

    /**
     * Searches the products. For this shows the loader and hides the keyboard. Then handles
     * possible errors or the search success. This method works both for the first search and
     * further results as per the pagination.
     */
    private fun searchProducts() {
        _isLoaderVisible.value = true
        _hideKeyboardEvent.value = true
        compositeDisposable.add(
            searchProductsUseCase.searchProducts(
                query ?: EMPTY_STRING,
                getOffset(),
                PAGE_SIZE
            ).compose(scheduler.applySingleDefaultSchedulers()).subscribe(
                { handleSearchSuccess(it) },
                { handleSearchError(it) }
            )
        )
    }

    private fun fetchProductRecommendations() {
        _isLoaderVisible.value = true
        compositeDisposable.add(
            fetchProductRecommendationsUseCase.fetchProductRecommendations(
                getOffset(), PAGE_SIZE_RECOMMENDATIONS
            ).compose(scheduler.applySingleDefaultSchedulers()).subscribe(
                { handleSearchSuccess(it) },
                { handleSearchError(it) }
            )
        )
    }

    /**
     * Handles the failure search scenario, for this it hides the loader, triggers the error event,
     * logs the error and show the empty state if there are no previously loaded products.
     * @param error The search error.
     */
    private fun handleSearchError(error: Throwable) {
        _isLoaderVisible.value = false
        _errorEvent.value = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_products_search
        )
        _iseEmptySearchVisible.value = _productsList.value.isNullOrEmpty()
        logger.log(TAG, error.toString(), error, ERROR)
    }

    /**
     * Process the successful search scenario. In order to keep an "infinite" list of results, it
     * concatenates the new results with the previous ones if present, then hides the loader and
     * shows the empty state only if the result of the concatenation is empty.
     * @param results The results of the search.
     */
    private fun handleSearchSuccess(results: List<ProductEntity>) {
        val currentResults = _productsList.value
        val newResults = results.map { getProductUiModelFromEntity(it) }
        _productsList.value = concatenate(
            currentResults ?: listOf(),
            newResults
        )
        _isLoaderVisible.value = false
        _iseEmptySearchVisible.value = _productsList.value.isNullOrEmpty()
    }

    private fun getProductUiModelFromEntity(product: ProductEntity) = ProductUiModel(
        product.id,
        product.title,
        moneyFormatter.format(product.price),
        product.quantity,
        product.soldQuantity,
        product.condition,
        product.thumbnail,
        product.shippingInformation.freeShipping
    )

    fun onDeleteRecentSearches() {
        _deleteRecentSearchesEvent.value = DialogInfoUiModel(
            R.drawable.ic_help,
            R.string.title_dialog_delete_recent_searches,
            R.string.message_dialog_delete_recent_searches,
            android.R.string.ok,
            deleteRecentSearches,
            android.R.string.cancel
        )
    }

    /**
     * Triggers navigation to the details screen to show the selected product's information.
     */
    fun onProductSelected(selectedProduct: ProductUiModel) {
        _navigationEvent.value = NavigationToDirectionEvent(
            actionDestProductsListFragmentToDestProductDetailsFragment(selectedProduct)
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}