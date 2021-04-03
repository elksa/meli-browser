package com.elksa.sample.buscador.mercadolibre.presentation.utils.formatters

import android.content.Context
import com.elksa.sample.buscador.mercadolibre.R
import java.text.NumberFormat
import java.util.Locale.US
import javax.inject.Inject

class MoneyFormatter @Inject constructor(
    val context: Context
) : IMoneyFormatter {

    override fun format(value: Number) =
        context.resources.getString(
            R.string.format_money,
            NumberFormat.getIntegerInstance(US).format(value)
        )
}