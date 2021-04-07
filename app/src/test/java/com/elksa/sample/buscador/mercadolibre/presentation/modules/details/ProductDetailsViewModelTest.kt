package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import android.view.View.GONE
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.domain.PictureEntity
import com.elksa.sample.buscador.mercadolibre.domain.ProductDetailsEntity
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity.ItemCondition.USED
import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.domain.utils.ItemDescriptionEntity
import com.elksa.sample.buscador.mercadolibre.interactors.FetchProductDetailsUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.modules.common.DialogInfoUiModel
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductUiModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.OnBackPressedEvent
import com.elksa.sample.buscador.mercadolibre.utils.TestScheduler
import com.elksa.sample.buscador.mercadolibre.utils.callPrivateFun
import com.elksa.sample.buscador.mercadolibre.utils.getProductUiModelFromProductEntity
import com.elksa.sample.buscador.mercadolibre.utils.getSampleProducts
import com.elksa.sample.buscador.mercadolibre.utils.setField
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val TAG = "ProductDetailsViewModel"
private const val FIELD_NAME_PRODUCT = "_product"
private const val FIELD_NAME_PRODUCT_DETAILS = "_productDetails"
private const val FIELD_NAME_COMPOSITE_DISPOSABLE = "compositeDisposable"
private const val FIELD_NAME_IS_THUMBNAIL_VISIBLE = "_isThumbnailVisible"
private const val FUNCTION_NAME_ON_CLEARED = "onCleared"

@RunWith(MockitoJUnitRunner::class)
class ProductDetailsViewModelTest {

    private lateinit var sut: ProductDetailsViewModel
    private val testScheduler = TestScheduler()

    @Mock
    private lateinit var fetchProductDetailsUseCaseMock: FetchProductDetailsUseCase

    @Mock
    private lateinit var loggerMock: ILogger

    @Mock
    private lateinit var compositeDisposableMock: CompositeDisposable

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        sut = ProductDetailsViewModel(fetchProductDetailsUseCaseMock, testScheduler, loggerMock)
    }

    @Test
    fun getProduct_productSet_returnsProduct() {
        // given
        val expectedProduct = getProductUiModelFromProductEntity(getSampleProducts()[0])
        setField(FIELD_NAME_PRODUCT, MutableLiveData(expectedProduct), sut)
        // when
        val result = sut.product
        // then
        assertEquals(expectedProduct, result.value)
    }

    @Test
    fun getProductDetails_productDetailsSet_returnsProductDetails() {
        // given
        val expectedProduct = getSampleProductDetails()
        setField(FIELD_NAME_PRODUCT_DETAILS, MutableLiveData(expectedProduct), sut)
        // when
        val result = sut.productDetails
        // then
        assertEquals(expectedProduct, result.value)
    }

    @Test
    fun isThumbnailVisible_trueSet_returnsTrue() {
        // given
        setField(FIELD_NAME_IS_THUMBNAIL_VISIBLE, MutableLiveData(true), sut)
        // when
        val result = sut.isThumbnailVisible
        // then
        assertEquals(true, result.value)
    }

    @Test
    fun init_productSet_productIsSet() {
        // given
        val expectedProduct = getProductUiModelFromProductEntity(getSampleProducts()[0])
        whenever(fetchProductDetailsUseCaseMock.fetchProductDetails(anyString()))
            .thenReturn(Single.just(getSampleProductDetails()))
        // when
        sut.init(expectedProduct)
        // then
        assertEquals(expectedProduct, sut.product.value)
    }

    @Test
    fun init_productSet_invokedFetchProductDetailsWithProductId() {
        // given
        val product = getProductUiModelFromProductEntity(getSampleProducts()[0])
        whenever(fetchProductDetailsUseCaseMock.fetchProductDetails(product.id))
            .thenReturn(Single.just(getSampleProductDetails()))
        // when
        sut.init(product)
        // then
        verify(fetchProductDetailsUseCaseMock).fetchProductDetails(product.id)
    }

    @Test
    fun init_onFetchProductDetailsSuccess_loaderGoneProductDetailsSet() {
        // given
        val productDetails = getSampleProductDetails()
        val product = getProductUiModelFromProductEntity(getSampleProducts()[0])
        val expectedProductDetailsUiModel = ProductDetailsUiModel.mapFromDomain(productDetails)
        whenever(fetchProductDetailsUseCaseMock.fetchProductDetails(product.id))
            .thenReturn(Single.just(productDetails))
        // when
        sut.init(product)
        // then
        assertEquals(GONE, sut.loaderVisibility.value)
        assertEquals(expectedProductDetailsUiModel, sut.productDetails.value)
    }

    @Test
    fun init_onFetchProductDetailsSuccessWithPictures_thumbnailNotVisible() {
        // given
        val productDetails = getSampleProductDetails()
        val product = getProductUiModelFromProductEntity(getSampleProducts()[0])
        whenever(fetchProductDetailsUseCaseMock.fetchProductDetails(product.id))
            .thenReturn(Single.just(productDetails))
        // when
        sut.init(product)
        // then
        assertEquals(false, sut.isThumbnailVisible.value)
    }

    @Test
    fun init_onFetchProductDetailsSuccessNoPictures_thumbnailVisible() {
        // given
        val productDetails = getSampleProductDetails(0)
        val product = getProductUiModelFromProductEntity(getSampleProducts()[0])
        whenever(fetchProductDetailsUseCaseMock.fetchProductDetails(product.id))
            .thenReturn(Single.just(productDetails))
        // when
        sut.init(product)
        // then
        assertEquals(true, sut.isThumbnailVisible.value)
    }

    @Test
    fun init_onFetchProductDetailsFailure_loaderGoneErrorLoggedThumbnailVisible() {
        // given
        val error = Throwable("error loadig product details")
        whenever(fetchProductDetailsUseCaseMock.fetchProductDetails(anyString()))
            .thenReturn(Single.error(error))
        val info = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_products_details
        )
        // when
        sut.init(getProductUiModelFromProductEntity(getSampleProducts()[0]))
        // then
        assertEquals(GONE, sut.loaderVisibility.value)
        assertEquals(true, sut.isThumbnailVisible.value)
        assertEquals(info, sut.errorEvent.value)
        verify(loggerMock).log(TAG, error.toString(), error, ERROR)
    }

    @Test
    fun init_existingProduct_newProductNotSet() {
        // given
        val product = getProductUiModelFromProductEntity(getSampleProducts()[0])
        setField(FIELD_NAME_PRODUCT, MutableLiveData(product), sut)
        whenever(fetchProductDetailsUseCaseMock.fetchProductDetails(anyString()))
            .thenReturn(Single.error(Throwable()))
        val newProduct = ProductUiModel(
            "newId",
            EMPTY_STRING,
            EMPTY_STRING,
            3,
            0,
            USED,
            EMPTY_STRING,
            false
        )
        // when
        sut.init(newProduct)
        // then
        assertEquals(product, sut.product.value)
    }

    @Test
    fun init_existingProductDetails_productDetailsNotLoaded() {
        // given
        setField(FIELD_NAME_PRODUCT_DETAILS, MutableLiveData(getSampleProductDetails()), sut)
        // when
        sut.init(getProductUiModelFromProductEntity(getSampleProducts()[0]))
        // then
        verifyZeroInteractions(fetchProductDetailsUseCaseMock)
    }

    @Test
    fun onBackPressed() {
        // when
        sut.onBackPressed()
        // then
        assertEquals(OnBackPressedEvent, sut.navigationEvent.value)
    }

    @Test
    fun onCleared_invoked_disposableCleared() {
        // given
        setField(FIELD_NAME_COMPOSITE_DISPOSABLE, compositeDisposableMock, sut)
        // when
        sut.callPrivateFun(FUNCTION_NAME_ON_CLEARED)
        // then
        verify(compositeDisposableMock).clear()
    }

    @Test
    fun updatePageIndicator_positionZero_currentPictureZeroSet() {
        // given
        val productDetails = ProductDetailsUiModel.mapFromDomain(getSampleProductDetails())
        setField(FIELD_NAME_PRODUCT_DETAILS, MutableLiveData(productDetails), sut)
        val position = 0
        // when
        sut.updatePageIndicator(position)
        // then
        assertEquals(position, sut.currentPicturePosition.value)
    }

    @Test
    fun updatePageIndicator_positionNegative_currentPictureRemainsNull() {
        // given
        val productDetails = ProductDetailsUiModel.mapFromDomain(getSampleProductDetails())
        setField(FIELD_NAME_PRODUCT_DETAILS, MutableLiveData(productDetails), sut)
        // when
        sut.updatePageIndicator( -1)
        // then
        assertNull(sut.currentPicturePosition.value)
    }

    @Test
    fun updatePageIndicator_positionOutOfBoundsPositive_currentPictureRemainsNull() {
        // given
        val productDetails = ProductDetailsUiModel.mapFromDomain(getSampleProductDetails(2))
        setField(FIELD_NAME_PRODUCT_DETAILS, MutableLiveData(productDetails), sut)
        // when
        sut.updatePageIndicator( 2)
        // then
        assertNull(sut.currentPicturePosition.value)
    }

    @Test
    fun updatePageIndicator_positionPositive_currentPictureSetToPosition() {
        // given
        val productDetails = ProductDetailsUiModel.mapFromDomain(getSampleProductDetails(3))
        setField(FIELD_NAME_PRODUCT_DETAILS, MutableLiveData(productDetails), sut)
        val position = 2
        // when
        sut.updatePageIndicator(position)
        // then
        assertEquals(position, sut.currentPicturePosition.value)
    }

    // region Helper methods
    private fun getSampleProductDetails(pictureCount: Int = 1) = ProductDetailsEntity(
        "id",
        "title",
        if (pictureCount > 0) {
            val pictures = mutableListOf<PictureEntity>()
            for (i in 1..pictureCount) {
                pictures.add(PictureEntity(i.toString(), "url"))
            }
            pictures.toList()
        } else listOf(),
        ItemDescriptionEntity("text", "plaintext")
    )
    // endregion
}