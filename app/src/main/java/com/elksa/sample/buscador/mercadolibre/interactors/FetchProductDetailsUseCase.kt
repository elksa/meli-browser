package com.elksa.sample.buscador.mercadolibre.interactors

import com.elksa.sample.buscador.mercadolibre.domain.ProductDetailsEntity
import com.elksa.sample.buscador.mercadolibre.framework.networking.MeliBrowserApi
import io.reactivex.Single
import javax.inject.Inject

class FetchProductDetailsUseCase @Inject constructor(
    private val meliBrowserApi: MeliBrowserApi,
    private val fetchItemDescriptionUseCase: FetchItemDescriptionUseCase
) {

    /**
     * Fetches the details of a given product it. Concatenates two calls, the first one fetches the
     * details of the product and the second one fetches the its description, any failed call will
     * result in the whole method failing.
     * @param idProduct the id of the product.
     * @return The product details entity.
     */
    fun fetchProductDetails(idProduct: String): Single<ProductDetailsEntity> {
        return meliBrowserApi.getProductDetails(idProduct).flatMap {
            fetchItemDescriptionUseCase.fetchItemDescription(it.id).map { description ->
                it.mapToDomain().apply { this.description = description }
            }
        }
    }
}