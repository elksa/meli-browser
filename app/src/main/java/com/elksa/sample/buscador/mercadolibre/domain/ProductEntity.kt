package com.elksa.sample.buscador.mercadolibre.domain

data class ProductEntity(
    val id: String,
    val title: String,
    val price: Double,
    val quantity: Int,
    val soldQuantity: Int,
    val condition: ItemCondition,
    val thumbnail: String,
    val shippingInformation: ShippingInformationEntity
) {

    enum class ItemCondition {
        NEW, USED, NOT_SPECIFIED
    }
}