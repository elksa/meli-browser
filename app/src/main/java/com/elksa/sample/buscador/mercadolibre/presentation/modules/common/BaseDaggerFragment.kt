package com.elksa.sample.buscador.mercadolibre.presentation.modules.common

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.elksa.sample.buscador.mercadolibre.presentation.modules.common.CustomDialogFragment.Companion.TAG
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationToDirectionEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.OnBackPressedEvent
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

    protected fun showDialog(dialogModel: DialogInfoUiModel) {
        CustomDialogFragment.Builder()
            .setIcon(dialogModel.icon)
            .setTitle(dialogModel.title)
            .setMessage(dialogModel.message)
            .setPositiveButton(dialogModel.positiveButtonText, dialogModel.onPositiveClickListener)
            .setNegativeButton(dialogModel.negativeButtonText)
            .create()
            .show(childFragmentManager, TAG)
    }
}