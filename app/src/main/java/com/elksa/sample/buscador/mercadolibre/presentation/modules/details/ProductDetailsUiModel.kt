package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductDetailsEntity

data class ProductDetailsUiModel(
    val id: String,
    val title: String,
    val pictures: List<PictureUiModel>,
    var description: String
) {

    companion object {

        fun mapFromDomain(productDetailsEntity: ProductDetailsEntity) = ProductDetailsUiModel(
            productDetailsEntity.id,
            productDetailsEntity.title,
            productDetailsEntity.pictures.map { PictureUiModel.mapFromDomain(it) },
            productDetailsEntity.description.plainText
        )
    }
}