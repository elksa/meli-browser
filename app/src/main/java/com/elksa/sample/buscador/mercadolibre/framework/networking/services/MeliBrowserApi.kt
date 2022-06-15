package com.elksa.sample.buscador.mercadolibre.framework.networking.services

import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ItemDescriptionDto
import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ProductDetailsDto
import com.elksa.sample.buscador.mercadolibre.framework.networking.model.ProductsSearchResultDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MeliBrowserApi {

    @GET("sites/{idSite}/search")
    fun searchProducts(
        @Path("idSite") idSite: String,
        @Query("q") query: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Single<ProductsSearchResultDto>

    @GET("items/{idProduct}")
    fun getProductDetails(
        @Path("idProduct") idItem: String,
    ): Single<ProductDetailsDto>

    @GET("items/{idItem}/description")
    fun getItemDescription(
        @Path("idItem") idItem: String,
    ): Single<ItemDescriptionDto>
}