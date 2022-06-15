package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import com.elksa.sample.buscador.mercadolibre.domain.entities.PictureEntity

data class PictureUiModel(
    val url: String
) {

    companion object {

        fun mapFromDomain(pictureEntity: PictureEntity) = PictureUiModel(pictureEntity.url)
    }
}