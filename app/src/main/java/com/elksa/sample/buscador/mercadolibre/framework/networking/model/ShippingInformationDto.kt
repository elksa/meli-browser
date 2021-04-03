package com.elksa.sample.buscador.mercadolibre.framework.networking.model

import com.elksa.sample.buscador.mercadolibre.domain.ShippingInformationEntity
import com.google.gson.annotations.SerializedName

class ShippingInformationDto(
    @SerializedName("free_shipping")
    val freeShipping: Boolean,
    @SerializedName("store_pick_up")
    val storePickUp: Boolean
) {

    fun mapToDomain() = ShippingInformationEntity(
        freeShipping,
        storePickUp
    )
}