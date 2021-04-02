package com.elksa.sample.buscador.mercadolibre.interactors

import com.elksa.sample.buscador.mercadolibre.domain.CategoryEntity
import com.elksa.sample.buscador.mercadolibre.framework.networking.MeliBrowserApi
import io.reactivex.Single
import javax.inject.Inject

class FetchCategoryUseCase @Inject constructor(
    private val meliBrowserApi: MeliBrowserApi
) {

    fun loadCategory(idCategory: String): Single<CategoryEntity> {
        return meliBrowserApi.getCategory(idCategory).map { it.mapToDomain() }
    }
}