package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.elksa.sample.buscador.mercadolibre.databinding.ListItemPictureBinding
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.ListItemView
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.imageLoader.IImageLoader

class ProductItemView(
    override val context: Context,
    private val imageLoader: IImageLoader<*>,
) : ListItemView<PictureUiModel> {

    private val binding = ListItemPictureBinding.inflate(LayoutInflater.from(context))

    override val view = binding.root

    override lateinit var data: PictureUiModel

    init {
        binding.root.apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
    }

    override fun bind(item: PictureUiModel) {
        data = item
        imageLoader.loadImage(item.url, binding.imgItemPictureImage)
        binding.uiModel = item
        binding.executePendingBindings()
    }
}