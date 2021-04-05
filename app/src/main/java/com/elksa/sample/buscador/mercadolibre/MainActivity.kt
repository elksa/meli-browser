package com.elksa.sample.buscador.mercadolibre

import android.app.SearchManager.QUERY
import android.content.Intent
import android.content.Intent.ACTION_SEARCH
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.SuggestionsProvider.Companion.AUTHORITY
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.SuggestionsProvider.Companion.MODE
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBus
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.SearchProductEvent
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var eventBus: IEventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        if (ACTION_SEARCH == intent.action) {
            intent.getStringExtra(QUERY)?.also { query ->
                saveRecentQuery(query)
                doSearch(query)
            }
        }
    }

    private fun doSearch(query: String) {
        eventBus.publish(SearchProductEvent(query))
    }

    private fun saveRecentQuery(query: String) {
        SearchRecentSuggestions(this, AUTHORITY, MODE).saveRecentQuery(query, null)
    }
}