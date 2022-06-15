package com.elksa.sample.buscador.mercadolibre.domain.interactors

import android.provider.SearchRecentSuggestions
import javax.inject.Inject

class SaveRecentSearchUseCase @Inject constructor(
    private val searchRecentSuggestions: SearchRecentSuggestions
) {

    fun saveRecentSearchQuery(query: String) {
        searchRecentSuggestions.saveRecentQuery(query, null)
    }
}