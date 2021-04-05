package com.elksa.sample.buscador.mercadolibre.presentation.modules.main

import android.provider.SearchRecentSuggestions
import androidx.lifecycle.ViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBus
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.SearchProductEvent
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val recentSuggestionsProvider: SearchRecentSuggestions,
    private val eventBus: IEventBus
): ViewModel() {

    private fun saveRecentQuery(query: String) {
        recentSuggestionsProvider.saveRecentQuery(query, null)
    }

    fun performSearch(query: String) {
        saveRecentQuery(query)
        eventBus.publish(SearchProductEvent(query))
    }
}