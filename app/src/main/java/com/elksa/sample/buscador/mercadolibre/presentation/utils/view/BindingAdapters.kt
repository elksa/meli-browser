package com.elksa.sample.buscador.mercadolibre.presentation.utils.view

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.imageLoader.GlideImageLoader

@BindingAdapter("imageUri")
fun ImageView.setImageFromUri(uri: String) {
    GlideImageLoader(context).loadImage(uri, this)
}