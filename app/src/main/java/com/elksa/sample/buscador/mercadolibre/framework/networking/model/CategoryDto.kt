package com.elksa.sample.buscador.mercadolibre.framework.networking.model

import com.elksa.sample.buscador.mercadolibre.domain.CategoryEntity

data class CategoryDto(
    val id: String,
    val name: String,
    val picture: String
) {

    fun mapToDomain() = CategoryEntity(
        id,
        name,
        picture
    )
}