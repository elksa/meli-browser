package com.elksa.sample.buscador.mercadolibre.di.modules

import com.elksa.sample.buscador.mercadolibre.presentation.utils.formatters.IMoneyFormatter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.formatters.MoneyFormatter
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class PresentationModule {

    @Singleton
    @Binds
    abstract fun bindMoneyFormatter(impl: MoneyFormatter): IMoneyFormatter
}