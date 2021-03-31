package com.elksa.sample.buscador.mercadolibre.di.modules

import com.elksa.sample.buscador.mercadolibre.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity
}