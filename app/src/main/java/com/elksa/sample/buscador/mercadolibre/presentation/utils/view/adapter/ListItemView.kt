package com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter

import android.content.Context
import android.view.View

interface ListItemView<T> {

    val context: Context

    val view: View

    val data: T

    fun bind(item: T)
}