package com.elksa.sample.buscador.mercadolibre.domain

data class ProductsSearchResultEntity(
    val idSite: String,
    val results: List<ProductEntity>
)