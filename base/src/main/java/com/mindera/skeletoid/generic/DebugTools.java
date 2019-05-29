package com.mindera.skeletoid.generic;

import android.support.annotation.VisibleForTesting;

import com.mindera.skeletoid.logs.LOG;

import java.util.Map;

/**
 * Class to provide debug only methods and utilities that should NOT be used in production
 */
public class DebugTools {

    @VisibleForTesting
    DebugTools() {
        throw new UnsupportedOperationException();
    }

    public static void printAllStackTraces(Class<?> clazz) {

        LOG.d(clazz.toString(), "DUMPING ALL STACK TRACES");

        Map<Thread, StackTraceElement[]> liveThreads = Thread.getAllStackTraces();
        for (Map.Entry<Thread, StackTraceElement[]> entry : liveThreads.entrySet()) {
            LOG.d(clazz.toString(), "Thread " + entry.getKey().getName());
            StackTraceElement[] traceElements = entry.getValue();
            for (StackTraceElement traceElement : traceElements) {
                LOG.d(clazz.toString(), "at " + traceElement);
            }
        }
    }
}
