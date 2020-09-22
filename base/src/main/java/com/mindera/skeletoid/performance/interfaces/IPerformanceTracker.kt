package com.mindera.skeletoid.performance.interfaces

interface IPerformanceTracker {

    val tag: String

    fun startTracking()

    fun stopTracking()
}
