package com.elksa.sample.buscador.mercadolibre.di.modules

import android.app.Application
import android.content.Context
import android.provider.SearchRecentSuggestions
import com.elksa.sample.buscador.mercadolibre.framework.android.SuggestionsProvider.Companion.AUTHORITY
import com.elksa.sample.buscador.mercadolibre.framework.android.SuggestionsProvider.Companion.MODE
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBusListener
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBusPublisher
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.RxEventBus
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

    @Singleton
    @Provides
    fun provideEventBus() = RxEventBus()

    @Singleton
    @Provides
    fun provideEventBusListener(eventBus: RxEventBus): IEventBusListener = eventBus

    @Singleton
    @Provides
    fun provideEventBusPublisher(eventBus: RxEventBus): IEventBusPublisher = eventBus
}