package com.elksa.sample.buscador.mercadolibre.framework.networking.model

import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductEntity
import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductEntity.ItemCondition
import com.google.gson.annotations.SerializedName

data class ProductDto(
    val id: String,
    val title: String,
    val price: Double,
    @SerializedName("available_quantity")
    val quantity: Int,
    @SerializedName("sold_quantity")
    val soldQuantity: Int,
    val condition: ItemConditionDto,
    val thumbnail: String,
    val shipping: ShippingInformationDto
) {

    enum class ItemConditionDto {
        @SerializedName("new") NEW,
        @SerializedName("used") USED,
        @SerializedName("not_specified") NOT_SPECIFIED;

        companion object {

            fun mapToDomain(type: ItemConditionDto) = when(type) {
                NEW -> ItemCondition.NEW
                USED -> ItemCondition.USED
                NOT_SPECIFIED -> ItemCondition.NOT_SPECIFIED
            }
        }
    }

    fun mapToDomain() = ProductEntity(
        id,
        title,
        price,
        quantity,
        soldQuantity,
        ItemConditionDto.mapToDomain(condition),
        thumbnail,
        shipping.mapToDomain()
    )
}