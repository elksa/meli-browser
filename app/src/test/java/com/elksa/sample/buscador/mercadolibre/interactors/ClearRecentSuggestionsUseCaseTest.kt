package com.elksa.sample.buscador.mercadolibre.interactors

import android.provider.SearchRecentSuggestions
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ClearRecentSuggestionsUseCaseTest {

    private lateinit var sut: ClearRecentSuggestionsUseCase

    @Mock
    private lateinit var searchRecentSuggestionsMock: SearchRecentSuggestions

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
}