package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductDetailsEntity
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.framework.networking.utils.IScheduler
import com.elksa.sample.buscador.mercadolibre.domain.interactors.FetchProductDetailsUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductUiModel
import com.elksa.sample.buscador.mercadolibre.presentation.modules.common.BaseViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.OnBackPressedEvent
import com.elksa.sample.buscador.mercadolibre.ui.dialogFragment.DialogInfoUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val TAG = "ProductDetailsViewModel"

@HiltViewModel
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

    private var _loaderVisibility = MutableLiveData(GONE)
    val loaderVisibility: LiveData<Int> get() = _loaderVisibility

    /**
     * Represents the visible picture inside the pager, if the user swipes in order to see another,
     * this value is updated with the new one.
     */
    private var _currentPicturePosition = MutableLiveData<Int>()
    val currentPicturePosition: LiveData<Int> get() = _currentPicturePosition

    /**
     * Sets the cached product info with the one coming from the list screen, allows faster data loading
     * since it is not necessary to wait for the details to be loaded in order to show most of the
     * product's information. It only triggers details loading if there are is nothing already loaded
     * to avoid overheading.
     */
    fun init(product: ProductUiModel) {
        if (_product.value == null) {
            _product.value = product
        }
        if (_productDetails.value == null) {
            loadProductDetails()
        }
    }

    /**
     * Fetches product details. Shows the loader while the background processing is taking place,
     * if there is an error the proper live data is set in order to show it and log it, if there is
     * no error then the details are updated with the new information and in both cases the loader
     * is hidden.
     */
    private fun loadProductDetails() {
        _loaderVisibility.value = VISIBLE
        _product.value?.let { product ->
            compositeDisposable.add(
                fetchProductDetailsUseCase.fetchProductDetails(product.id)
                    .compose(scheduler.applySingleDefaultSchedulers())
                    .subscribe(
                        {
                            handleDetailsSuccess(it)
                        },{
                            handleDetailsError(it)
                        }
                    )
            )
        }
    }

    /**
     * Handles the failure scenario. If there is an error the proper live data is set in order to
     * show it and log it, the loader is hidden.
     * @param error The error to be handled.
     */
    private fun handleDetailsError(error: Throwable) {
        _errorEvent.value = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_products_details
        )
        _isThumbnailVisible.value = true
        logger.log(TAG, error.toString(), error, ERROR)
        _loaderVisibility.value = GONE
    }

    /**
     * Handles the successful scenario. If there is no error then the details are updated with the
     * new information, the thumbnail is hidden, so is the loader.
     * @param details The successful response containing the full product details.
     */
    private fun handleDetailsSuccess(details: ProductDetailsEntity) {
        _productDetails.value = ProductDetailsUiModel.mapFromDomain(details)
        _isThumbnailVisible.value = details.pictures.isEmpty()
        _loaderVisibility.value = GONE
    }

    fun onBackPressed() {
        _navigationEvent.value = OnBackPressedEvent
    }

    /**
     * Updates the selected picture in the pager, only if there are details already loaded and it is
     * a valid position. The position is valid if it is at least 0 and less than the picture count.
     */
    fun updatePageIndicator(position: Int) {
        productDetails.value?.let {
            if (position >= 0 && position < it.pictures.size) {
                _currentPicturePosition.value = position % it.pictures.size
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}