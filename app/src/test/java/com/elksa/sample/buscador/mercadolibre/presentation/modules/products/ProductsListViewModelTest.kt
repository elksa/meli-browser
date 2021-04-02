package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.view.View
import android.view.View.GONE
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elksa.sample.buscador.mercadolibre.domain.ProductsSearchResultEntity
import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger.LogLevel.ERROR
import com.elksa.sample.buscador.mercadolibre.interactors.SearchProductsUseCase
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

    @Before
    fun setUp() {
        sut = ProductsListViewModel(searchProductsUseCaseMock, testScheduler, loggerMock)
    }

    @Test
    fun getProductsList_onSuccessWithResults_productsShownLoaderGone() {
        // given
        whenever(searchProductsUseCaseMock.searchProducts(anyString())).thenReturn(
            Single.just(
                ProductsSearchResultEntity(
                    EMPTY_STRING,
                    listOf()
                )
            )
        )
        // when
        sut.searchProducts(EMPTY_STRING)
        // then
        assertEquals(View.INVISIBLE, sut.loaderVisibility.value)
    }

    @Test
    fun getProductsList_onFailure_emptyStateShownLoaderGoneErrorLogged() {
        // given
        val error = Throwable("error loading products")
        whenever(searchProductsUseCaseMock.searchProducts(anyString())).thenReturn(
            Single.error(error)
        )
        // when
        sut.searchProducts("query")
        // then
        assertEquals(GONE, sut.loaderVisibility.value)
        verify(loggerMock).log(TAG, error.toString(), error, ERROR)
    }
}