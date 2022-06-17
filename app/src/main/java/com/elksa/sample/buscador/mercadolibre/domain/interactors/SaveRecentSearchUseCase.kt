package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ISuggestionsRepository
import javax.inject.Inject

class SaveRecentSearchUseCase @Inject constructor(
    private val suggestionsRepository: ISuggestionsRepository
) {

    fun saveRecentSearchQuery(query: String) {
        suggestionsRepository.saveRecentSearchQuery(query)
    }
}