package com.elksa.sample.buscador.mercadolibre.interactors

import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.framework.networking.MeliBrowserApi
import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ProductsSearchResultDto
import com.elksa.sample.buscador.mercadolibre.framework.networking.utils.SITE_ID_CO
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

@RunWith(MockitoJUnitRunner::class)
class SearchProductsUseCaseTest {

    private lateinit var sut: SearchProductsUseCase

    @Mock
    private lateinit var meliBrowserApiMock: MeliBrowserApi

    @Before
    fun setUp() {
        sut = SearchProductsUseCase(meliBrowserApiMock)
    }

    @Test
    fun searchProducts_onSuccess_returnsProductsSearch() {
        // given
        val productsSearchResult = ProductsSearchResultDto(EMPTY_STRING, listOf())
        whenever(meliBrowserApiMock.searchProducts(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Single.just(productsSearchResult))
        val expectedResult = productsSearchResult.mapToDomain()
        // when
        val result = sut.searchProducts(EMPTY_STRING, 0, 0)
        // then
        assertEquals(expectedResult, result.blockingGet())
    }

    @Test
    fun searchProducts_onFailure_returnsError() {
        // given
        val error = Throwable("error searching products")
        whenever(meliBrowserApiMock.searchProducts(anyString(), anyString(), anyInt(), anyInt()))
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
        whenever(meliBrowserApiMock.searchProducts(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(Throwable()))
        // when
        sut.searchProducts(query, offset, limit)
        // then
        verify(meliBrowserApiMock).searchProducts(SITE_ID_CO, query, offset, limit)
    }
}