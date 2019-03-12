package com.mindera.rxoperators

import com.google.firebase.perf.FirebasePerformance
import com.mindera.FirebasePerformanceTracker
import com.mindera.skeletoid.performance.rxoperators.PerformanceSingleOperator

class FirebasePerformanceSingleOperator<T>(tag: String) :
    PerformanceSingleOperator<T>(
        FirebasePerformanceTracker(
            tag,
            FirebasePerformance.getInstance()
        )
    )
