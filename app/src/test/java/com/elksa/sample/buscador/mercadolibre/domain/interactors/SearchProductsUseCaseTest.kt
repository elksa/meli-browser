package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.interfaces.IProductRepository
import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.utils.getSampleProductsDto
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val SITE_ID_CO = "MCO"

@RunWith(MockitoJUnitRunner::class)
class SearchProductsUseCaseTest {

    private lateinit var sut: SearchProductsUseCase

    @Mock
    private lateinit var productRepositoryMock: IProductRepository

    @Before
    fun setUp() {
        sut = SearchProductsUseCase(productRepositoryMock)
    }

    @Test
    fun searchProducts_onSuccess_returnsProductsSearch() {
        // given
        val products = getSampleProductsDto().map { it.mapToDomain() }
        whenever(productRepositoryMock.searchProducts(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Single.just(products))
        // when
        val result = sut.searchProducts(EMPTY_STRING, 0, 0)
        // then
        assertEquals(products[0].id, result.blockingGet()[0].id)
        assertEquals(products[0].price, result.blockingGet()[0].price, 0.0)
        assertEquals(products[0].quantity, result.blockingGet()[0].quantity)
        assertEquals(products[0].soldQuantity, result.blockingGet()[0].soldQuantity)
        assertEquals(products[0].thumbnail, result.blockingGet()[0].thumbnail)
        assertEquals(products[0].title, result.blockingGet()[0].title)
        assertEquals(products[0].condition, result.blockingGet()[0].condition)
        assertEquals(
            products[0].shippingInformation.freeShipping,
            result.blockingGet()[0].shippingInformation.freeShipping
        )
    }

    @Test
    fun searchProducts_onFailure_returnsError() {
        // given
        val error = Throwable("error searching products")
        whenever(productRepositoryMock.searchProducts(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(error))
        // when
        val result = sut.searchProducts(EMPTY_STRING, 0, 0)
        // then
        result.test().assertError(error)
    }

    @Test
    fun searchProducts_invocation_apiSearchProductsInvokedWithProperParams() {
        // given
        val query = "query"
        val offset = 1
        val limit = 2
        whenever(productRepositoryMock.searchProducts(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(Throwable()))
        // when
        sut.searchProducts(query, offset, limit)
        // then
        verify(productRepositoryMock).searchProducts(SITE_ID_CO, query, offset, limit)
    }
}