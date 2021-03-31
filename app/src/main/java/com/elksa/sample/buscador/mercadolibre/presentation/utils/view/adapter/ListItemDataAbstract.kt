package com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter

class ListItemDataAbstract<T>(
    override val data: T,
    override val type: Int = 0
) : ListItemData<T>