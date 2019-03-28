package com.mindera.skeletoid.kt.extensions.utils

import java.text.NumberFormat

fun Float?.formatNumber(formatter: NumberFormat = NumberFormat.getInstance()): String {
    return this?.let { formatter.format(this) }.orEmpty()
}