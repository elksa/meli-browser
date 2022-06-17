package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ISuggestionsRepository
import javax.inject.Inject

class ClearRecentSuggestionsUseCase @Inject constructor(
    private val suggestionsRepository: ISuggestionsRepository
) {

    fun clearRecentSuggestions() {
        suggestionsRepository.clearHistory()
    }
}