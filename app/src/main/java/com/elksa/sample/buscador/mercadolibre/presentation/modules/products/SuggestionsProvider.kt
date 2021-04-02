package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.content.SearchRecentSuggestionsProvider

class SuggestionsProvider : SearchRecentSuggestionsProvider() {

    companion object {
        const val AUTHORITY = "com.elksa.sample.buscador.mercadolibre.presentation.modules.products.SuggestionsProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES // or DATABASE_MODE_2LINES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }
}