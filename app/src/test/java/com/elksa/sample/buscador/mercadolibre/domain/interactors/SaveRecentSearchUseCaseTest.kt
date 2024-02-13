package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ISuggestionsRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveRecentSearchUseCaseTest {

    private lateinit var sut: SaveRecentSearchUseCase

    @Mock
    private lateinit var suggestionsRepositoryMock: ISuggestionsRepository

    @Before
    fun setUp() {
        sut = SaveRecentSearchUseCase(suggestionsRepositoryMock)
    }

    @Test
    fun saveRecentSearchQuery_validRecentQuery_saveRecentQueryInvokedWithProperQuery() {
        // given
        val query = "query"
        // when
        sut.saveRecentSearchQuery(query)
        // then
        verify(suggestionsRepositoryMock).saveRecentSearchQuery(query)
    }
}