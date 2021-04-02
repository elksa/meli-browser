package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.domain.utils.IScheduler
import com.elksa.sample.buscador.mercadolibre.interactors.SearchProductsUseCase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val TAG = "ProductsListViewModel"

class ProductsListViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val scheduler: IScheduler,
    private val logger: ILogger
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _productsList = MutableLiveData(listOf<ProductUiModel>())
    val productsList: LiveData<List<ProductUiModel>> get() = _productsList

    private var _productDetailsNavigationEvent: MutableLiveData<ProductUiModel> = MutableLiveData()
    val productDetailsNavigationEvent: LiveData<ProductUiModel> get() = _productDetailsNavigationEvent
    fun productDetailsNavigationEventConsumed() {
        _productDetailsNavigationEvent.value = null
    }

    private var _loaderVisibility = MutableLiveData(GONE)
    val loaderVisibility: LiveData<Int> get() = _loaderVisibility

    fun searchProducts(query: String) {
        _loaderVisibility.value = VISIBLE
        compositeDisposable.add(
            searchProductsUseCase.searchProducts(query)
                .compose(scheduler.applySingleDefaultSchedulers())
                .subscribe(
                    { response ->
                        _productsList.value = response.results.map {
                            ProductUiModel.mapFromDomain(it)
                        }
                        _loaderVisibility.value = GONE
                    }, {
                        _loaderVisibility.value = GONE
                        logger.log(TAG, it.toString(), it, ERROR)
                    }
                )
        )
    }

    fun onProductSelected(selectedProduct: ProductUiModel) {
        _productDetailsNavigationEvent.value = selectedProduct
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}