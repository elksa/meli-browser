package com.elksa.sample.buscador.mercadolibre.ui.common.imageLoader

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.elksa.sample.buscador.mercadolibre.ui.R

class GlideImageLoader(
    private val context: Context
) : IImageLoader<RequestListener<Drawable>> {

    private val options by lazy {
        RequestOptions()
            .dontTransform()
            .placeholder(R.drawable.ic_image_placeholder)
            .error(R.drawable.ic_broken_image)
    }

    override fun loadImage(uri: String, target: ImageView) {
        Glide.with(context)
            .load(uri)
            .apply(options)
            .into(target)
    }

    override fun loadImage(uri: String, target: ImageView, listener: RequestListener<Drawable>) {
        Glide.with(context)
            .load(uri)
            .apply(options)
            .listener(listener)
            .into(target)
    }
}