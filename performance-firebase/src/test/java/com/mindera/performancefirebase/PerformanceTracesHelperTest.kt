package com.mindera.performancefirebase

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.mindera.skeletoid.utils.extensions.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.mockStatic
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(PowerMockRunner::class)
@PrepareForTest(FirebasePerformance::class)
class PerformanceTracesHelperTest {

    @Test
    fun testStartTrace() {
        val tag = "hello Mindera!"
        val expectedTrace = mock<Trace>()
        mockStatic(FirebasePerformance::class.java)
        val firebasePerformance = mock<FirebasePerformance>()
        PowerMockito.`when`(firebasePerformance.newTrace(eq(tag))).thenReturn(expectedTrace)
        PowerMockito.`when`(FirebasePerformance.getInstance()).thenReturn(firebasePerformance)

        val helper = PerformanceTracesHelper()
        val actualTrace = helper.startTrace(tag)

        assertTrue(helper.runningTraces.containsKey(tag))
        assertNotNull(actualTrace)
        assertEquals(actualTrace, helper.runningTraces[tag])
        assertEquals(expectedTrace, actualTrace)
        verify(actualTrace).start()
    }

    @Test
    fun testStartSameTraceAgain() {
        val tag = "hello Mindera!"
        val expectedTrace = mock<Trace>()
        val anotherExpectedTrace = mock<Trace>()
        mockStatic(FirebasePerformance::class.java)
        val firebasePerformance = mock<FirebasePerformance>()
        PowerMockito.`when`(firebasePerformance.newTrace(eq(tag))).thenReturn(expectedTrace)
        PowerMockito.`when`(FirebasePerformance.getInstance()).thenReturn(firebasePerformance)

        val helper = PerformanceTracesHelper()
        val trace1 = helper.startTrace(tag)

        assertTrue(helper.runningTraces.containsKey(tag))
        assertNotNull(trace1)
        assertEquals(trace1, helper.runningTraces[tag])
        assertEquals(expectedTrace, trace1)
        verify(trace1).start()

        PowerMockito.`when`(firebasePerformance.newTrace(eq(tag))).thenReturn(anotherExpectedTrace)

        val trace2 = helper.startTrace(tag)
        assertTrue(helper.runningTraces.containsKey(tag))
        assertEquals(trace1, helper.runningTraces[tag])
        assertNull(trace2)
        verifyNoMoreInteractions(trace1)
    }

    @Test
    fun testNotStartEmptyTrace() {
        val tag = ""
        val expectedTrace = mock<Trace>()
        mockStatic(FirebasePerformance::class.java)
        val firebasePerformance = mock<FirebasePerformance>()
        PowerMockito.`when`(firebasePerformance.newTrace(eq(tag))).thenReturn(expectedTrace)
        PowerMockito.`when`(FirebasePerformance.getInstance()).thenReturn(firebasePerformance)

        val helper = PerformanceTracesHelper()
        val actualTrace = helper.startTrace(tag)

        assertFalse(helper.runningTraces.containsKey(tag))
        assertNull(helper.runningTraces[tag])
        assertNull(actualTrace)
    }

    @Test
    fun testStopUnstartedTrace() {
        val tag = ""

        val helper = PerformanceTracesHelper()
        helper.stopTrace(tag)

        assertFalse(helper.runningTraces.containsKey(tag))
    }

    @Test
    fun testStopTrace() {
        val tag = "hello Mindera!"
        val expectedTrace = mock<Trace>()
        mockStatic(FirebasePerformance::class.java)
        val firebasePerformance = mock<FirebasePerformance>()
        PowerMockito.`when`(firebasePerformance.newTrace(eq(tag))).thenReturn(expectedTrace)
        PowerMockito.`when`(FirebasePerformance.getInstance()).thenReturn(firebasePerformance)

        val helper = PerformanceTracesHelper()
        helper.startTrace(tag)
        helper.stopTrace(tag)

        assertFalse(helper.runningTraces.containsKey(tag))
    }

    @Test
    fun testStopTraceTwice() {
        val tag = "hello Mindera!"
        val expectedTrace = mock<Trace>()
        mockStatic(FirebasePerformance::class.java)
        val firebasePerformance = mock<FirebasePerformance>()
        PowerMockito.`when`(firebasePerformance.newTrace(eq(tag))).thenReturn(expectedTrace)
        PowerMockito.`when`(FirebasePerformance.getInstance()).thenReturn(firebasePerformance)

        val helper = PerformanceTracesHelper()
        val trace = helper.startTrace(tag)
        assertNotNull(trace)
        helper.stopTrace(tag)
        helper.stopTrace(tag)

        verify(trace).stop()
        assertFalse(helper.runningTraces.containsKey(tag))
    }

    @Test
    fun testClearTraces() {
        val tag1 = "hello Mindera!"
        val tag2 = "hi Mindera!"
        val expectedTrace1 = mock<Trace>()
        val expectedTrace2 = mock<Trace>()
        mockStatic(FirebasePerformance::class.java)
        val firebasePerformance = mock<FirebasePerformance>()
        PowerMockito.`when`(firebasePerformance.newTrace(eq(tag1))).thenReturn(expectedTrace1)
        PowerMockito.`when`(firebasePerformance.newTrace(eq(tag2))).thenReturn(expectedTrace2)
        PowerMockito.`when`(FirebasePerformance.getInstance()).thenReturn(firebasePerformance)

        val helper = PerformanceTracesHelper()
        val trace1 = helper.startTrace(tag1)
        val trace2 = helper.startTrace(tag2)

        assertNotNull(trace1)
        assertNotNull(trace2)
        assertEquals(2, helper.runningTraces.size)

        helper.clearTraces()

        assertTrue(helper.runningTraces.isEmpty())
        verify(trace1).stop()
        verify(trace2).stop()
    }
}
