package com.elksa.sample.buscador.mercadolibre.domain.entities

import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING

data class ItemDescriptionEntity(
    val text: String = EMPTY_STRING,
    val plainText: String = EMPTY_STRING
)