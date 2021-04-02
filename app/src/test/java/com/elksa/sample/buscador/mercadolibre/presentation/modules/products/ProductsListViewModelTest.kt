package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.view.View.GONE
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity
import com.elksa.sample.buscador.mercadolibre.domain.ProductsSearchResultEntity
import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.interactors.SearchProductsUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductsListFragmentDirections.Companion.actionDestProductsListFragmentToDestProductDetailsFragment
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBus
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationToDirectionEvent
import com.elksa.sample.buscador.mercadolibre.utils.TestScheduler
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val TAG = "ProductsListViewModel"

@RunWith(MockitoJUnitRunner::class)
class ProductsListViewModelTest {

    private lateinit var sut: ProductsListViewModel
    private val testScheduler = TestScheduler()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var searchProductsUseCaseMock: SearchProductsUseCase

    @Mock
    private lateinit var loggerMock: ILogger

    @Mock
    private lateinit var eventBusMock: IEventBus

    @Before
    fun setUp() {
        sut = ProductsListViewModel(
            searchProductsUseCaseMock,
            testScheduler,
            loggerMock,
            eventBusMock
        )
    }

    @Test
    fun getProductsList_onSuccessWithResults_productsSetLoaderGone() {
        // given
        val products = getSampleProducts()
        whenever(searchProductsUseCaseMock.searchProducts(anyString(), anyString())).thenReturn(
            Single.just(ProductsSearchResultEntity(EMPTY_STRING, products))
        )
        // when
        sut.searchProducts(EMPTY_STRING)
        // then
        assertEquals(products.size, sut.productsList.value?.size)
        assertEquals(GONE, sut.loaderVisibility.value)
    }

    @Test
    fun getProductsList_onSuccessWithResults_productsProperlyMapped() {
        // given
        val products = getSampleProducts()
        whenever(searchProductsUseCaseMock.searchProducts(anyString(), anyString())).thenReturn(
            Single.just(ProductsSearchResultEntity(EMPTY_STRING, products))
        )
        val expectedProductUiModel = getProductUiModelFromProductEntity(products[0])
        // when
        sut.searchProducts(EMPTY_STRING)
        // then
        assertEquals(products.size, sut.productsList.value?.size)
        assertEquals(expectedProductUiModel, sut.productsList.value?.get(0))
    }

    @Test
    fun getProductsList_onFailure_emptyStateShownLoaderGoneErrorLogged() {
        // given
        val error = Throwable("error loading products")
        whenever(searchProductsUseCaseMock.searchProducts(anyString(), anyString())).thenReturn(
            Single.error(error)
        )
        // when
        sut.searchProducts(EMPTY_STRING)
        // then
        assertEquals(GONE, sut.loaderVisibility.value)
        verify(loggerMock).log(TAG, error.toString(), error, ERROR)
    }

    @Test
    fun onProductSelected_productSelected_navigateToProductDetailsEventTriggered() {
        // given
        val selectedProduct = getProductUiModelFromProductEntity(getSampleProducts()[0])
        val expectedEvent = NavigationToDirectionEvent(
            actionDestProductsListFragmentToDestProductDetailsFragment(selectedProduct)
        )
        // when
        sut.onProductSelected(selectedProduct)
        // then
        assertEquals(expectedEvent, sut.navigationEvent.value)
    }

    // region Helper methods
    private fun getSampleProducts() = listOf(
        ProductEntity(
            "id",
            "title",
            0.0,
            "COP",
            1,
            "new",
            "link",
            "thumbnail",
            "stop"
        )
    )

    private fun getProductUiModelFromProductEntity(product: ProductEntity) = ProductUiModel(
        product.id,
        product.title,
        product.price,
        product.idCurrency,
        product.quantity,
        product.condition,
        product.link,
        product.thumbnail,
        product.stopTime
    )
    // endregion
}