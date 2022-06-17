package com.elksa.sample.buscador.mercadolibre.domain.interfaces

import com.elksa.sample.buscador.mercadolibre.domain.entities.ItemDescriptionEntity
import io.reactivex.Single

interface IItemRepository {

    fun getItemDescription(idItem: String): Single<ItemDescriptionEntity>
}