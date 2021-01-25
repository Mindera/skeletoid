package com.mindera.skeletoid.kt.extensions.utils

import java.text.NumberFormat
import java.util.Locale

/**
 * Parses an amount of any currency from a string to a float
 *
 * @param locale the locale of the currency
 * @param convertCents a boolean argument to tell if a conversion is needed to the highest value of the currency
 * (for example - 50p becomes £0.50)
 */
fun String?.parseCurrency(locale: Locale = Locale.UK, convertCents: Boolean = false): Float? {
    return this?.let { amount ->
        val value = runCatching { NumberFormat.getInstance(locale).parse(amount)?.toFloat() }.getOrNull()
            ?: throw NumberFormatException()
        if (convertCents) value / 100f else value
    }
}


