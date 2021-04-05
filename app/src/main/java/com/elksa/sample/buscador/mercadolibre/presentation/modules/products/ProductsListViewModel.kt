package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity
import com.elksa.sample.buscador.mercadolibre.domain.ProductsSearchResultEntity
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.domain.utils.IScheduler
import com.elksa.sample.buscador.mercadolibre.interactors.SearchProductsUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductsListFragmentDirections.Companion.actionDestProductsListFragmentToDestProductDetailsFragment
import com.elksa.sample.buscador.mercadolibre.presentation.utils.concatenate
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBus
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.SearchProductEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.formatters.IMoneyFormatter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.SingleLiveEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.common.BaseViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationToDirectionEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val TAG = "ProductsListViewModel"
private const val PAGE_SIZE = 50

class ProductsListViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val scheduler: IScheduler,
    private val logger: ILogger,
    private val eventBus: IEventBus,
    private val moneyFormatter: IMoneyFormatter
) : BaseViewModel() {

    private val compositeDisposable = CompositeDisposable()

    /**
     * Flag to keep track of the bus listener initialization.
     */
    private var eventListenerInitialized = false

    /**
     * List of products, this is the result of the search.
     */
    private var _productsList = MutableLiveData(listOf<ProductUiModel>())
    val productsList: LiveData<List<ProductUiModel>> get() = _productsList

    /**
     * Event to for hiding the keyboard. In some cases when the keyboard is shown and the
     * user performs a voice search, the keyboard may be left opened.
     */
    private var _hideKeyboardEvent = SingleLiveEvent<Boolean>()
    val hideKeyboardEvent: LiveData<Boolean> get() = _hideKeyboardEvent

    /**
     * Event for deleting recent searches.
     */
    private var _deleteRecentSearchesEvent = SingleLiveEvent<Boolean>()
    val deleteRecentSearchesEvent: LiveData<Boolean> get() = _deleteRecentSearchesEvent

    /**
     * Whether empty state should be visible or not.
     */
    private var _emptySearchVisibility = MutableLiveData(GONE)
    val emptySearchVisibility: LiveData<Int> get() = _emptySearchVisibility

    /**
     * Whether the loader should be visible or not
     */
    private var _isLoaderVisible = MutableLiveData(false)
    val isLoaderVisible: LiveData<Boolean> get() = _isLoaderVisible

    private var query = "Motorola"

    /**
     * Initializes the listener for incoming queries and, caches the query and clears the cached
     * results if there comes a new query. Additionally if there are no results then triggers the
     * first search to populate the list.
     */
    fun init() {
        if (eventListenerInitialized.not()) {
            compositeDisposable.add(
                eventBus.listen(SearchProductEvent::class.java).subscribe {
                    query = it.query
                    _productsList.value = listOf()
                    searchProducts()
                }
            )
            eventListenerInitialized = true
        }
        if (productsList.value.isNullOrEmpty()) {
            searchProducts()
        }
    }

    /**
     * Searches the products. For this shows the loader and hides the keyboard. Then handles
     * possible errors or the search success. This method works both for the first search and
     * further results as per the pagination.
     */
    fun searchProducts() {
        _isLoaderVisible.value = true
        _hideKeyboardEvent.value = true
        val offset = _productsList.value?.size ?: 0
        compositeDisposable.add(
            searchProductsUseCase.searchProducts(query, offset, PAGE_SIZE)
                .compose(scheduler.applySingleDefaultSchedulers())
                .subscribe(
                    {
                        handleSearchSuccess(it)
                    }, {
                        handleSearchError(it)
                    }
                )
        )
    }

    /**
     * Handles the failure search scenario, for this it hides the loader, triggers the error event,
     * logs the error and show the empty state if there are no previously loaded products.
     */
    private fun handleSearchError(error: Throwable) {
        _isLoaderVisible.value = false
        _errorEvent.value = R.string.error_products_search
        _emptySearchVisibility.value = if (_productsList.value.isNullOrEmpty()) VISIBLE else GONE
        logger.log(TAG, error.toString(), error, ERROR)
    }

    /**
     * Process the successful search scenario. In order to keep an "infinite" list of results, it
     * concatenates the new results with the previous ones if present, then hides the loader and
     * shows the empty state only if the result of the concatenation is empty.
     */
    private fun handleSearchSuccess(searchResult: ProductsSearchResultEntity) {
        val currentResults = _productsList.value
        val newResults = searchResult.results.map { getProductUiModelFromEntity(it) }
        _productsList.value = concatenate(
            currentResults ?: listOf(),
            newResults
        )
        _isLoaderVisible.value = false
        _emptySearchVisibility.value =
            if (_productsList.value.isNullOrEmpty()) VISIBLE else GONE
    }

    /**
     * Maps the product domain entity to the product UI model.
     */
    private fun getProductUiModelFromEntity(product: ProductEntity) = ProductUiModel(
        product.id,
        product.title,
        moneyFormatter.format(product.price),
        product.idCurrency,
        product.quantity,
        product.soldQuantity,
        product.condition,
        product.link,
        product.thumbnail,
        product.stopTime,
        product.shippingInformation.freeShipping
    )

    /**
     * Triggers the delete recent searches event.
     */
    fun onDeleteRecentSearches() {
        _deleteRecentSearchesEvent.value = true
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