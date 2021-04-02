package com.elksa.sample.buscador.mercadolibre.framework.networking.model

import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity
import com.google.gson.annotations.SerializedName

data class ProductDto(
    val id: String,
    val title: String,
    val price: Double,
    @SerializedName("currency_id")
    val idCurrency: String,
    @SerializedName("available_quantity")
    val quantity: Int,
    val condition: String,
    @SerializedName("permalink")
    val link: String,
    val thumbnail: String,
    @SerializedName("stop_time")
    val stopTime: String
) {

    fun mapToDomain() = ProductEntity(
        id,
        title,
        price,
        idCurrency,
        quantity,
        condition,
        link,
        thumbnail,
        stopTime
    )
}