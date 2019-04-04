package com.mindera.performancefirebase

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace

class PerformanceTracesHelper {

    companion object {
        private const val LOG_TAG = "PerformanceTracesHelper"
    }

    private val runningTraces = HashMap<String, Trace>()

    fun startTrace(tag: String): Trace {
        val trace = FirebasePerformance.getInstance().newTrace(tag)
        runningTraces[tag] = trace

        trace.start()

        return trace
    }

    fun stopTrace(tag: String) {
        runningTraces.remove(tag)?.let { it.stop() }
    }

    fun clearTraces() {
        if (runningTraces.isNotEmpty()) {
            runningTraces.forEach { (_, value) -> value.stop() }
        }
        runningTraces.clear()
    }
}