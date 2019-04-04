package com.mindera.skeletoid.kt.extensions.views

import android.view.ViewGroup

fun ViewGroup.setMarginTop(marginTop: Int) {
    val params: ViewGroup.MarginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(params.leftMargin, marginTop, params.rightMargin, params.bottomMargin)
    layoutParams = params
}

fun ViewGroup.setMarginBottom(marginBottom: Int) {
    val params: ViewGroup.MarginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, marginBottom)

    layoutParams = params
}