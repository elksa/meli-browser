package com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus

interface IEventBusPublisher {

    fun publish(event: BusEvent)
}