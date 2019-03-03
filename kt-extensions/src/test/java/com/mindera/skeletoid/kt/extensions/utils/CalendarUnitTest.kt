package com.mindera.skeletoid.kt.extensions.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class CalendarUnitTest {

    companion object {
        private const val JANUARY = 0
        private const val FEBRUARY = 1
        private const val DECEMBER = 11
    }

    private val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)

    @Test
    fun testAddDays_plus() {

        // having
        val calendar = Calendar.getInstance()
        calendar.set(currentYear, JANUARY, 1)

        // when
        calendar.addDays(10)
        val observed = calendar.get(Calendar.DAY_OF_MONTH)

        // then
        assertEquals(11, observed)
    }

    @Test
    fun testAddDays_minus() {

        // having
        val calendar = Calendar.getInstance()
        calendar.set(currentYear, JANUARY, 31)

        // when
        calendar.addDays(-30)
        val observed = calendar.get(Calendar.DAY_OF_MONTH)

        // then
        assertEquals(1, observed)
    }

    @Test
    fun testAddDays_nextMonth() {

        // having
        val calendar = Calendar.getInstance()
        calendar.set(currentYear, JANUARY, 31)

        // when
        calendar.addDays(1)
        val observedDay = calendar.get(Calendar.DAY_OF_MONTH)
        val observedMonth = calendar.get(Calendar.MONTH)

        // then
        assertEquals(1, observedDay)
        assertEquals(FEBRUARY, observedMonth)
    }

    @Test
    fun testAddDays_previousMonth() {

        // having
        val calendar = Calendar.getInstance()
        calendar.set(currentYear, FEBRUARY, 1)

        // when
        calendar.addDays(-1)
        val observedDay = calendar.get(Calendar.DAY_OF_MONTH)
        val observedMonth = calendar.get(Calendar.MONTH)

        // then
        assertEquals(31, observedDay)
        assertEquals(JANUARY, observedMonth)
    }

    @Test
    fun testAddDays_nextYear() {

        // having
        val calendar = Calendar.getInstance()
        calendar.set(currentYear, DECEMBER, 31)

        // when
        calendar.addDays(1)
        val observedDay = calendar.get(Calendar.DAY_OF_MONTH)
        val observedMonth = calendar.get(Calendar.MONTH)
        val observedYear = calendar.get(Calendar.YEAR)

        // then
        assertEquals(1, observedDay)
        assertEquals(JANUARY, observedMonth)
        assertEquals(currentYear + 1, observedYear)
    }

    @Test
    fun testAddDays_previousYear() {

        // having
        val calendar = Calendar.getInstance()
        calendar.set(currentYear, JANUARY, 1)

        // when
        calendar.addDays(-1)
        val observedDay = calendar.get(Calendar.DAY_OF_MONTH)
        val observedMonth = calendar.get(Calendar.MONTH)
        val observedYear = calendar.get(Calendar.YEAR)

        // then
        assertEquals(31, observedDay)
        assertEquals(DECEMBER, observedMonth)
        assertEquals(currentYear - 1, observedYear)
    }
}