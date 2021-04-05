package com.elksa.sample.buscador.mercadolibre.presentation.modules.main

import android.provider.SearchRecentSuggestions
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
    private lateinit var searchRecentSuggestionsMock: SearchRecentSuggestions

    @Before
    fun setUp() {
        sut = MainViewModel(searchRecentSuggestionsMock, eventBusMock)
    }

    @Test
    fun performSearch_withQuery_saveRecentQuery() {
        // given
        val query = "query"
        // when
        sut.performSearch(query)
        // then
        verify(searchRecentSuggestionsMock).saveRecentQuery(query, null)
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