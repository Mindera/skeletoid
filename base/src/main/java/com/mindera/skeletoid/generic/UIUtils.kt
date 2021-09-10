package com.mindera.skeletoid.generic

import android.content.Context

object UIUtils {

    /**
     * Returns the status bar height in pixels.
     *
     * @param context       App context
     * @return status bar height or 0
     */
    @JvmStatic
    fun getStatusBarHeightPixels(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }

    /**
     * Given a value in dips, gets the amount of pixels that equates to in the given context.
     *
     * @param valueInDips - the amount of dips to convert to pixels
     * @param context - our current android context
     *
     * @return the value in pixels
     */
    fun getValueInPixels(valueInDips: Int, context: Context): Float {
        return (valueInDips * context.resources.displayMetrics.density)
    }
}