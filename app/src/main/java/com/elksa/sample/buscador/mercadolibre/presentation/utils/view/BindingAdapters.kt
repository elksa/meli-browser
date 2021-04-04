package com.elksa.sample.buscador.mercadolibre.presentation.utils.view

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.elksa.sample.buscador.mercadolibre.R
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity.ItemCondition
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity.ItemCondition.NEW
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity.ItemCondition.USED
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductUiModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.imageLoader.GlideImageLoader

@BindingAdapter("imageUrl")
fun ImageView.setImageFromUrl(url: String?) {
    url?.let { GlideImageLoader(context).loadImage(url, this) }
}

@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    this.isVisible = isVisible
}

@BindingAdapter("freeShipping")
fun TextView.setFreeShipping(freeShipping: Boolean?) {
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

@BindingAdapter("itemConditionUnitsSold")
fun TextView.setItemConditionUnitsSold(product: ProductUiModel?) {
    product?.let {
        val condition = when (it.condition) {
            NEW -> context.getString(R.string.label_condition_new)
            USED -> context.getString(R.string.label_condition_used)
        }
        text = context.getString(
            R.string.format_details_item_condition_units_sold, condition, it.soldQuantity
        )
    }
}

@BindingAdapter(value = ["indicatorData", "indicatorPage"])
fun LinearLayout.setIndicatorData(itemCount: Int?, currentPage: Int?) {
    val hMargin = resources.getDimension(R.dimen.indicator_horizontal_margin).toInt()
    if (itemCount != null && childCount == 0) {
        for (i in 1..itemCount) {
            addView(
                ImageView(context).apply {
                    layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                        setMargins(hMargin, 0, hMargin, 0)
                    }
                    setImageResource(R.drawable.ic_circle_outlined)
                }
            )
        }
    }
    currentPage?.let { page ->
        for (i in 0 until childCount) {
            (getChildAt(i) as ImageView).run {
                this.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                    setMargins(hMargin, 0, hMargin, 0)
                }
                setImageResource(
                    if (page == i) R.drawable.ic_circle_filled
                    else R.drawable.ic_circle_outlined
                )
            }
        }
    }
}