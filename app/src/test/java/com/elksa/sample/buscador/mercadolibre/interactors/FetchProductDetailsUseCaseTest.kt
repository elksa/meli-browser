package com.elksa.sample.buscador.mercadolibre.interactors

import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.domain.utils.ItemDescriptionEntity
import com.elksa.sample.buscador.mercadolibre.framework.networking.MeliBrowserApi
import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ProductDetailsDto
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchProductDetailsUseCaseTest {

    private lateinit var sut: FetchProductDetailsUseCase

    @Mock
    private lateinit var meliBrowserApiMock: MeliBrowserApi

    @Mock
    private lateinit var fetchItemDescriptionUseCaseMock: FetchItemDescriptionUseCase

    @Before
    fun setUp() {
        sut = FetchProductDetailsUseCase(meliBrowserApiMock, fetchItemDescriptionUseCaseMock)
    }

    @Test
    fun fetchProductDetails_onSuccess_returnsProductDetails() {
        // given
        val productDetailsDto = ProductDetailsDto(
            "id", "title", "idCategory", listOf()
        )
        val itemDescription = ItemDescriptionEntity("text", "plaintext")
        whenever(meliBrowserApiMock.getProductDetails(anyString()))
            .thenReturn(Single.just(productDetailsDto))
        whenever(fetchItemDescriptionUseCaseMock.fetchItemDescription(anyString()))
            .thenReturn(Single.just(itemDescription))
        val expectedProductDetails = productDetailsDto.mapToDomain().apply {
            description = itemDescription
        }
        // when
        val result = sut.fetchProductDetails(EMPTY_STRING)
        // then
        assertEquals(expectedProductDetails, result.blockingGet())
    }

    @Test
    fun fetchProductDetails_onFailureDescription_returnsError() {
        // given
        val error = Throwable("error loading description")
        val productDetailsDto = ProductDetailsDto(
            "id", "title", "idCategory", listOf()
        )
        whenever(fetchItemDescriptionUseCaseMock.fetchItemDescription(anyString()))
            .thenReturn(Single.error(error))
        whenever(meliBrowserApiMock.getProductDetails(anyString()))
            .thenReturn(Single.just(productDetailsDto))
        // when
        val result = sut.fetchProductDetails(EMPTY_STRING)
        // then
        result.test().assertError(error)
    }

    @Test
    fun fetchProductDetails_onFailureDetails_returnsError() {
        // given
        val error = Throwable("error loading details")
        whenever(meliBrowserApiMock.getProductDetails(anyString()))
            .thenReturn(Single.error(error))
        // when
        val result = sut.fetchProductDetails(EMPTY_STRING)
        // then
        result.test().assertError(error)
    }

    @Test
    fun fetchProductDetails_invocation_apiDetailsInvokedWithItemId() {
        // given
        val itemId = "itemId"
        val productDetailsDto = ProductDetailsDto(
            itemId, "title", "idCategory", listOf()
        )
        whenever(meliBrowserApiMock.getProductDetails(anyString()))
            .thenReturn(Single.just(productDetailsDto))
        // when
        sut.fetchProductDetails(itemId)
        // then
        verify(meliBrowserApiMock).getProductDetails(itemId)
    }
}