package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductEntity
import com.elksa.sample.buscador.mercadolibre.framework.networking.services.MeliBrowserApi
import com.elksa.sample.buscador.mercadolibre.framework.networking.utils.SITE_ID_CO
import io.reactivex.Single
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val meliBrowserApi: MeliBrowserApi
) {

    /**
     * Performs product search by query and pagination data.
     * @param query the text query
     * @param offset the lower limit of the result block.
     * @param limit the amount of results to be fetched or page size.
     * @return list of products matching the search query.
     */
    fun searchProducts(
        query: String,
        offset: Int,
        limit: Int
    ): Single<List<ProductEntity>> {
        return meliBrowserApi.searchProducts(SITE_ID_CO, query, offset, limit).map {
            it.results.map { product -> product.mapToDomain() }
        }
    }
}