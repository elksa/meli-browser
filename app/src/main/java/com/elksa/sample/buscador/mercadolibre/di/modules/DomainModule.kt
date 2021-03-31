package com.elksa.sample.buscador.mercadolibre.di.modules

import com.elksa.sample.buscador.mercadolibre.domain.utils.ILogger
import com.elksa.sample.buscador.mercadolibre.domain.utils.IScheduler
import com.elksa.sample.buscador.mercadolibre.domain.utils.LogcatLogger
import com.elksa.sample.buscador.mercadolibre.domain.utils.RxScheduler
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DomainModule {

    @Singleton
    @Binds
    abstract fun bindScheduler(impl: RxScheduler): IScheduler

    @Singleton
    @Binds
    abstract fun bindLogger(impl: LogcatLogger): ILogger
}