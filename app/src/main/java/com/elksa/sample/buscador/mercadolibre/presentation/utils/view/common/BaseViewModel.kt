package com.elksa.sample.buscador.mercadolibre.presentation.utils.view.common

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.SingleLiveEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationEvent

abstract class BaseViewModel : ViewModel() {

    protected var _loaderVisibility = MutableLiveData(View.GONE)
    val loaderVisibility: LiveData<Int> get() = _loaderVisibility

    protected val _navigationEvent = SingleLiveEvent<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> get() = _navigationEvent
}