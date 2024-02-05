package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.entities.ItemDescriptionEntity
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.IItemRepository
import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchItemDescriptionUseCaseTest {

    private lateinit var sut: FetchItemDescriptionUseCase

    @Mock
    private lateinit var repositoryMock: IItemRepository

    @Before
    fun setUp() {
        sut = FetchItemDescriptionUseCase(repositoryMock)
    }

    @Test
    fun fetchItemDescription_onSuccess_returnsItemDescription() {
        // given
        val itemDescription = ItemDescriptionEntity("text", "plaintext")
        `when`(repositoryMock.getItemDescription(anyString()))
            .thenReturn(Single.just(itemDescription))
        // when
        val result = sut.fetchItemDescription(EMPTY_STRING)
        // then
        assertEquals(itemDescription, result.blockingGet())
    }

    @Test
    fun fetchItemDescription_onFailure_returnsError() {
        // given
        val error = Throwable("error fetching item description")
        `when`(repositoryMock.getItemDescription(anyString()))
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
        `when`(repositoryMock.getItemDescription(anyString()))
            .thenReturn(Single.error(Throwable()))
        // when
        sut.fetchItemDescription(itemId)
        // then
        verify(repositoryMock).getItemDescription(itemId)
    }
}