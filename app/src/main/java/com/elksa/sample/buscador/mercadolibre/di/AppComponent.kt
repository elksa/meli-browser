package com.elksa.sample.buscador.mercadolibre.di

import android.app.Application
import com.elksa.sample.buscador.mercadolibre.di.modules.*
import com.elksa.sample.buscador.mercadolibre.di.modules.ActivityBuildersModule
import com.elksa.sample.buscador.mercadolibre.di.modules.AppModule
import com.elksa.sample.buscador.mercadolibre.di.modules.FragmentBuildersModule
import com.elksa.sample.buscador.mercadolibre.presentation.application.MeliBrowserApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuildersModule::class,
        FragmentBuildersModule::class,
        NetworkingModule::class,
        AndroidModule::class,
        PresentationModule::class,
        RepositoryModule::class
    ]
)

interface AppComponent : AndroidInjector<MeliBrowserApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}