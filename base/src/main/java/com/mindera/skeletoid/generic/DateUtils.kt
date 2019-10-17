package com.mindera.skeletoid.generic

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object DateUtils {

    private const val DATE_FORMAT = "yyyy-MM-dd"
    private const val TIME_ZONE = "GMT"

    /**
     * Check if a timestamp if from the same day or after
     *
     * @param afterDate    The date to check if is after the other
     * @param originalDate The original date
     * @return true if the afterDate is after the originalDate
     */
    fun isSameDayOrAfter(afterDate: Long, originalDate: Long): Boolean {

        val afterDateCal = Calendar.getInstance()
        afterDateCal.timeInMillis = afterDate
        val originalDateCal = Calendar.getInstance()
        originalDateCal.timeInMillis = originalDate

        if (afterDateCal.get(Calendar.YEAR) > originalDateCal.get(Calendar.YEAR)) {
            return true
        } else if (afterDateCal.get(Calendar.YEAR) == originalDateCal.get(Calendar.YEAR)) {
            return afterDateCal.get(Calendar.DAY_OF_YEAR) >= originalDateCal.get(Calendar.DAY_OF_YEAR)
        }
        return false
    }

    /**
     * Formats a date to a string in a pattern. Using the GMT time zone
     * @param date Date to format to a string
     * @param formatPattern Format pattern
     * @return Date in a String formatted
     */
    fun formatDate(date: Date, formatPattern: String = DATE_FORMAT): String {
        return SimpleDateFormat(formatPattern, Locale.getDefault()).apply { this.timeZone = TimeZone.getTimeZone(TIME_ZONE) }.format(date)
    }

    /**
     * Parses a string in a format to a date. Using the GMT time zone
     * @param dateString String to parse
     * @param formatPattern Format pattern
     * @return Date
     */
    fun parseDate(dateString: String, formatPattern: String = DATE_FORMAT): Date? {
        return SimpleDateFormat(formatPattern, Locale.getDefault()).apply { this.timeZone = TimeZone.getTimeZone(TIME_ZONE) }.parse(dateString)
    }

    /**
     * Calculates the number of days between two dates
     * @param firstDate
     * @param secondDate
     * @return Days difference
     */
    fun daysBetween(firstDate: Date, secondDate: Date): Long {
        return TimeUnit.MILLISECONDS.toDays((firstDate.time - secondDate.time))
    }
}
