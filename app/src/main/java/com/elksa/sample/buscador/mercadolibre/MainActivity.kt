package com.elksa.sample.buscador.mercadolibre

import android.app.SearchManager.QUERY
import android.content.Intent
import android.content.Intent.ACTION_SEARCH
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.widget.Toast
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.SuggestionsProvider.Companion.AUTHORITY
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.SuggestionsProvider.Companion.MODE
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        if (ACTION_SEARCH == intent.action) {
            val jargon = intent.getBooleanExtra("JARGON", false)
            intent.getStringExtra(QUERY)?.also { query ->
                saveRecentQuery(query)
                doMySearch("$query - $jargon")
            }
        }
    }

    private fun doMySearch(query: String) {
        Toast.makeText(this, "Searching for $query", Toast.LENGTH_SHORT).show()
    }

    private fun saveRecentQuery(query: String) {
        SearchRecentSuggestions(this, AUTHORITY, MODE).saveRecentQuery(query, null)
    }

    private fun clearRecentQueries() {
        SearchRecentSuggestions(this, AUTHORITY, MODE).clearHistory()
    }
}