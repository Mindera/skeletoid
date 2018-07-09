package com.mindera.skeletoid.threads.utils;

import android.support.annotation.VisibleForTesting;

public class ThreadUtils {

    @VisibleForTesting
    ThreadUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the current thread name
     *
     * @return The current thread name in a printable string
     */
    public static String getCurrentThreadName() {
        return "[T# " + Thread.currentThread().getName() + "] ";
    }
}
