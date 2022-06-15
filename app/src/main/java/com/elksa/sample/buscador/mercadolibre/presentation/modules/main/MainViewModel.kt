package com.elksa.sample.buscador.mercadolibre.presentation.modules.main

import androidx.lifecycle.ViewModel
import com.elksa.sample.buscador.mercadolibre.domain.interactors.SaveRecentSearchUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBusPublisher
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.SearchProductEvent
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val saveRecentSearchUseCase: SaveRecentSearchUseCase,
    private val eventBusPublisher: IEventBusPublisher
): ViewModel() {

    private fun saveRecentQuery(query: String) {
        saveRecentSearchUseCase.saveRecentSearchQuery(query)
    }

    fun performSearch(query: String) {
        saveRecentQuery(query)
        eventBusPublisher.publish(SearchProductEvent(query))
    }
}