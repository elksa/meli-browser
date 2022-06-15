package com.elksa.sample.buscador.mercadolibre.domain.interactors

import android.provider.SearchRecentSuggestions
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveRecentSearchUseCaseTest {

    private lateinit var sut: SaveRecentSearchUseCase

    @Mock
    private lateinit var searchRecentSuggestionsMock: SearchRecentSuggestions

    @Before
    fun setUp() {
        sut = SaveRecentSearchUseCase(searchRecentSuggestionsMock)
    }

    @Test
    fun saveRecentSearchQuery_validRecentQuery_saveRecentQueryInvokedWithProperQuery() {
        // given
        val query = "query"
        // when
        sut.saveRecentSearchQuery(query)
        // then
        verify(searchRecentSuggestionsMock).saveRecentQuery(query, null)
    }
}