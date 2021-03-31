package com.elksa.sample.buscador.mercadolibre.di.modules

import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.imageLoader.GlideImageLoader
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.imageLoader.IImageLoader
import dagger.Binds
import dagger.Module

@Module
abstract class PresentationModule {

    @Binds
    abstract fun bindImageLoader(impl: GlideImageLoader): IImageLoader<*>
}