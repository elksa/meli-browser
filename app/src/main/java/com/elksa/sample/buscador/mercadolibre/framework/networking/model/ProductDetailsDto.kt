package com.elksa.sample.buscador.mercadolibre.framework.networking.model

import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductDetailsEntity
import com.google.gson.annotations.SerializedName

data class ProductDetailsDto(
    val id: String,
    val title: String,
    @SerializedName("category_id")
    val idCategory: String,
    val pictures: List<PictureDto>
) {

    fun mapToDomain() = ProductDetailsEntity(
        id,
        title,
        pictures.map { it.mapToDomain() }
    )
}