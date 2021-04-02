package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import android.os.Parcelable
import com.elksa.sample.buscador.mercadolibre.domain.ProductDetailsEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetailsUiModel(
    val id: String,
    val title: String
) : Parcelable {

    companion object {

        fun mapFromDomain(productDetailsEntity: ProductDetailsEntity) = ProductDetailsUiModel(
            productDetailsEntity.id,
            productDetailsEntity.title
        )
    }
}