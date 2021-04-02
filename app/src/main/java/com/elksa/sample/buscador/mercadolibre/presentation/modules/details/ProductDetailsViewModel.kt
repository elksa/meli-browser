package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.domain.utils.IScheduler
import com.elksa.sample.buscador.mercadolibre.interactors.FetchProductDetailsUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductUiModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val TAG = "ProductDetailsViewModel"

class ProductDetailsViewModel @Inject constructor(
    private val fetchProductDetailsUseCase: FetchProductDetailsUseCase,
    private val scheduler: IScheduler,
    private val logger: ILogger
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _product = MutableLiveData<ProductUiModel>()
    val product: LiveData<ProductUiModel> get() = _product

    private var _productDetails = MutableLiveData<ProductDetailsUiModel>()
    val productDetails: LiveData<ProductDetailsUiModel> get() = _productDetails

    fun init(product: ProductUiModel) {
        _product.value = product
        loadProductDetails()
    }

    private fun loadProductDetails() {
        _product.value?.let {
            compositeDisposable.add(
                fetchProductDetailsUseCase.loadProductDetails(it.id)
                    .compose(scheduler.applySingleDefaultSchedulers())
                    .subscribe(
                        { details ->
                            _productDetails.value = ProductDetailsUiModel.mapFromDomain(details)
                        },{ error ->
                            logger.log(TAG, error.toString(), error, ERROR)
                        }
                    )
            )
        }
    }
}