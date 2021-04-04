package com.elksa.sample.buscador.mercadolibre.presentation.utils.view.common

import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationToDirectionEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.OnBackPressedEvent
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment

abstract class BaseDaggerFragment : DaggerFragment() {

    protected fun <T : BaseViewModel> observerViewModelEvents(viewModel: T) {
        viewModel.navigationEvent.observe(viewLifecycleOwner, Observer(::navigateFragment))
    }

    private fun navigateFragment(navigation: NavigationEvent) {
        when (navigation) {
            is NavigationToDirectionEvent -> findNavController().navigate(navigation.navDirections)
            is OnBackPressedEvent -> requireActivity().onBackPressed()
        }
    }

    protected fun showError(view: View, @StringRes message: Int) {
        Snackbar.make(view, getString(message), Snackbar.LENGTH_LONG)
            .setAction(null, null).show()
    }
}