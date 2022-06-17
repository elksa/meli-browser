package com.elksa.sample.buscador.mercadolibre.domain.interactors

import com.elksa.sample.buscador.mercadolibre.domain.entities.ItemDescriptionEntity
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.IItemRepository
import io.reactivex.Single
import javax.inject.Inject

class FetchItemDescriptionUseCase @Inject constructor(
    private val itemRepository: IItemRepository
) {

    /**
     * Fetches the description of a given item by its id.
     * @param idItem the id of the item.
     * @return The item description entity.
     */
    fun fetchItemDescription(idItem: String): Single<ItemDescriptionEntity> {
        return itemRepository.getItemDescription(idItem)
    }
}