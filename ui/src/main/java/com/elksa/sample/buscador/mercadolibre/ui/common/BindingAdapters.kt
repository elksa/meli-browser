package com.elksa.sample.buscador.mercadolibre.ui.common

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.elksa.sample.buscador.mercadolibre.ui.common.imageLoader.GlideImageLoader

@BindingAdapter("imageUrl")
fun ImageView.setImageFromUrl(url: String?) {
    url?.let { GlideImageLoader(context).loadImage(url, this) }
}

@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    this.isVisible = isVisible
}