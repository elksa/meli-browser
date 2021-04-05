package com.elksa.sample.buscador.mercadolibre.interactors

import com.elksa.sample.buscador.mercadolibre.domain.ProductsSearchResultEntity
import com.elksa.sample.buscador.mercadolibre.framework.networking.MeliBrowserApi
import com.elksa.sample.buscador.mercadolibre.framework.networking.utils.SITE_ID_CO
import io.reactivex.Single
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val meliBrowserApi: MeliBrowserApi
) {

    /**
     * Performs product search by query and pagination data.
     * @param query the text query
     * @param offset the lower limit for the result block.
     * @param limit the page size.
     * @return product search result entity.
     */
    fun searchProducts(
        query: String,
        offset: Int,
        limit: Int
    ): Single<ProductsSearchResultEntity> {
        return meliBrowserApi.searchProducts(SITE_ID_CO, query, offset, limit).map {
            it.mapToDomain()
        }
    }
}