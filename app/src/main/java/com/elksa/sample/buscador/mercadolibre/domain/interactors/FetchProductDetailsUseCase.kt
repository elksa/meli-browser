package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.entities.ProductDetailsEntity
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.IProductRepository
import io.reactivex.Single
import javax.inject.Inject

class FetchProductDetailsUseCase @Inject constructor(
    private val productRepository: IProductRepository,
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
        return productRepository.getProductDetails(idProduct).flatMap {
            fetchItemDescriptionUseCase.fetchItemDescription(it.id).map { description ->
                it.apply { this.description = description }
            }
        }
    }
}