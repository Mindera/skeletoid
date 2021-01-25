package com.mindera.skeletoid.kt.extensions.utils

import java.util.Calendar

fun Calendar.addDays(days: Int) {
    add(Calendar.DAY_OF_YEAR, days)
}