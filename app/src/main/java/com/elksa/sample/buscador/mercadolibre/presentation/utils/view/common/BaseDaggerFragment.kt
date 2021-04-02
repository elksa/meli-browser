package com.elksa.sample.buscador.mercadolibre.presentation.utils.view.common

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationToDirectionEvent
import dagger.android.support.DaggerFragment

abstract class BaseDaggerFragment : DaggerFragment() {

    protected fun <T : BaseViewModel> observerViewModelEvents(viewModel: T) {
        viewModel.navigationEvent.observe(viewLifecycleOwner, Observer(::navigateFragment))
    }

    protected fun navigateFragment(navigation: NavigationEvent) {
        when (navigation) {
            is NavigationToDirectionEvent -> findNavController().navigate(navigation.navDirections)
        }
    }
}