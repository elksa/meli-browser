package com.elksa.sample.buscador.mercadolibre.data

import com.elksa.sample.buscador.mercadolibre.data.model.ProductsSearchResultDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MeliBrowserApi {

    @GET("search")
    fun searchProducts(
        @Query("q") query: String
    ): Single<ProductsSearchResultDto>

}