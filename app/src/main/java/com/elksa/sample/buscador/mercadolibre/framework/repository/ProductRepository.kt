package com.elksa.sample.buscador.mercadolibre.framework.repository

import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductDetailsEntity
import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductEntity
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.IProductRepository
import com.elksa.sample.buscador.mercadolibre.framework.networking.services.MeliBrowserApi
import io.reactivex.Single
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api: MeliBrowserApi) : IProductRepository {

    override fun searchProducts(
        idSite: String,
        query: String,
        offset: Int,
        limit: Int
    ): Single<List<ProductEntity>> {
        return api.searchProducts(idSite, query, offset, limit).map {
            it.results.map { product -> product.mapToDomain() }
        }
    }

    override fun getProductDetails(idProduct: String): Single<ProductDetailsEntity> {
        return api.getProductDetails(idProduct).map { item -> item.mapToDomain() }
    }
}