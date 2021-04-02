package com.elksa.sample.buscador.mercadolibre.domain.utils

import io.reactivex.SingleTransformer

interface IScheduler {
    fun <T> applySingleDefaultSchedulers(): SingleTransformer<T, T>
}