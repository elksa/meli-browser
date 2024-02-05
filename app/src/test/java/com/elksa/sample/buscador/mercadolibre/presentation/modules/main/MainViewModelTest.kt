package com.elksa.sample.buscador.mercadolibre.presentation.modules.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elksa.sample.buscador.mercadolibre.domain.interactors.SaveRecentSearchUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBusPublisher
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.SearchProductEvent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private lateinit var sut: MainViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var eventBusPublisherMock: IEventBusPublisher

    @Mock
    private lateinit var saveRecentSearchUseCaseMok: SaveRecentSearchUseCase

    @Before
    fun setUp() {
        sut = MainViewModel(saveRecentSearchUseCaseMok, eventBusPublisherMock)
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
        verify(eventBusPublisherMock).publish(SearchProductEvent(query))
    }
}