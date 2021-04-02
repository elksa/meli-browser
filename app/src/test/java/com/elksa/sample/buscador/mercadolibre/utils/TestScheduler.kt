package com.elksa.sample.buscador.mercadolibre.utils

import com.elksa.sample.buscador.mercadolibre.domain.utils.IScheduler
import io.reactivex.SingleTransformer
import io.reactivex.schedulers.Schedulers

class TestScheduler : IScheduler {

    override fun <T> applySingleDefaultSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
        }
    }
}
