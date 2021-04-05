package com.elksa.sample.buscador.mercadolibre.di.modules

import android.app.Application
import android.content.Context
import android.provider.SearchRecentSuggestions
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.SuggestionsProvider.Companion.AUTHORITY
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.SuggestionsProvider.Companion.MODE
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
internal class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideSearchRecentSuggestions(context: Context) =
        SearchRecentSuggestions(context, AUTHORITY, MODE)
}