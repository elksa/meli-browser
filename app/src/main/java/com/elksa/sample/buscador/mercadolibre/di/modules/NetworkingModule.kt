package com.elksa.sample.buscador.mercadolibre.di.modules

import com.elksa.sample.buscador.mercadolibre.data.utils.SERVER_URL
import com.elksa.sample.buscador.mercadolibre.framework.networking.MeliBrowserApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkingModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HttpLoggingInterceptor().apply { level = BODY })

        return Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideMeliBrowserApi(retrofit: Retrofit): MeliBrowserApi =
        retrofit.create(MeliBrowserApi::class.java)
}