package com.elksa.sample.buscador.mercadolibre.domain

data class ProductEntity(
    val id: String,
    val title: String,
    val price: Double,
    val idCurrency: String,
    val quantity: Int,
    val condition: ItemCondition,
    val link: String,
    val thumbnail: String,
    val stopTime: String,
    val shippingInformation: ShippingInformationEntity
) {

    enum class ItemCondition {
        NEW, USED
    }
}