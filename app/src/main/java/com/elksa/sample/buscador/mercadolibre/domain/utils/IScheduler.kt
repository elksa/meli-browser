package com.elksa.sample.buscador.mercadolibre.domain.utils

import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer

interface IScheduler {
    fun <T> applySingleDefaultSchedulers(): SingleTransformer<T, T>
    fun <T> applyObservableDefaultSchedulers(): ObservableTransformer<T, T>
    fun applyCompletableDefaultSchedulers(): CompletableTransformer
}