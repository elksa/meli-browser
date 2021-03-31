package com.elksa.sample.buscador.mercadolibre.presentation.utils

import android.view.View

fun View.setSingleClickListener(action: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(action))
}