package com.elksa.sample.buscador.mercadolibre.presentation.utils

fun <T> concatenate(vararg lists: List<T>): List<T> {
    return listOf(*lists).flatten()
}