package com.elksa.sample.buscador.mercadolibre.presentation.modules.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.NavigationToDirectionEvent
import com.elksa.sample.buscador.mercadolibre.presentation.utils.view.navigation.OnBackPressedEvent
import com.elksa.sample.buscador.mercadolibre.ui.dialogFragment.CustomDialogFragment
import com.elksa.sample.buscador.mercadolibre.ui.dialogFragment.CustomDialogFragment.Companion.TAG
import com.elksa.sample.buscador.mercadolibre.ui.dialogFragment.DialogInfoUiModel
import dagger.android.support.DaggerFragment

abstract class BaseDaggerFragment : DaggerFragment() {

    protected abstract fun initComponents(inflater: LayoutInflater)

    protected abstract fun setupObservers()

    protected abstract fun setupActionBar()

    protected abstract fun getCurrentView(): View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initComponents(inflater)
        setupObservers()
        setupActionBar()

        return getCurrentView()
    }

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