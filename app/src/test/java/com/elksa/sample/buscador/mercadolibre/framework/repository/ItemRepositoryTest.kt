package com.elksa.sample.buscador.mercadolibre.framework.repository

import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ItemDescriptionDto
import com.elksa.sample.buscador.mercadolibre.framework.networking.services.MeliBrowserApi
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

private const val ITEM_ID = "itemId"

@RunWith(MockitoJUnitRunner::class)
class ItemRepositoryTest {

    private lateinit var sut: ItemRepository

    @Mock
    private lateinit var apiMock: MeliBrowserApi

    @Before
    fun setUp() {
        sut = ItemRepository(apiMock)
    }

    @Test
    fun getItemDescription_onSuccess_returnsItemDescriptionWithProperValues() {
        // given
        val itemDescDto = ItemDescriptionDto("text", "plainText")
        whenever(apiMock.getItemDescription(anyString())).thenReturn(Single.just(itemDescDto))
        // when
        val result = sut.getItemDescription(ITEM_ID)
        // then
        assertEquals(itemDescDto.text, result.blockingGet().text)
        assertEquals(itemDescDto.plainText, result.blockingGet().plainText)
    }

    @Test
    fun getItemDescription_onFailure_returnsError() {
        // given
        val error = Throwable("error fetching item description")
        whenever(apiMock.getItemDescription(anyString())).thenReturn(Single.error(error))
        // when
        val result = sut.getItemDescription(ITEM_ID)
        // then
        result.test().assertError(error)
    }

    @Test
    fun getItemDescription_apiInvocation_apiGetItemDescriptionInvokedWithItemId() {
        // given
        val itemDescDto = ItemDescriptionDto("text", "plainText")
        whenever(apiMock.getItemDescription(anyString())).thenReturn(Single.just(itemDescDto))
        // when
        sut.getItemDescription(ITEM_ID)
        // then
        verify(apiMock).getItemDescription(ITEM_ID)
    }
}