@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.views

import android.graphics.drawable.Drawable
import android.widget.TextView

fun TextView.getLeftCompoundDrawable(): Drawable? {
    val DRAWABLE_LEFT = 0
    return compoundDrawables[DRAWABLE_LEFT]
}

fun TextView.getRightCompoundDrawable(): Drawable? {
    val DRAWABLE_RIGHT = 2
    return compoundDrawables[DRAWABLE_RIGHT]
}

fun TextView.clearCompoundDrawables() {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
}

fun TextView.setLeftCompoundDrawable(id: Int) {
    setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}

fun TextView.setRightCompoundDrawable(id: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0)
}