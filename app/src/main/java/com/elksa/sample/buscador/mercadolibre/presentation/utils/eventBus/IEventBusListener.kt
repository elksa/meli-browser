package com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus

import io.reactivex.Observable

interface IEventBusListener {

    fun <T> listen(eventType: Class<T>): Observable<T>
}