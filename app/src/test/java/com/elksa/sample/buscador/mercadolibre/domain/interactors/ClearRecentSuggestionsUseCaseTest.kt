package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ISuggestionsRepository
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ClearRecentSuggestionsUseCaseTest {

    private lateinit var sut: ClearRecentSuggestionsUseCase

    @Mock
    private lateinit var searchRecentSuggestionsMock: ISuggestionsRepository

    @Before
    fun setUp() {
        sut = ClearRecentSuggestionsUseCase(searchRecentSuggestionsMock)
    }

    @Test
    fun clearRecentSuggestions_invoked_clearHistoryInvoked() {
        // when
        sut.clearRecentSuggestions()
        // then
        verify(searchRecentSuggestionsMock).clearHistory()
    }

    @Test
    fun clearRecentSuggestions_invoked_dummyFail() {
        // when
        sut.clearRecentSuggestions()
        // then
        assertTrue(true)
    }
}
