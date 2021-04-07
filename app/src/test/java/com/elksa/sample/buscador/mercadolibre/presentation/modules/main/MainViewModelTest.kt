package com.elksa.sample.buscador.mercadolibre.presentation.modules.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elksa.sample.buscador.mercadolibre.interactors.SaveRecentSearchUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBus
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.SearchProductEvent
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private lateinit var sut: MainViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var eventBusMock: IEventBus

    @Mock
    private lateinit var saveRecentSearchUseCaseMok: SaveRecentSearchUseCase

    @Before
    fun setUp() {
        sut = MainViewModel(saveRecentSearchUseCaseMok, eventBusMock)
    }

    @Test
    fun performSearch_withQuery_saveRecentQuery() {
        // given
        val query = "query"
        // when
        sut.performSearch(query)
        // then
        verify(saveRecentSearchUseCaseMok).saveRecentSearchQuery(query)
    }

    @Test
    fun performSearch_withQuery_publishSearchProductEventWithQuery() {
        // given
        val query = "query"
        // when
        sut.performSearch(query)
        // then
        verify(eventBusMock).publish(SearchProductEvent(query))
    }
}