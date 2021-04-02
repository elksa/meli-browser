package com.elksa.sample.buscador.mercadolibre.presentation.utils.eventBus

sealed class BusEvent

data class SearchProductEvent(val query: String) : BusEvent()