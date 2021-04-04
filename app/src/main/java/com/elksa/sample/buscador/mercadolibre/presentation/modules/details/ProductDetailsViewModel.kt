package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.domain.utils.IScheduler
import com.elksa.sample.buscador.mercadolibre.interactors.FetchProductDetailsUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductUiModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.common.BaseViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.OnBackPressedEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val TAG = "ProductDetailsViewModel"

class ProductDetailsViewModel @Inject constructor(
    private val fetchProductDetailsUseCase: FetchProductDetailsUseCase,
    private val scheduler: IScheduler,
    private val logger: ILogger
) : BaseViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private var _product = MutableLiveData<ProductUiModel>()
    val product: LiveData<ProductUiModel> get() = _product

    private var _productDetails = MutableLiveData<ProductDetailsUiModel>()
    val productDetails: LiveData<ProductDetailsUiModel> get() = _productDetails

    private var _isThumbnailVisible = MutableLiveData(false)
    val isThumbnailVisible: LiveData<Boolean> get() = _isThumbnailVisible

    private var _currentPicturePosition = MutableLiveData<Int>()
    val currentPicturePosition: LiveData<Int> get() = _currentPicturePosition

    fun init(product: ProductUiModel) {
        if (_product.value == null) {
            _product.value = product
        }
        if (_productDetails.value == null) {
            loadProductDetails()
        }
    }

    private fun loadProductDetails() {
        _loaderVisibility.value = VISIBLE
        _product.value?.let {
            compositeDisposable.add(
                fetchProductDetailsUseCase.fetchProductDetails(it.id)
                    .compose(scheduler.applySingleDefaultSchedulers())
                    .subscribe(
                        { details ->
                            _productDetails.value = ProductDetailsUiModel.mapFromDomain(details)
                            _isThumbnailVisible.value = details.pictures.isEmpty()
                            _loaderVisibility.value = GONE
                        },{ error ->
                            _errorEvent.value = R.string.error_products_details
                            _isThumbnailVisible.value = true
                            logger.log(TAG, error.toString(), error, ERROR)
                            _loaderVisibility.value = GONE
                        }
                    )
            )
        }
    }

    fun onBackPressed() {
        _navigationEvent.value = OnBackPressedEvent
    }

    fun updatePageIndicator(position: Int) {
        productDetails.value?.let {
            if (position >= 0) {
                _currentPicturePosition.value = position % it.pictures.size
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}