package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.os.Parcelable
import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductUiModel(
    val id: String,
    val title: String,
    val price: Double,
    val idCurrency: String,
    val quantity: Int,
    val condition: String,
    val link: String,
    val thumbnail: String,
    val stopTime: String
) : Parcelable {

    companion object {

        fun mapFromDomain(productEntity: ProductEntity) = ProductUiModel(
            productEntity.id,
            productEntity.title,
            productEntity.price,
            productEntity.idCurrency,
            productEntity.quantity,
            productEntity.condition,
            productEntity.link,
            productEntity.thumbnail,
            productEntity.stopTime
        )
    }
}