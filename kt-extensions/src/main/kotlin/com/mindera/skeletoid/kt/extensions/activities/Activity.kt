@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.activities

import android.app.Activity
import android.os.Build
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View

fun Activity.setStatusBarTextWhite() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
}

fun Activity.makeStatusBarFullyTransparent() {
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                           View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                                           View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
}

fun Activity.setStatusBarColor(@ColorRes colorResId: Int) {
    window.statusBarColor = ContextCompat.getColor(this, colorResId)
}