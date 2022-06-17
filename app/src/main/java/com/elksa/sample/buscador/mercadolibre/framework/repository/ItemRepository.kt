package com.elksa.sample.buscador.mercadolibre.framework.repository

import com.elksa.sample.buscador.mercadolibre.domain.entities.ItemDescriptionEntity
import com.elksa.sample.buscador.mercadolibre.domain.interfaces.IItemRepository
import com.elksa.sample.buscador.mercadolibre.framework.networking.services.MeliBrowserApi
import io.reactivex.Single
import javax.inject.Inject

class ItemRepository @Inject constructor(private val api: MeliBrowserApi) : IItemRepository {

    override fun getItemDescription(idItem: String): Single<ItemDescriptionEntity> {
        return api.getItemDescription(idItem).map { it.mapToDomain() }
    }
}