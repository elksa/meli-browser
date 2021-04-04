package com.elksa.sample.buscador.mercadolibre.utils

import com.elksa.sample.buscador.mercadolibre.domain.ProductEntity
import com.elksa.sample.buscador.mercadolibre.domain.ShippingInformationEntity
import com.elksa.sample.buscador.mercadolibre.presentation.modules.products.ProductUiModel

fun getSampleProducts() = listOf(
    ProductEntity(
        "id",
        "title",
        0.0,
        "COP",
        3,
        1,
        ProductEntity.ItemCondition.NEW,
        "link",
        "thumbnail",
        "stop",
        ShippingInformationEntity(freeShipping = true, storePickUp = false)
    )
)

fun getProductUiModelFromProductEntity(product: ProductEntity, price: String? = null) =
    ProductUiModel(
        product.id,
        product.title,
        price ?: product.price.toString(),
        product.idCurrency,
        product.quantity,
        product.soldQuantity,
        product.condition,
        product.link,
        product.thumbnail,
        product.stopTime,
        true
    )