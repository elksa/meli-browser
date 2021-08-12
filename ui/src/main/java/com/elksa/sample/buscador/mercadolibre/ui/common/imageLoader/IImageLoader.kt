package com.elksa.sample.buscador.mercadolibre.ui.common.imageLoader

import android.widget.ImageView

interface IImageLoader<L> {

    fun loadImage(uri: String, target: ImageView)

    fun loadImage(uri: String, target: ImageView, listener: L)
}