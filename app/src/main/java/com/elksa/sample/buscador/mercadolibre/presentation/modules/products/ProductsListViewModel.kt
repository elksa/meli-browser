package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elksa.sample.buscador.mercadolibre.R
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

    private var eventListenerInitialized = false

    private var _productsList = MutableLiveData(listOf<ProductUiModel>())
    val productsList: LiveData<List<ProductUiModel>> get() = _productsList

    private var _hideKeyboardEvent = SingleLiveEvent<Boolean>()
    val hideKeyboardEvent: LiveData<Boolean> get() = _hideKeyboardEvent

    private var _deleteRecentSearchesEvent = SingleLiveEvent<Boolean>()
    val deleteRecentSearchesEvent: LiveData<Boolean> get() = _deleteRecentSearchesEvent

    private var _emptySearchVisibility = MutableLiveData(GONE)
    val emptySearchVisibility: LiveData<Int> get() = _emptySearchVisibility

    private var _isLoaderVisible = MutableLiveData(false)
    val isLoaderVisible: LiveData<Boolean> get() = _isLoaderVisible

    private var query = "Motorola"

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

    fun searchProducts() {
        _isLoaderVisible.value = true
        _hideKeyboardEvent.value = true
        val offset = _productsList.value?.size ?: 0
        compositeDisposable.add(
            searchProductsUseCase.searchProducts(query, offset, PAGE_SIZE)
                .compose(scheduler.applySingleDefaultSchedulers())
                .subscribe(
                    { response ->
                        val currentResults = _productsList.value
                        val newResults = response.results.map {
                            ProductUiModel.mapFromDomain(it, moneyFormatter.format(it.price)).apply {
                                price
                            }
                        }
                        _productsList.value = concatenate(
                            currentResults ?: listOf(),
                            newResults
                        )
                        _isLoaderVisible.value = false
                        _emptySearchVisibility.value =
                            if (_productsList.value.isNullOrEmpty()) VISIBLE else GONE
                    }, {
                        _isLoaderVisible.value = false
                        _errorEvent.value = R.string.error_products_search
                        _emptySearchVisibility.value =
                            if (_productsList.value.isNullOrEmpty()) VISIBLE else GONE
                        logger.log(TAG, it.toString(), it, ERROR)
                    }
                )
        )
    }

    fun onDeleteRecentSearches() {
        _deleteRecentSearchesEvent.value = true
    }

    fun onProductSelected(selectedProduct: ProductUiModel) {
        _navigationEvent.value = NavigationToDirectionEvent(
            actionDestProductsListFragmentToDestProductDetailsFragment(selectedProduct)
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}