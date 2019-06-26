package com.mindera.skeletoid.generic

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DateUtilsUnitTest {

    @Test
    fun testSameDay() {
        val after = 1481429921000L
        val original = 1481429921000L

        assertTrue(DateUtils.isSameDayOrAfter(after, original))
    }

    @Test
    fun testAfterYear() {
        val after = 1481429921000L
        val original = 1381429921000L

        assertTrue(DateUtils.isSameDayOrAfter(after, original))
    }

    @Test
    fun testBeforeYear() {
        val after = 1381429921000L
        val original = 1481429921000L

        assertFalse(DateUtils.isSameDayOrAfter(after, original))
    }

    @Test
    fun testBeforeDay() {
        val after = 1480429921000L
        val original = 1481429921000L

        assertFalse(DateUtils.isSameDayOrAfter(after, original))
    }
}
