package com.elksa.sample.buscador.mercadolibre.framework.repository

import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.framework.networking.model.PictureDto
import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ProductDetailsDto
import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ProductsSearchResultDto
import com.elksa.sample.buscador.mercadolibre.framework.networking.services.MeliBrowserApi
import com.elksa.sample.buscador.mercadolibre.utils.getSampleProductsDto
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

private const val ID_SITE = "idSite"
private const val ID_PRODUCT = "idProduct"
private const val QUERY = "query"
private const val OFFSET = 1
private const val LIMIT = 2

@RunWith(MockitoJUnitRunner::class)
class ProductRepositoryTest {

    private lateinit var sut: ProductRepository

    @Mock
    private lateinit var apiMock: MeliBrowserApi

    @Before
    fun setUp() {
        sut = ProductRepository(apiMock)
    }

    @Test
    fun searchProducts_onSuccess_returnsProductSearchResult() {
        // given
        val productsDto = getSampleProductsDto()
        val searchResult = ProductsSearchResultDto(ID_SITE, productsDto)
        `when`(apiMock.searchProducts(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Single.just(searchResult))
        // when
        val result = sut.searchProducts(ID_SITE, EMPTY_STRING, 0, 0)
        // then
        val product = result.blockingGet()[0]
        assertEquals(productsDto[0].id, product.id)
        assertEquals(productsDto[0].price, product.price, 0.0)
        assertEquals(productsDto[0].quantity, product.quantity)
        assertEquals(productsDto[0].soldQuantity, product.soldQuantity)
        assertEquals(productsDto[0].thumbnail, product.thumbnail)
        assertEquals(productsDto[0].title, product.title)
        assertEquals(productsDto[0].condition.name, product.condition.name)
        assertEquals(productsDto[0].shipping.freeShipping, product.shippingInformation.freeShipping)
    }

    @Test
    fun searchProducts_onFailure_returnsError() {
        // given
        val error = Throwable("error searching products")
        `when`(apiMock.searchProducts(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(error))
        // when
        val result = sut.searchProducts(ID_SITE, QUERY, OFFSET, LIMIT)
        // then
       result.test().assertError(error)
    }

    @Test
    fun searchProducts_invocation_apiSearchProductsInvokedWithProperParams() {
        // given
        val error = Throwable("error searching products")
        `when`(apiMock.searchProducts(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(Single.error(error))
        // when
        sut.searchProducts(ID_SITE, QUERY, OFFSET, LIMIT)
        // then
        verify(apiMock).searchProducts(ID_SITE, QUERY, OFFSET, LIMIT)
    }

    @Test
    fun getProductDetails_onSuccess_returnsProductDetails() {
        // given
        val productDetailsDto = ProductDetailsDto(
            "id",
            "title",
            "category",
            listOf(PictureDto("idPicture", "url"))
        )
        `when`(apiMock.getProductDetails(anyString())).thenReturn(Single.just(productDetailsDto))
        // when
        val result = sut.getProductDetails(EMPTY_STRING)
        // then
        assertEquals(productDetailsDto.id, result.blockingGet().id)
        assertEquals(productDetailsDto.title, result.blockingGet().title)
        assertEquals(productDetailsDto.pictures.size, result.blockingGet().pictures.size)
        assertEquals(productDetailsDto.pictures[0].id, result.blockingGet().pictures[0].id)
        assertEquals(productDetailsDto.pictures[0].url, result.blockingGet().pictures[0].url)
    }

    @Test
    fun getProductDetails_onFailure_returnsError() {
        // given
        val error = Throwable("error loading description")
        `when`(apiMock.getProductDetails(anyString())).thenReturn(Single.error(error))
        // when
        val result = sut.getProductDetails(EMPTY_STRING)
        // then
        result.test().assertError(error)
    }

    @Test
    fun getProductDetails_invocation_apiGetDetailsInvokedWithProductId() {
        // given
        val error = Throwable("error loading description")
        `when`(apiMock.getProductDetails(anyString())).thenReturn(Single.error(error))
        // when
        sut.getProductDetails(ID_PRODUCT)
        // then
        verify(apiMock).getProductDetails(ID_PRODUCT)
    }
}