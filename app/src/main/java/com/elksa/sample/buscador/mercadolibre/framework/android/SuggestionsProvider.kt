package com.elksa.sample.buscador.mercadolibre.framework.android

import android.content.SearchRecentSuggestionsProvider

class SuggestionsProvider : SearchRecentSuggestionsProvider() {

    companion object {
        const val AUTHORITY = "com.elksa.sample.buscador.mercadolibre.framework.android.SuggestionsProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }
}