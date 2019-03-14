package com.mindera.skeletoid.performance

interface PerformanceTracker {

    val tag: String

    fun startTracking()

    fun stopTracking()
}
