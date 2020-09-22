package com.mindera.performancefirebase

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.mindera.skeletoid.performance.interfaces.IPerformanceTracker

class FirebasePerformanceTracker(override val tag: String, private val firebasePerformance: FirebasePerformance) :
    IPerformanceTracker {

    private lateinit var myTrace: Trace

    override fun startTracking() {
        if (!::myTrace.isInitialized) {
            myTrace = firebasePerformance.newTrace(tag)
            myTrace.start()
        }
    }

    override fun stopTracking() {
        if (::myTrace.isInitialized) {
            myTrace.stop()
        }
    }
}
