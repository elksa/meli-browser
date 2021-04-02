package com.elksa.sample.buscador.mercadolibre.framework.networking

import com.elksa.sample.buscador.mercadolibre.framework.networking.model.CategoryDto
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
        @Query("q") query: String
    ): Single<ProductsSearchResultDto>

    @GET("items/{idProduct}")
    fun getProductDetails(
        @Path("idProduct") idItem: String,
    ): Single<ProductDetailsDto>

    @GET("categories/{idCategory}")
    fun getCategory(
        @Path("idCategory") idCategory: String,
    ): Single<CategoryDto>
}