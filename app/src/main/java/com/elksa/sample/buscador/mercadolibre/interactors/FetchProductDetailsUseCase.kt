package com.elksa.sample.buscador.mercadolibre.interactors

import com.elksa.sample.buscador.mercadolibre.domain.ProductDetailsEntity
import com.elksa.sample.buscador.mercadolibre.framework.networking.MeliBrowserApi
import io.reactivex.Single
import javax.inject.Inject

class FetchProductDetailsUseCase @Inject constructor(
    private val meliBrowserApi: MeliBrowserApi,
    private val fetchItemDescriptionUseCase: FetchItemDescriptionUseCase
) {

    fun fetchProductDetails(idProduct: String): Single<ProductDetailsEntity> {
        return meliBrowserApi.getProductDetails(idProduct).flatMap {
            fetchItemDescriptionUseCase.fetchItemDescription(it.id).map { description ->
                it.mapToDomain().apply { this.description = description }
            }
        }
    }
}