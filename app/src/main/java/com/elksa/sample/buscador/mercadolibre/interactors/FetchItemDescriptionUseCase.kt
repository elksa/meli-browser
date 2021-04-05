package com.elksa.sample.buscador.mercadolibre.interactors

import com.elksa.sample.buscador.mercadolibre.domain.utils.ItemDescriptionEntity
import com.elksa.sample.buscador.mercadolibre.framework.networking.MeliBrowserApi
import io.reactivex.Single
import javax.inject.Inject

class FetchItemDescriptionUseCase @Inject constructor(
    private val meliBrowserApi: MeliBrowserApi
) {

    /**
     * Fetches the description of a given item by its id.
     * @param idItem the id of the item.
     * @return The item description entity.
     */
    fun fetchItemDescription(idItem: String): Single<ItemDescriptionEntity> {
        return meliBrowserApi.getItemDescription(idItem).map { it.mapToDomain() }
    }
}