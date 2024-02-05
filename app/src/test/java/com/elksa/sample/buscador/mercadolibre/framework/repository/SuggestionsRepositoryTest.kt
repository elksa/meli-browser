package com.elksa.sample.buscador.mercadolibre.framework.repository

import android.provider.SearchRecentSuggestions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

private const val QUERY = "query"

@RunWith(MockitoJUnitRunner::class)
class SuggestionsRepositoryTest {

    private lateinit var sut: SuggestionsRepository

    @Mock
    private lateinit var suggestionsServiceMock: SearchRecentSuggestions

    @Before
    fun setUp() {
        sut = SuggestionsRepository(suggestionsServiceMock)
    }

    @Test
    fun clearHistory_invocation_suggestionsServiceClearHistoryInvoked() {
        // when
        sut.clearHistory()
        // when
        verify(suggestionsServiceMock).clearHistory()
    }

    @Test
    fun saveRecentSearchQuery_invocation_suggestionsServiceSaveRecentQueryInvokedWithQuery() {
        // when
        sut.saveRecentSearchQuery(QUERY)
        // then
        verify(suggestionsServiceMock).saveRecentQuery(QUERY, null)
    }
}