package com.elksa.sample.buscador.mercadolibre.domain.entities

data class ProductDetailsEntity(
    val id: String,
    val title: String,
    val pictures: List<PictureEntity>,
    var description: ItemDescriptionEntity = ItemDescriptionEntity()
)