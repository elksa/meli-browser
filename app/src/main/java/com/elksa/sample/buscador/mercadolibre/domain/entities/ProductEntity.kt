package com.elksa.sample.buscador.mercadolibre.domain.entities

data class ProductEntity(
    val id: String,
    val title: String,
    val price: Double,
    val idCurrency: String,
    val quantity: Int,
    val condition: String,
    val link: String,
    val thumbnail: String,
    val stopTime: String
)