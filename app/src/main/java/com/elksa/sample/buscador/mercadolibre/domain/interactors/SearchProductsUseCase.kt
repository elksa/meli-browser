package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductEntity
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.IProductRepository
import io.reactivex.Single
import javax.inject.Inject

private const val SITE_ID_CO = "MCO"

class SearchProductsUseCase @Inject constructor(
    private val productRepository: IProductRepository
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
        return productRepository.searchProducts(SITE_ID_CO, query, offset, limit)
    }
}