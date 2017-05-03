package com.mindera.skeletoid.threads.utils;


public class ThreadUtils {

    /**
     * Gets the current thread name
     *
     * @return The current thread name in a printable string
     */
    public static String getCurrentThreadName() {
        return "[T# " + Thread.currentThread().getName() + "] ";
    }
}
