@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.utils

import java.util.*


fun Calendar.addDays(days: Int) {
    add(Calendar.DAY_OF_YEAR, days)
}