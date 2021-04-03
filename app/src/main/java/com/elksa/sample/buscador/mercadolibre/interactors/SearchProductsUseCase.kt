package com.elksa.sample.buscador.mercadolibre.interactors

import com.elksa.sample.buscador.mercadolibre.domain.ProductsSearchResultEntity
import com.elksa.sample.buscador.mercadolibre.framework.networking.MeliBrowserApi
import com.elksa.sample.buscador.mercadolibre.framework.networking.utils.SITE_ID_CO
import io.reactivex.Single
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val meliBrowserApi: MeliBrowserApi
) {

    fun searchProducts(query: String, idSite: String = SITE_ID_CO): Single<ProductsSearchResultEntity> {
        return meliBrowserApi.searchProducts(idSite, query).map { it.mapToDomain() }
    }
}