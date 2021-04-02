package com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RxEventBus @Inject constructor() : IEventBus {

    private val publisher = PublishSubject.create<BusEvent>()

    override fun publish(event: BusEvent) {
        publisher.onNext(event)
    }

    override fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}