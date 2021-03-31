package com.elksa.sample.buscador.mercadolibre.domain.entities

data class ProductsSearchResultEntity(
    val idSite: String,
    val results: List<ProductEntity>
)