package com.mindera.skeletoid.generic

import android.content.Context
import androidx.annotation.VisibleForTesting

object UIUtils {

    /**
     * Returns the status bar height in pixels.
     *
     * @param context       App context
     * @return status bar height or 0
     */
    @JvmStatic
    fun getStatusBarHeighPixels(context: Context): Int {
        val resourceId = context.resources
            .getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources
            .getDimensionPixelSize(resourceId) else 0
    }
}