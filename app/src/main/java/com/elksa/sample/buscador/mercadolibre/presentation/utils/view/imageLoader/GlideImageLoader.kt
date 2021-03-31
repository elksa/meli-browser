package com.elksa.sample.buscador.mercadolibre.presentation.utils.view.imageLoader

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.elksa.sample.buscador.mercadolibre.R
import javax.inject.Inject

class GlideImageLoader @Inject constructor(
    private val context: Context
) : IImageLoader<RequestListener<Drawable>> {

    private val options by lazy {
        RequestOptions()
            .dontTransform()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
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