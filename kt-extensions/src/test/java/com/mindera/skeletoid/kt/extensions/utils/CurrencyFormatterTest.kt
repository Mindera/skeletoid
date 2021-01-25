package com.mindera.skeletoid.kt.extensions.utils

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CurrencyFormatterTest {

    @Test(expected = NumberFormatException::class)
    fun `test parseCurrency with rubbish`() {
        val observed = "aaa".parseCurrency()
    }

    @Test
    fun `test parseCurrency no args passed`() {
        val observed = "1£".parseCurrency()
        Assert.assertNotNull(observed)
        Assert.assertEquals(1f, observed)
    }

    @Test
    fun `test parseCurrency convert cents`() {
        val observed = "50p".parseCurrency(convertCents = true)
        Assert.assertNotNull(observed)
        Assert.assertEquals(0.5f, observed)
    }
}