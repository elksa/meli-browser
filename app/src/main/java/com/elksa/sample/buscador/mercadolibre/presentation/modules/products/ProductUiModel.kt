package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.os.Parcelable
import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductEntity.ItemCondition
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductUiModel(
    val id: String,
    val title: String,
    val price: String,
    val quantity: Int,
    val soldQuantity: Int,
    val condition: ItemCondition,
    val thumbnail: String,
    val freeShipping: Boolean
) : Parcelable