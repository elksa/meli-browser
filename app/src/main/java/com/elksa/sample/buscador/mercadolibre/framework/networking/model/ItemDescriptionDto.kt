package com.elksa.sample.buscador.mercadolibre.framework.networking.model

import com.elksa.sample.buscador.mercadolibre.domain.entities.ItemDescriptionEntity
import com.google.gson.annotations.SerializedName

data class ItemDescriptionDto(
    val text: String,
    @SerializedName("plain_text")
    val plainText: String
) {

    fun mapToDomain() = ItemDescriptionEntity(text, plainText)
}