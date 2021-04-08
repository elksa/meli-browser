package com.elksa.sample.buscador.mercadolibre.presentation.modules.products

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.elksa.sample.buscador.mercadolibre.databinding.ListItemProductBinding
import com.elksa.sample.buscador.mercadolibre.presentation.utils.setSingleClickListener
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.adapter.ListItemView

private const val DURATION_RIPPLE_EFFECT = 120L

class ProductItemView(
    override val context: Context,
    onSelected: ((ProductUiModel) -> Unit)
) : ListItemView<ProductUiModel> {

    private val binding = ListItemProductBinding.inflate(LayoutInflater.from(context))

    override val view = binding.root

    override lateinit var data: ProductUiModel

    init {
        binding.root.apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            setSingleClickListener {
                Handler(Looper.getMainLooper()).postDelayed(
                    { onSelected(data) },
                    DURATION_RIPPLE_EFFECT
                )
            }
        }
    }

    override fun bind(item: ProductUiModel) {
        data = item
        binding.uiModel = item
        binding.executePendingBindings()
    }
}