package com.elksa.sample.buscador.mercadolibre.utils

import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductEntity
import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductEntity.ItemCondition
import com.elksa.sample.buscador.mercadolibre.domain.entities.ShippingInformationEntity
import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ProductDto
import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ProductDto.ItemConditionDto.NEW
import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ShippingInformationDto
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductUiModel

fun getSampleProducts() = listOf(
    ProductEntity(
        "id",
        "title",
        0.0,
        3,
        1,
        ItemCondition.NEW,
        "thumbnail",
        ShippingInformationEntity(true)
    )
)

fun getSampleProductsDto() = listOf(
    ProductDto(
        "id",
        "title",
        0.0,
        3,
        1,
        NEW,
        "thumbnail",
        ShippingInformationDto(true)
    )
)

fun getProductUiModelFromProductEntity(product: ProductEntity, price: String? = null) =
    ProductUiModel(
        product.id,
        product.title,
        price ?: product.price.toString(),
        product.quantity,
        product.soldQuantity,
        product.condition,
        product.thumbnail,
        true
    )