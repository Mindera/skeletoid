package com.mindera.performancefirebase.rxoperators

import com.google.firebase.perf.FirebasePerformance
import com.mindera.performancefirebase.FirebasePerformanceTracker
import com.mindera.skeletoid.performance.rxoperators.PerformanceObservableOperator

class FirebasePerformanceObservableOperator<T>(tag: String) :
    PerformanceObservableOperator<T>(
        FirebasePerformanceTracker(
            tag,
            FirebasePerformance.getInstance()
        )
    )
