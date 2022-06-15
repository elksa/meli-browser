package com.elksa.sample.buscador.mercadolibre.domain.interactors

import android.provider.SearchRecentSuggestions
import javax.inject.Inject

class ClearRecentSuggestionsUseCase @Inject constructor(
    private val searchRecentSuggestions: SearchRecentSuggestions
) {

    fun clearRecentSuggestions() {
        searchRecentSuggestions.clearHistory()
    }
}