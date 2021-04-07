package com.elksa.sample.buscador.mercadolibre.presentation.modules.common

import androidx.lifecycle.ViewModel

class CustomDialogViewModel : ViewModel() {

    var onPositiveClickListener: (() -> Unit)? = null
    var onNegativeClickListener: (() -> Unit)? = null
}