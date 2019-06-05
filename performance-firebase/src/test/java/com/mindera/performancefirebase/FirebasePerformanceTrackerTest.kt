package com.mindera.performancefirebase

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.mindera.skeletoid.utils.extensions.mock
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class FirebasePerformanceTrackerTest {

    private lateinit var tracker: FirebasePerformanceTracker
    private val firebasePerformanceMock = mock<FirebasePerformance>()
    private val trace = mock<Trace>()
    private val tag = "test_method"

    @Before
    fun setUp() {
        `when`(firebasePerformanceMock.newTrace(tag)).thenReturn(trace)
        tracker =
            FirebasePerformanceTracker(tag, firebasePerformanceMock)
    }

    @Test
    fun shouldStartTrackingWithTag() {
        tracker.startTracking()

        verify(firebasePerformanceMock).newTrace(tag)
        verify(trace).start()
    }

    @Test
    fun shouldStartTrackingOnlyOnce() {
        tracker.startTracking()
        tracker.startTracking()

        verify(firebasePerformanceMock).newTrace(tag)
        verify(trace).start()
    }

    @Test
    fun shouldStopStartedTrackingWithTag() {
        tracker.startTracking()
        tracker.stopTracking()

        verify(trace).stop()
    }

    @Test
    fun shouldNotStopTrackingWithoutStarting() {
        tracker.stopTracking()

        verify(trace, times(0)).stop()
    }
}
