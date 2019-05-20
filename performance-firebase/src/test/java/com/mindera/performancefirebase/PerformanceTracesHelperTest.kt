package com.mindera.performancefirebase

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.mindera.skeletoid.utils.extensions.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.mockStatic
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import kotlin.test.assertEquals
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
        assertEquals(actualTrace, helper.runningTraces[tag])
        assertEquals(expectedTrace, actualTrace)
        verify(expectedTrace).start()
    }
}