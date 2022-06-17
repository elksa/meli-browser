package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.utils.RandomGenerator
import javax.inject.Inject

class FetchProductRecommendationsUseCase @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val randomGenerator: RandomGenerator
) {

    private var lastUsedIndex: Int? = null

    /**
     * This is an arbitrary mocked list representing the top queries users search, in order to
     * provide a meaningful starting point for the application.
     */
    private val topSearches = listOf(
        "celular",
        "playstation",
        "xbox",
        "monitor",
        "nintendo",
        "laptop",
        "nvdia"
    )

    private fun getRandomIndex() = randomGenerator.generateRandomInt(0, topSearches.size)

    /**
     * Generates a random index from zero (inclusive) to the top searches list size (exclusive). It
     * grants the same index is not generated more than once in a row, if that's the case, the next
     * valid index will be used.
     * @return a randomly generated index.
     */
    private fun getNewIndex(): Int {
        return if (lastUsedIndex == null) {
            getRandomIndex()
        } else {
            var newIndex = getRandomIndex()
            if(newIndex == lastUsedIndex) {
                newIndex = if (newIndex >= topSearches.size - 1) 0 else newIndex + 1
            }
            lastUsedIndex = newIndex
            newIndex
        }
    }

    /**
     * Fetches product recommendations
     * @param offset the lower limit of the result block.
     * @param limit the amount of results to be fetched or page size.
     * @return products list searched with a randomly generated query picked from the top searches.
     */
    fun fetchProductRecommendations(offset: Int, limit: Int) =
        searchProductsUseCase.searchProducts(topSearches[getNewIndex()], offset, limit)
}