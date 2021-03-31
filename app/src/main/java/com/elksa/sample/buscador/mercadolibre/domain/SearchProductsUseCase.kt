package com.elksa.sample.buscador.mercadolibre.domain

import com.elksa.sample.buscador.mercadolibre.data.MeliBrowserApi
import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductsSearchResultEntity
import io.reactivex.Single
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val meliBrowserApi: MeliBrowserApi
) {

    fun searchProducts(query: String): Single<ProductsSearchResultEntity> {
        return meliBrowserApi.searchProducts(query).map { it.mapToDomain() }
    }
}