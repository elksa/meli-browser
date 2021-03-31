package com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

typealias AdapterFactory = (parent: ViewGroup, viewType: Int) -> ListItemView<*>

class CustomListAdapter(
    private val factory: AdapterFactory
) : ListAdapter<ListItemData<*>, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val item = factory.invoke(parent, viewType) as ListItemView<Any>
        return ItemViewHolder(item)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = holder as ItemViewHolder
        item.itemModel.bind(getItem(position).data!!)
    }

    override fun getItemViewType(position: Int): Int = currentList[position].type

    private class ItemViewHolder(val itemModel: ListItemView<Any>) :
        RecyclerView.ViewHolder(itemModel.view)
}

class ItemDiffCallback : DiffUtil.ItemCallback<ListItemData<*>>() {
    override fun areItemsTheSame(oldItem: ListItemData<*>, newItem: ListItemData<*>) =
        oldItem.data.hashCode() == newItem.data.hashCode()

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ListItemData<*>, newItem: ListItemData<*>) =
        oldItem.data === newItem.data
}