package com.elksa.sample.buscador.mercadolibre.di.modules

import com.elksa.sample.buscador.mercadolibre.presentation.modules.details.ProductDetailsFragment
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindProductsListFragment(): ProductsListFragment

    @ContributesAndroidInjector
    internal abstract fun bindProductDetailsFragment(): ProductDetailsFragment
}