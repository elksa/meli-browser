package com.elksa.sample.buscador.mercadolibre.framework.networking.model

import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity.ItemCondition
import com.google.gson.annotations.SerializedName

data class ProductDto(
    val id: String,
    val title: String,
    val price: Double,
    @SerializedName("currency_id")
    val idCurrency: String,
    @SerializedName("available_quantity")
    val quantity: Int,
    @SerializedName("sold_quantity")
    val soldQuantity: Int,
    val condition: ItemConditionDto,
    @SerializedName("permalink")
    val link: String,
    val thumbnail: String,
    @SerializedName("stop_time")
    val stopTime: String,
    val shipping: ShippingInformationDto
) {

    enum class ItemConditionDto {
        @SerializedName("new") NEW,
        @SerializedName("used") USED;

        companion object {

            fun mapToDomain(type: ItemConditionDto) = when(type) {
                NEW -> ItemCondition.NEW
                USED -> ItemCondition.USED
            }
        }
    }

    fun mapToDomain() = ProductEntity(
        id,
        title,
        price,
        idCurrency,
        quantity,
        soldQuantity,
        ItemConditionDto.mapToDomain(condition),
        link,
        thumbnail,
        stopTime,
        shipping.mapToDomain()
    )
}