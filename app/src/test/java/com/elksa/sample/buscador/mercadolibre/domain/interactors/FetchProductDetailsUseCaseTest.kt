package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.domain.entities.ItemDescriptionEntity
import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductDetailsEntity
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.IProductRepository
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
    private lateinit var productRepositoryMock: IProductRepository

    @Mock
    private lateinit var fetchItemDescriptionUseCaseMock: FetchItemDescriptionUseCase

    @Before
    fun setUp() {
        sut = FetchProductDetailsUseCase(productRepositoryMock, fetchItemDescriptionUseCaseMock)
    }

    @Test
    fun fetchProductDetails_onSuccess_returnsProductDetails() {
        // given
        val productDetails = ProductDetailsEntity(
            "id", "title", listOf(), ItemDescriptionEntity()
        )
        val itemDescription = ItemDescriptionEntity("text", "plaintext")
        whenever(productRepositoryMock.getProductDetails(anyString()))
            .thenReturn(Single.just(productDetails))
        whenever(fetchItemDescriptionUseCaseMock.fetchItemDescription(anyString()))
            .thenReturn(Single.just(itemDescription))
        // when
        val result = sut.fetchProductDetails(EMPTY_STRING)
        // then
        assertEquals(productDetails, result.blockingGet())
    }

    @Test
    fun fetchProductDetails_onFailureDescription_returnsError() {
        // given
        val error = Throwable("error loading description")
        val productDetails = ProductDetailsEntity(
            "id", "title", listOf(), ItemDescriptionEntity()
        )
        whenever(fetchItemDescriptionUseCaseMock.fetchItemDescription(anyString()))
            .thenReturn(Single.error(error))
        whenever(productRepositoryMock.getProductDetails(anyString()))
            .thenReturn(Single.just(productDetails))
        // when
        val result = sut.fetchProductDetails(EMPTY_STRING)
        // then
        result.test().assertError(error)
    }

    @Test
    fun fetchProductDetails_onFailureDetails_returnsError() {
        // given
        val error = Throwable("error loading details")
        whenever(productRepositoryMock.getProductDetails(anyString()))
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
        val productDetails = ProductDetailsEntity(
            "id", "title", listOf(), ItemDescriptionEntity()
        )
        whenever(productRepositoryMock.getProductDetails(anyString()))
            .thenReturn(Single.just(productDetails))
        // when
        sut.fetchProductDetails(itemId)
        // then
        verify(productRepositoryMock).getProductDetails(itemId)
    }
}