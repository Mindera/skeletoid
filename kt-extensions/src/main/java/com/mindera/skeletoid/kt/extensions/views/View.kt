@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.views

import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.ViewTreeObserver

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

fun View.setPaddingTop(paddingTop: Int) {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

fun View.setPaddingBottom(paddingBottom: Int) {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

inline fun <T: View> T.afterDrawn(crossinline nextFunction: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                nextFunction()
            }
        }
    })
}


