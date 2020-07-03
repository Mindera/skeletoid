package com.mindera.skeletoid.generic

import com.mindera.skeletoid.logs.LOG

/**
 * Class to provide debug only methods and utilities that should NOT be used in production
 */
object DebugTools {
    @JvmStatic
    fun printAllStackTraces(clazz: Class<*>) {
        LOG.d(clazz.toString(), "DUMPING ALL STACK TRACES")
        val liveThreads = Thread.getAllStackTraces()
        for ((key, traceElements) in liveThreads) {
            LOG.d(clazz.toString(), "Thread " + key.name)
            for (traceElement in traceElements) {
                LOG.d(clazz.toString(), "at $traceElement")
            }
        }
    }
}