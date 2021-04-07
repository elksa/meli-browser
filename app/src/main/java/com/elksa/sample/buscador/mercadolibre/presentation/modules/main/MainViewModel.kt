package com.elksa.sample.buscador.mercadolibre.presentation.modules.main

import androidx.lifecycle.ViewModel
import com.elksa.sample.buscador.mercadolibre.interactors.SaveRecentSearchUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBus
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.SearchProductEvent
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val saveRecentSearchUseCase: SaveRecentSearchUseCase,
    private val eventBus: IEventBus
): ViewModel() {

    private fun saveRecentQuery(query: String) {
        saveRecentSearchUseCase.saveRecentSearchQuery(query)
    }

    fun performSearch(query: String) {
        saveRecentQuery(query)
        eventBus.publish(SearchProductEvent(query))
    }
}