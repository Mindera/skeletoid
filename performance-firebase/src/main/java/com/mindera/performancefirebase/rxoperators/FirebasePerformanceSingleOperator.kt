package com.mindera.performancefirebase.rxoperators

import com.google.firebase.perf.FirebasePerformance
import com.mindera.performancefirebase.FirebasePerformanceTracker
import com.mindera.skeletoid.performance.rxoperators.PerformanceSingleOperator

class FirebasePerformanceSingleOperator<T>(tag: String) :
    PerformanceSingleOperator<T>(
        FirebasePerformanceTracker(
            tag,
            FirebasePerformance.getInstance()
        )
    )
