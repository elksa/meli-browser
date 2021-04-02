package com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation

import androidx.navigation.NavDirections

sealed class NavigationEvent

data class NavigationToDirectionEvent(
    val navDirections: NavDirections
) : NavigationEvent()