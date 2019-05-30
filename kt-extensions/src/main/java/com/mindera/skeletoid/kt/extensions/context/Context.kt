@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.context

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.IntegerRes
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.getInteger(@IntegerRes idRes: Int): Int {
    return resources.getInteger(idRes)
}

fun Context.getStringArray(idRes: Int): Array<String> {
    return resources.getStringArray(idRes)
}