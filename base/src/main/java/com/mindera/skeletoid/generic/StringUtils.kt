package com.mindera.skeletoid.generic

import androidx.annotation.VisibleForTesting

object StringUtils {
    /**
     * Get the nth index of a substring on a string
     *
     * @param str    The string
     * @param substr the substring
     * @param index  nth ocurrence
     * @return The index
     */
    @JvmStatic
    fun ordinalIndexOf(str: String?, substr: String?, index: Int): Int {
        if (str == null || substr == null || index <= 0) {
            return -1
        }
        if (str == substr && index > 1) {
            return -1
        }
        var pos = str.indexOf(substr)
        var position = index
        while (--position > 0 && pos != -1) {
            pos = str.indexOf(substr, pos + 1)
        }
        return pos
    }
}