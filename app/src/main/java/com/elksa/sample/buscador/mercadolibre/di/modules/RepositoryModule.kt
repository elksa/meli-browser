package com.elksa.sample.buscador.mercadolibre.di.modules

import com.elksa.sample.buscador.mercadolibre.domain.interfaces.IItemRepository
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.IProductRepository
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ISuggestionsRepository
import com.elksa.sample.buscador.mercadolibre.framework.repository.ItemRepository
import com.elksa.sample.buscador.mercadolibre.framework.repository.ProductRepository
import com.elksa.sample.buscador.mercadolibre.framework.repository.SuggestionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideProductRepository(impl: ProductRepository): IProductRepository

    @Singleton
    @Binds
    abstract fun provideItemRepository(impl: ItemRepository): IItemRepository

    @Singleton
    @Binds
    abstract fun provideSuggestionsRepository(impl: SuggestionsRepository): ISuggestionsRepository
}