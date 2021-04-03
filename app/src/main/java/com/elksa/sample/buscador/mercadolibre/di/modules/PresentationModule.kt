package com.elksa.sample.buscador.mercadolibre.di.modules

import com.elksa.sample.buscador.mercadolibre.presentation.utils.formatters.MoneyFormatter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.IEventBus
import com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus.RxEventBus
import com.elksa.sample.buscador.mercadolibre.presentation.utils.formatters.IMoneyFormatter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.imageLoader.GlideImageLoader
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.imageLoader.IImageLoader
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class PresentationModule {

    @Singleton
    @Binds
    abstract fun bindImageLoader(impl: GlideImageLoader): IImageLoader<*>

    @Singleton
    @Binds
    abstract fun bindEventBus(impl: RxEventBus): IEventBus

    @Singleton
    @Binds
    abstract fun bindMoneyFormatter(impl: MoneyFormatter): IMoneyFormatter
}