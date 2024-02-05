package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.utils.RandomGenerator
import com.elksa.sample.buscador.mercadolibre.utils.getField
import com.elksa.sample.buscador.mercadolibre.utils.getSampleProducts
import com.elksa.sample.buscador.mercadolibre.utils.setField
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

private const val FIELD_NAME_TOP_SEARCHES = "topSearches"
private const val FIELD_NAME_LAST_USED_INDEX = "lastUsedIndex"

@RunWith(MockitoJUnitRunner::class)
class FetchProductRecommendationsUseCaseTest {

    private lateinit var sut: FetchProductRecommendationsUseCase

    @Mock
    private lateinit var searchProductsUseCaseMock: SearchProductsUseCase

    @Mock
    private lateinit var randomGeneratorMock: RandomGenerator

    @Before
    fun setUp() {
        sut = FetchProductRecommendationsUseCase(searchProductsUseCaseMock, randomGeneratorMock)
    }

    @Test
    fun fetchProductRecommendations_onSuccess_returnsProductsSearch() {
        // given
        val products = getSampleProducts()
        `when`(searchProductsUseCaseMock.searchProducts(anyString(), anyInt(), anyInt()))
            .thenReturn(Single.just(products))
        // when
        val result = sut.fetchProductRecommendations(0, 0)
        // then
        assertEquals(products, result.blockingGet())
    }

    @Test
    fun fetchProductRecommendations_onFailure_returnsError() {
        // given
        val error = Throwable("error fetching recommendations")
        `when`(searchProductsUseCaseMock.searchProducts(anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(error))
        // when
        val result = sut.fetchProductRecommendations(0, 0)
        // then
        result.test().assertError(error)
    }

    @Test
    fun fetchProductRecommendations_newIndexGenerated_searchUseCaseInvokedWithNewIndexAndProperLimit() {
        // given
        val index = 0
        val offset = 1
        val limit = 2
        val error = Throwable("error fetching recommendations")
        val topSearches: List<String> = getField(sut, FIELD_NAME_TOP_SEARCHES)
        `when`(searchProductsUseCaseMock.searchProducts(anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(error))
        `when`(randomGeneratorMock.generateRandomInt(anyInt(), anyInt())).thenReturn(index)
        // when
        sut.fetchProductRecommendations(offset, limit)
        // then
        verify(searchProductsUseCaseMock).searchProducts(topSearches[index], offset, limit)
    }

    @Test
    fun fetchProductRecommendations_firstIndexGenerated_searchUseCaseInvokedWithFirstIndexAndProperLimit() {
        // given
        val index = 0
        val offset = 1
        val limit = 2
        val error = Throwable("error fetching recommendations")
        val topSearches: List<String> = getField(sut, FIELD_NAME_TOP_SEARCHES)
        setField(FIELD_NAME_LAST_USED_INDEX, index + 1, sut)
        `when`(searchProductsUseCaseMock.searchProducts(anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(error))
        `when`(randomGeneratorMock.generateRandomInt(anyInt(), anyInt())).thenReturn(index)
        // when
        sut.fetchProductRecommendations(offset, limit)
        // then
        verify(searchProductsUseCaseMock).searchProducts(topSearches[index], offset, limit)
    }

    @Test
    fun fetchProductRecommendations_previousIndexGeneratedInRange_searchUseCaseInvokedWithIndexPlusOneAndProperLimit() {
        // given
        val index = 0
        val expectedIndex = index + 1
        val offset = 1
        val limit = 2
        val error = Throwable("error fetching recommendations")
        val topSearches: List<String> = getField(sut, FIELD_NAME_TOP_SEARCHES)
        setField(FIELD_NAME_LAST_USED_INDEX, index, sut)
        `when`(searchProductsUseCaseMock.searchProducts(anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(error))
        `when`(randomGeneratorMock.generateRandomInt(anyInt(), anyInt())).thenReturn(index)
        // when
        sut.fetchProductRecommendations(offset, limit)
        // then
        verify(searchProductsUseCaseMock).searchProducts(topSearches[expectedIndex], offset, limit)
    }

    @Test
    fun fetchProductRecommendations_previousIndexGeneratedNotInRange_searchUseCaseInvokedWithZeroAndProperLimit() {
        // given
        val offset = 1
        val limit = 2
        val error = Throwable("error fetching recommendations")
        val topSearches: List<String> = getField(sut, FIELD_NAME_TOP_SEARCHES)
        val index = topSearches.size - 1
        val expectedIndex = 0
        setField(FIELD_NAME_LAST_USED_INDEX, index, sut)
        `when`(searchProductsUseCaseMock.searchProducts(anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(error))
        `when`(randomGeneratorMock.generateRandomInt(anyInt(), anyInt())).thenReturn(index)
        // when
        sut.fetchProductRecommendations(offset, limit)
        // then
        verify(searchProductsUseCaseMock).searchProducts(topSearches[expectedIndex], offset, limit)
    }
}