package com.elksa.sample.buscador.mercadolibre.framework.networking.utils

import io.reactivex.SingleTransformer

interface IScheduler {
    fun <T> applySingleDefaultSchedulers(): SingleTransformer<T, T>
}