package com.elksa.sample.buscador.mercadolibre.presentation.modules.main

import com.elksa.sample.buscador.mercadolibre.domain.interactors.SaveRecentSearchUseCase
import com.elksa.sample.buscador.mercadolibre.presentation.modules.common.BaseViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBusPublisher
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.SearchProductEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveRecentSearchUseCase: SaveRecentSearchUseCase,
    private val eventBusPublisher: IEventBusPublisher
): BaseViewModel() {

    private fun saveRecentQuery(query: String) {
        saveRecentSearchUseCase.saveRecentSearchQuery(query)
    }

    fun performSearch(query: String) {
        saveRecentQuery(query)
        eventBusPublisher.publish(SearchProductEvent(query))
    }
}