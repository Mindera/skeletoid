package com.mindera.skeletoid.apprating.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object DateUtils {

    private const val DATE_FORMAT = "yyyy-MM-dd"
    private const val TIME_ZONE = "GMT"

    fun formatDate(date: Date, formatPattern: String = DATE_FORMAT): String {
        return SimpleDateFormat(formatPattern, Locale.getDefault()).apply { this.timeZone = TimeZone.getTimeZone(TIME_ZONE) }.format(date)
    }

    fun parseDate(dateString: String, formatPattern: String = DATE_FORMAT): Date {
        return SimpleDateFormat(formatPattern, Locale.getDefault()).apply { this.timeZone = TimeZone.getTimeZone(TIME_ZONE) }.parse(dateString)
    }

    fun daysBetween(firstDate: Date, secondDate: Date): Long {
        return TimeUnit.MILLISECONDS.toDays((firstDate.time - secondDate.time))
    }
}
