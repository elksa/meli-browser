package com.elksa.sample.buscador.mercadolibre.presentation.utils.view.imageLoader

import android.widget.ImageView

interface IImageLoader<L> {

    fun loadImage(uri: String, target: ImageView)

    fun loadImage(uri: String, target: ImageView, listener: L)
}