package com.elksa.sample.buscador.mercadolibre.presentation.utils.view

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity.ItemCondition
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity.ItemCondition.NEW
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity.ItemCondition.USED
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.imageLoader.GlideImageLoader

@BindingAdapter("imageUrl")
fun ImageView.setImageFromUrl(url: String?) {
    url?.let { GlideImageLoader(context).loadImage(url, this) }
}

@BindingAdapter("shipping")
fun TextView.setShipment(freeShipping: Boolean?) {
    freeShipping?.let {
        background = ContextCompat.getDrawable(
            context,
            if (it) R.drawable.shp_bg_green_radius_xsmall else R.drawable.shp_bg_yellow_radius_xsmall
        )
        text = context.getString(
            if (it) R.string.label_shipment_free else R.string.label_shipment_not_free
        )
    }
}

@BindingAdapter("itemCondition")
fun TextView.setItemCondition(itemCondition: ItemCondition?) {
    itemCondition?.let {
        text = context.getString(
            when (it) {
                NEW -> R.string.label_condition_new
                USED -> R.string.label_condition_used
            }
        )
    }
}