package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import android.os.Parcelable
import com.elksa.sample.buscador.mercadolibre.domain.CategoryEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryUiModel(
    val id: String,
    val name: String,
    val picture: String
) : Parcelable {

    companion object {

        fun mapFromDomain(categoryEntity: CategoryEntity) = CategoryUiModel(
            categoryEntity.id,
            categoryEntity.name,
            categoryEntity.picture
        )
    }
}