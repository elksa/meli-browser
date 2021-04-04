package com.elksa.sample.buscador.mercadolibre.domain

import com.elksa.sample.buscador.mercadolibre.domain.utils.ItemDescriptionEntity

data class ProductDetailsEntity(
    val id: String,
    val title: String,
    val pictures: List<PictureEntity>,
    var description: ItemDescriptionEntity = ItemDescriptionEntity()
)