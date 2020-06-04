package com.mindera.skeletoid.generic

import com.mindera.skeletoid.generic.DebugTools.printAllStackTraces
import com.mindera.skeletoid.logs.LOG
import com.mindera.skeletoid.logs.LOG.d
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.internal.verification.VerificationModeFactory
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(LOG::class)
class DebugToolsUnitTests {
    @Test
    fun testPrintAllStackTraces() {
        PowerMockito.mockStatic(LOG::class.java)
        printAllStackTraces(String::class.java)
        PowerMockito.verifyStatic(LOG::class.java, VerificationModeFactory.times(1))
        d(
            ArgumentMatchers.eq(String::class.java.toString()),
            ArgumentMatchers.eq("DUMPING ALL STACK TRACES")
        )
        PowerMockito.verifyStatic(LOG::class.java, VerificationModeFactory.atLeastOnce())
        d(
            ArgumentMatchers.eq(String::class.java.toString()),
            ArgumentMatchers.anyString()
        )
    }
}