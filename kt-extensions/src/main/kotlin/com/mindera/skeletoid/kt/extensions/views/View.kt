@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.views

import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup

fun View.visible() {
    visibility = VISIBLE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun View.gone() {
    visibility = GONE
}

fun View.isVisible(): Boolean {
    return visibility == VISIBLE
}

fun View.isInvisible(): Boolean {
    return visibility == INVISIBLE
}

fun View.isGone(): Boolean {
    return visibility == GONE
}

fun View.disableChildren(enabled: Boolean) {
    this.isEnabled = enabled
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            this.getChildAt(i).disableChildren(enabled)
        }
    }
}


