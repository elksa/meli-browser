package com.elksa.sample.buscador.mercadolibre.presentation.utils

import android.os.SystemClock
import android.view.View
import android.view.View.OnClickListener

private const val MIN_CLICK_INTERVAL = 800L

class OnSingleClickListener(
    private val onSingleClick: (View) -> Unit
) : OnClickListener {

    private var lastClickTime: Long = 0L

    override fun onClick(view: View) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - lastClickTime
        if (elapsedTime > MIN_CLICK_INTERVAL) {
            lastClickTime = currentClickTime
            onSingleClick(view)
        }
    }
}