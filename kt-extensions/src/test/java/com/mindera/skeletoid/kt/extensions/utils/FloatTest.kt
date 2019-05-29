package com.mindera.skeletoid.kt.extensions.utils

import org.junit.Test
import java.text.NumberFormat
import java.util.Locale
import kotlin.test.assertEquals

class FloatTest {

    @Test
    fun testFormatNumberWithFormatter() {
        val float = 14.3F
        val numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH)
        val expectedFloat = "14.3"
        val actualFloat = float.formatNumber(numberFormat)

        assertEquals(expectedFloat, actualFloat)
    }

    @Test
    fun testFormatNumberWithPercentFormatter() {
        val float = 14.3F
        val numberFormat = NumberFormat.getPercentInstance(Locale.ENGLISH)
        val expectedFloat = "1,430%"
        val actualFloat = float.formatNumber(numberFormat)

        assertEquals(expectedFloat, actualFloat)
    }

    @Test
    fun testFormatNumberWithCurrencyFormatterFraction() {
        val float = 14.3F
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        val expectedFloat = "$14.30"
        val actualFloat = float.formatNumber(numberFormat)

        assertEquals(expectedFloat, actualFloat)
    }

    @Test
    fun testFormatNumberWithCurrencyFormatter() {
        val float = 14F
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        val expectedFloat = "$14.00"
        val actualFloat = float.formatNumber(numberFormat)

        assertEquals(expectedFloat, actualFloat)
    }

    @Test
    fun testFormatNumberNull() {
        val float: Float? = null
        val actualFloat = float.formatNumber()

        assertEquals("", actualFloat)
    }
}
