package com.elksa.sample.buscador.mercadolibre.presentation.modules.main

import android.app.SearchManager.QUERY
import android.content.Intent
import android.content.Intent.ACTION_SEARCH
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.elksa.sample.buscador.mercadolibre.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

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
                viewModel.performSearch(query)
            }
        }
    }
}