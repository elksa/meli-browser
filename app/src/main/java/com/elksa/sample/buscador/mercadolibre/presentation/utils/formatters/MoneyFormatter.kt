package com.elksa.sample.buscador.mercadolibre.presentation.utils.formatters

import android.content.Context
import com.elksa.sample.buscador.mercadolibre.R
import java.text.NumberFormat
import java.util.Locale.US
import javax.inject.Inject

class MoneyFormatter @Inject constructor(
    val context: Context
) : IMoneyFormatter {

    /**
     * Formats a numeric price value to its corresponding string representation for Colombian
     * currency without decimals. For example, the numeric value "199900.0", would be "$ 199.900".
     */
    override fun format(value: Number) =
        context.resources.getString(
            R.string.format_money,
            NumberFormat.getIntegerInstance(US).format(value)
        )
}