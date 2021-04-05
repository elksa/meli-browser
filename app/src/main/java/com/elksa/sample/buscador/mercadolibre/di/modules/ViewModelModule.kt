package com.elksa.sample.buscador.mercadolibre.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.elksa.sample.buscador.mercadolibre.di.ViewModelFactory
import com.elksa.sample.buscador.mercadolibre.di.ViewModelKey
import com.elksa.sample.buscador.mercadolibre.presentation.modules.details.ProductDetailsViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.modules.main.MainViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductsListViewModel::class)
    internal abstract fun bindProductsListViewModel(viewModel: ProductsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailsViewModel::class)
    internal abstract fun bindProductDetailsViewModel(viewModel: ProductDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}