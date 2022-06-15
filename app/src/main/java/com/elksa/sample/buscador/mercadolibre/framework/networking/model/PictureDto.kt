package com.elksa.sample.buscador.mercadolibre.framework.networking.model

import com.elksa.sample.buscador.mercadolibre.domain.entities.PictureEntity
import com.google.gson.annotations.SerializedName

data class PictureDto(
    val id: String,
    @SerializedName("secure_url")
    val url: String
) {

    fun mapToDomain() = PictureEntity(id, url)
}