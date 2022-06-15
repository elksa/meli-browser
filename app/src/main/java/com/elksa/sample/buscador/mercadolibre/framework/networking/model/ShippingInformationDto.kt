package com.elksa.sample.buscador.mercadolibre.framework.networking.model

import com.elksa.sample.buscador.mercadolibre.domain.entities.ShippingInformationEntity
import com.google.gson.annotations.SerializedName

class ShippingInformationDto(
    @SerializedName("free_shipping")
    val freeShipping: Boolean
) {

    fun mapToDomain() = ShippingInformationEntity(freeShipping)
}