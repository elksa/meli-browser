package com.elksa.sample.buscador.mercadolibre.framework.networking.model

import com.google.gson.annotations.SerializedName

data class ProductsSearchResultDto(
    @SerializedName("site_id")
    val idSite: String,
    val results: List<ProductDto>
)