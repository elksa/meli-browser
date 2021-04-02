package com.elksa.sample.buscador.mercadolibre.presentation.utils.view.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.SingleLiveEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationEvent

abstract class BaseViewModel : ViewModel() {

    protected val _navigationEvent = SingleLiveEvent<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> get() = _navigationEvent
}