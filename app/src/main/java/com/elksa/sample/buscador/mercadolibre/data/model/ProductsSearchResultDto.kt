package com.elksa.sample.buscador.mercadolibre.data.model

import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductsSearchResultEntity
import com.google.gson.annotations.SerializedName

data class ProductsSearchResultDto(
    @SerializedName("site_id")
    val idSite: String,
    val results: List<ProductDto>
) {

    fun mapToDomain() = ProductsSearchResultEntity(idSite, results.map { it.mapToDomain() })
}