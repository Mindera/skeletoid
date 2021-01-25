package com.mindera.skeletoid.kt.extensions.utils

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.text.SimpleDateFormat
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CalendarTest {

    @Test
    fun `test calendar add days`() {
        val calendar = Calendar.getInstance()
        calendar.set(2020, 0, 25)
        calendar.addDays(2)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val observed = formatter.format(calendar.time)

        Assert.assertEquals("2020-01-27", observed)
    }

    @Test
    fun `test calendar add days at end of month `() {
        val calendar = Calendar.getInstance()
        calendar.set(2020, 0, 31)
        calendar.addDays(2)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val observed = formatter.format(calendar.time)

        Assert.assertEquals("2020-02-02", observed)
    }

    @Test
    fun `test calendar add days at end of year `() {
        val calendar = Calendar.getInstance()
        calendar.set(2020, 11, 31)
        calendar.addDays(2)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val observed = formatter.format(calendar.time)

        Assert.assertEquals("2021-01-02", observed)
    }
}