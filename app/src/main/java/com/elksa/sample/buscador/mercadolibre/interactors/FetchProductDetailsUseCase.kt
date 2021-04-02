package com.elksa.sample.buscador.mercadolibre.interactors

import com.elksa.sample.buscador.mercadolibre.domain.ProductDetailsEntity
import com.elksa.sample.buscador.mercadolibre.framework.networking.MeliBrowserApi
import io.reactivex.Single
import javax.inject.Inject

class FetchProductDetailsUseCase @Inject constructor(
    private val meliBrowserApi: MeliBrowserApi,
    private val fetchCategoryUseCase: FetchCategoryUseCase
) {

    fun loadProductDetails(idProduct: String): Single<ProductDetailsEntity> {
        return meliBrowserApi.getProductDetails(idProduct).flatMap {
            fetchCategoryUseCase.loadCategory(it.idCategory).map { category ->
                it.mapToDomain().apply {
                    this.category = category
                }
            }
        }
    }
}