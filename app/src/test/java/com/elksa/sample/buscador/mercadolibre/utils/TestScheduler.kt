package com.elksa.sample.buscador.mercadolibre.utils

import com.elksa.sample.buscador.mercadolibre.domain.utils.IScheduler
import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.schedulers.Schedulers

class TestScheduler : IScheduler {

    override fun <T> applySingleDefaultSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
        }
    }

    override fun <T> applyObservableDefaultSchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
        }
    }

    override fun applyCompletableDefaultSchedulers(): CompletableTransformer {
        return CompletableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
        }
    }
}
