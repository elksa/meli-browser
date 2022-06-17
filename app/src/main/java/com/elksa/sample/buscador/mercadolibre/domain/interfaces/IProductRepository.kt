package com.elksa.sample.buscador.mercadolibre.domain.interfaces

import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductDetailsEntity
import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductEntity
import io.reactivex.Single

interface IProductRepository {

    fun searchProducts(
        idSite: String,
        query: String,
        offset: Int,
        limit: Int
    ): Single<List<ProductEntity>>

    fun getProductDetails(idProduct: String): Single<ProductDetailsEntity>
}

