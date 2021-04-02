package com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus

import io.reactivex.Observable

interface IEventBus {

    fun publish(event: BusEvent)

    fun <T> listen(eventType: Class<T>): Observable<T>
}