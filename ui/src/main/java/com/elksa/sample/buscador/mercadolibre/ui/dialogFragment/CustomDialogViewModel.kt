package com.elksa.sample.buscador.mercadolibre.ui.dialogFragment

import androidx.lifecycle.ViewModel

class CustomDialogViewModel : ViewModel() {

    var onPositiveClickListener: (() -> Unit)? = null
    var onNegativeClickListener: (() -> Unit)? = null
}