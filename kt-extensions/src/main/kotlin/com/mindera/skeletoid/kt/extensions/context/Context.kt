@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.context

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.IntegerRes
import android.support.v4.content.ContextCompat

inline fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

inline fun Context.getInteger(@IntegerRes idRes: Int): Int {
    return resources.getInteger(idRes)
}

inline fun Context.getStringArray(idRes: Int): Array<String> {
    return resources.getStringArray(idRes)
}