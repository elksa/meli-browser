package com.elksa.sample.buscador.mercadolibre.di.modules

import android.content.Context
import android.provider.SearchRecentSuggestions
import com.elksa.sample.buscador.mercadolibre.framework.android.SuggestionsProvider.Companion.AUTHORITY
import com.elksa.sample.buscador.mercadolibre.framework.android.SuggestionsProvider.Companion.MODE
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBusListener
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBusPublisher
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.RxEventBus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class AppModule {

    @Singleton
    @Provides
    fun provideSearchRecentSuggestions(@ApplicationContext context: Context) =
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