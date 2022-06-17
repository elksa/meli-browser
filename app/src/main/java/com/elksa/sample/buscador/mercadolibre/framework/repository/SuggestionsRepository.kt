package com.elksa.sample.buscador.mercadolibre.framework.repository

import android.provider.SearchRecentSuggestions
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ISuggestionsRepository
import javax.inject.Inject

class SuggestionsRepository @Inject constructor(
    private val suggestionsService: SearchRecentSuggestions
) : ISuggestionsRepository {

    override fun clearHistory() {
        suggestionsService.clearHistory()
    }

    override fun saveRecentSearchQuery(query: String) {
        suggestionsService.saveRecentQuery(query, null)
    }
}