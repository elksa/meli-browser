package com.elksa.sample.buscador.mercadolibre.domain

import com.elksa.sample.buscador.mercadolibre.domain.utils.EMPTY_STRING

data class CategoryEntity(
    val id: String = EMPTY_STRING,
    val name: String = EMPTY_STRING,
    val picture: String = EMPTY_STRING
)