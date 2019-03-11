package com.mindera.skeletoid.threads.utils

object ThreadUtils {

    /**
     * Gets the current thread name
     *
     * @return The current thread name in a printable string
     */
    fun getCurrentThreadName() : String{
        return "[T# " + Thread.currentThread().name + "] "
    }
}
