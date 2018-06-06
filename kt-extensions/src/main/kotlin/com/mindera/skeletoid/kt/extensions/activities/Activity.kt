@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.activities

import android.app.Activity
import android.os.Build
import android.view.View

inline fun Activity.setStatusBarTextWhite() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
}