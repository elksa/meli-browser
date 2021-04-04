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
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBus
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.SearchProductEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.formatters.IMoneyFormatter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.SingleLiveEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.common.BaseViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationToDirectionEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val TAG = "ProductsListViewModel"

class ProductsListViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val scheduler: IScheduler,
    private val logger: ILogger,
    private val eventBus: IEventBus,
    private val moneyFormatter: IMoneyFormatter
) : BaseViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _productsList = MutableLiveData(listOf<ProductUiModel>())
    val productsList: LiveData<List<ProductUiModel>> get() = _productsList

    private var _hideKeyboardEvent = SingleLiveEvent<Boolean>()
    val hideKeyboardEvent: LiveData<Boolean> get() = _hideKeyboardEvent

    private var _emptySearchVisibility = MutableLiveData(GONE)
    val emptySearchVisibility: LiveData<Int> get() = _emptySearchVisibility

    fun init() {
        compositeDisposable.add(
            eventBus.listen(SearchProductEvent::class.java).subscribe { searchProducts(it.query) }
        )
        searchProducts("Motorola")
    }

    fun searchProducts(query: String) {
        _loaderVisibility.value = VISIBLE
        _hideKeyboardEvent.value = true
        compositeDisposable.add(
            searchProductsUseCase.searchProducts(query)
                .compose(scheduler.applySingleDefaultSchedulers())
                .subscribe(
                    { response ->
                        _emptySearchVisibility.value = if (response.results.isEmpty()) VISIBLE else GONE
                        _productsList.value = response.results.map {
                            ProductUiModel.mapFromDomain(it, moneyFormatter.format(it.price)).apply {
                                price
                            }
                        }
                        _loaderVisibility.value = GONE
                    }, {
                        _loaderVisibility.value = GONE
                        _errorEvent.value = R.string.error_products_search
                        _emptySearchVisibility.value = VISIBLE
                        logger.log(TAG, it.toString(), it, ERROR)
                    }
                )
        )
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