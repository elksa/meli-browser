package com.elksa.sample.buscador.mercadolibre.presentation.modules.common

data class DialogInfoUiModel(
    val icon: Int,
    val title: Int,
    val message: Int,
    val positiveButtonText: Int = 0,
    val onPositiveClickListener: (() -> Unit)? = null,
    val negativeButtonText: Int = 0,
    val onNegativeClickListener: (() -> Unit)? = null
)