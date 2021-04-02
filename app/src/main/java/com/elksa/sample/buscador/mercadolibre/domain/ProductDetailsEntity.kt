package com.elksa.sample.buscador.mercadolibre.domain

data class ProductDetailsEntity(
    val id: String,
    val title: String,
    val pictures: List<PictureEntity>,
    var category: CategoryEntity = CategoryEntity()
)