package com.mindera.skeletoid.generic;

import com.mindera.skeletoid.logs.Logger;

import java.util.Map;

/**
 * Class to provide debug only methods and utilities that should NOT be used in production
 */
public class DebugTools {

    public static void printAllStackTraces(Class<?> clazz) {

        Logger.d(clazz.toString(), "DUMPING ALL STACK TRACES");

        Map<Thread, StackTraceElement[]> liveThreads = Thread.getAllStackTraces();
        for (Thread thread : liveThreads.keySet()) {
            Logger.d(clazz.toString(), "Thread " + thread.getName());
            StackTraceElement[] traceElements = liveThreads.get(thread);
            for (StackTraceElement traceElement : traceElements) {
                Logger.d(clazz.toString(), "at " + traceElement);
            }
        }
    }
}
