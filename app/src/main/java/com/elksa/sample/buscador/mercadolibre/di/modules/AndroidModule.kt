package com.elksa.sample.buscador.mercadolibre.di.modules

import com.elksa.sample.buscador.mercadolibre.domain.interfaces.ILogger
import com.elksa.sample.buscador.mercadolibre.framework.networking.utils.IScheduler
import com.elksa.sample.buscador.mercadolibre.framework.android.LogcatLogger
import com.elksa.sample.buscador.mercadolibre.framework.networking.utils.RxScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AndroidModule {

    @Singleton
    @Binds
    abstract fun bindScheduler(impl: RxScheduler): IScheduler

    @Singleton
    @Binds
    abstract fun bindLogger(impl: LogcatLogger): ILogger
}