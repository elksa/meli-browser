package com.elksa.sample.buscador.mercadolibre.presentation.modules.details

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max

private const val MIN_SCALE = 0.65f
private const val MIN_ALPHA = 0.3f
private const val ZERO_ALPHA = 0f

class ZoomOutTransformation : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.run {
            when {
                position < -1 -> {
                    alpha = ZERO_ALPHA
                }
                position <= 1 -> {
                    scaleX = max(MIN_SCALE, 1 - abs(position))
                    scaleY = max(MIN_SCALE, 1 - abs(position))
                    alpha = max(MIN_ALPHA, 1 - abs(position))
                }
                else -> {
                    alpha = ZERO_ALPHA
                }
            }
        }
    }
}