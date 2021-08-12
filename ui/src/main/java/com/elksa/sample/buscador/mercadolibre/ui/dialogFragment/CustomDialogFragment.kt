package com.elksa.sample.buscador.mercadolibre.ui.dialogFragment

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.elksa.sample.buscador.mercadolibre.ui.dialogFragment.CustomDialogFragment.Builder.Companion.ARG_ICON
import com.elksa.sample.buscador.mercadolibre.ui.dialogFragment.CustomDialogFragment.Builder.Companion.ARG_NEGATIVE_BUTTON
import com.elksa.sample.buscador.mercadolibre.ui.dialogFragment.CustomDialogFragment.Builder.Companion.ARG_POSITIVE_BUTTON
import com.elksa.sample.buscador.mercadolibre.ui.dialogFragment.CustomDialogFragment.Builder.Companion.ARG_SUBTITLE
import com.elksa.sample.buscador.mercadolibre.ui.dialogFragment.CustomDialogFragment.Builder.Companion.ARG_TITLE

class CustomDialogFragment : DialogFragment() {

    class Builder {

        companion object {
            const val ARG_ICON = "ARG_ICON"
            const val ARG_TITLE = "ARG_TITLE"
            const val ARG_SUBTITLE = "ARG_SUBTITLE"
            const val ARG_POSITIVE_BUTTON = "ARG_POSITIVE_BUTTON"
            const val ARG_NEGATIVE_BUTTON = "ARG_NEGATIVE_BUTTON"
        }

        private var onPositiveClickListener: (() -> Unit)? = null
        private var onNegativeClickListener: (() -> Unit)? = null
        private val arguments = Bundle()

        fun setIcon(@DrawableRes drawableId: Int) =
            apply { arguments.putInt(ARG_ICON, drawableId) }

        fun setTitle(@StringRes titleId: Int) = apply {
            arguments.putInt(ARG_TITLE, titleId)
        }

        fun setMessage(@StringRes messageId: Int) = apply {
            arguments.putInt(ARG_SUBTITLE, messageId)
        }

        fun setPositiveButton(@StringRes positiveButtonText: Int = 0, listener: (() -> Unit)? = null) =
            apply {
                arguments.putInt(ARG_POSITIVE_BUTTON, positiveButtonText)
                onPositiveClickListener = listener
            }

        fun setNegativeButton(
            @StringRes negativeButtonText: Int = 0,
            listener: (() -> Unit)? = null
        ) = apply {
            arguments.putInt(ARG_NEGATIVE_BUTTON, negativeButtonText)
            onNegativeClickListener = listener
        }

        fun create(): CustomDialogFragment {
            val dialog = CustomDialogFragment()
            dialog.arguments = arguments
            dialog.dialogPositiveClickListener = onPositiveClickListener
            dialog.dialogNegativeClickListener = onNegativeClickListener
            return dialog
        }
    }

    companion object {
        const val TAG = "MeliDialogFragment"
    }

    private var viewModel: CustomDialogViewModel? = null
    private var dialogPositiveClickListener: (() -> Unit)? = null
    private var dialogNegativeClickListener: (() -> Unit)? = null
    private var icon: Int = 0
    private var title: Int = 0
    private var message: Int = 0
    private var positiveButtonText = 0
    private var negativeButtonText = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomDialogViewModel::class.java)
        loadParams()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val textPositive = if (positiveButtonText != 0) positiveButtonText else android.R.string.ok
        val builder = AlertDialog.Builder(requireContext())
            .setIcon(icon)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(textPositive)) { _, _ ->
                viewModel?.onPositiveClickListener?.invoke()
            }
        if (negativeButtonText != 0 || viewModel?.onNegativeClickListener != null) {
            val textNegative =
                if (negativeButtonText != 0) negativeButtonText else android.R.string.cancel
            builder.setNegativeButton(textNegative) { _, _ ->
                viewModel?.onNegativeClickListener?.invoke()
            }
        }

        return builder.create()
    }


    private fun loadParams()  {
        arguments?.run {
            icon = getInt(ARG_ICON)
            title = getInt(ARG_TITLE)
            message = getInt(ARG_SUBTITLE)
            positiveButtonText = getInt(ARG_POSITIVE_BUTTON)
            negativeButtonText = getInt(ARG_NEGATIVE_BUTTON)
        }
        dialogPositiveClickListener?.let { viewModel?.onPositiveClickListener = it }
        dialogNegativeClickListener?.let { viewModel?.onNegativeClickListener = it }
    }
}