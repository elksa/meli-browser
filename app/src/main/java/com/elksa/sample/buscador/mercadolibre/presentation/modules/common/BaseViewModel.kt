package com.elksa.sample.buscador.mercadolibre.presentation.modules.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.SingleLiveEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationEvent
import com.elksa.sample.buscador.mercadolibre.ui.dialogFragment.DialogInfoUiModel

abstract class BaseViewModel : ViewModel() {

    protected var _errorEvent = SingleLiveEvent<DialogInfoUiModel>()
    val errorEvent: LiveData<DialogInfoUiModel> get() = _errorEvent

    protected val _navigationEvent = SingleLiveEvent<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> get() = _navigationEvent
}