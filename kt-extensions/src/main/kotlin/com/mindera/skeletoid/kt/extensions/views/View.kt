@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.views

import android.view.View
import android.view.View.*
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

var CLICK_THROTTLE = 2000L

inline fun View.visible() {
    visibility = VISIBLE
}

inline fun View.invisible() {
    visibility = INVISIBLE
}

inline fun View.gone() {
    visibility = GONE
}

inline fun View.isVisible(): Boolean {
    return visibility == VISIBLE
}

inline fun View.isInvisible(): Boolean {
    return visibility == INVISIBLE
}

inline fun View.isGone(): Boolean {
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

fun View.clicksThrottle(): Observable<Unit> =
        this.clicks().throttleFirst(CLICK_THROTTLE, TimeUnit.MILLISECONDS)


