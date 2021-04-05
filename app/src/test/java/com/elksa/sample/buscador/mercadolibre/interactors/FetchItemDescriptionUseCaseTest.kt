package com.elksa.sample.buscador.mercadolibre.interactors

import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import com.elksa.sample.buscador.mercadolibre.framework.networking.MeliBrowserApi
import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ItemDescriptionDto
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
class FetchItemDescriptionUseCaseTest {

    private lateinit var sut: FetchItemDescriptionUseCase

    @Mock
    private lateinit var meliBrowserApiMock: MeliBrowserApi

    @Before
    fun setUp() {
        sut = FetchItemDescriptionUseCase(meliBrowserApiMock)
    }

    @Test
    fun fetchItemDescription_onSuccess_returnsItemDescription() {
        // given
        val itemDescriptionDto = ItemDescriptionDto("text", "plaintext")
        whenever(meliBrowserApiMock.getItemDescription(anyString()))
            .thenReturn(Single.just(itemDescriptionDto))
        val expectedResult = itemDescriptionDto.mapToDomain()
        // when
        val result = sut.fetchItemDescription(EMPTY_STRING)
        // then
        assertEquals(expectedResult, result.blockingGet())
    }

    @Test
    fun fetchItemDescription_onFailure_returnsError() {
        // given
        val error = Throwable("error fetching item description")
        whenever(meliBrowserApiMock.getItemDescription(anyString()))
            .thenReturn(Single.error(error))
        // when
        val result = sut.fetchItemDescription(EMPTY_STRING)
        // then
        result.test().assertError(error)
    }

    @Test
    fun fetchItemDescription_invocation_apiGetItemDescriptionInvokedWithItemId() {
        // given
        val itemId = "itemId"
        whenever(meliBrowserApiMock.getItemDescription(anyString()))
            .thenReturn(Single.error(Throwable()))
        // when
        sut.fetchItemDescription(itemId)
        // then
        verify(meliBrowserApiMock).getItemDescription(itemId)
    }
}