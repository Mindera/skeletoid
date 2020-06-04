package com.mindera.skeletoid.threads.utils

import androidx.annotation.VisibleForTesting

object ThreadUtils {

    /**
     * Gets the current thread name
     *
     * @return The current thread name in a printable string
     */
    @JvmStatic
    val currentThreadName: String
        get() = "[T# " + Thread.currentThread().name + "] "

}