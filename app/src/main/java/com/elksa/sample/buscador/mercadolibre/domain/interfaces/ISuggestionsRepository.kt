package com.elksa.sample.buscador.mercadolibre.domain.interfaces

interface ISuggestionsRepository {

    fun clearHistory()

    fun saveRecentSearchQuery(query: String)
}