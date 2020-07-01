package com.mindera.skeletoid.logs.appenders

import android.util.Log
import com.mindera.skeletoid.logs.LOG
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.internal.verification.VerificationModeFactory
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class)
class LogcatAppenderUnitTest {

    companion object{
        private val packageName  = "my.package.name"
    }

    @Test
    fun testConstructor() {
        val logcatAppender = LogcatAppender(packageName)

        assertEquals("LogcatAppender", logcatAppender.loggerId)
    }

    @Test
    fun testFormatLog() {
        val logcatAppender = LogcatAppender(packageName)

        val logs = arrayListOf<String>()

        assertEquals(logs, logcatAppender.formatLog(null))

        logs.add("abc")

        assertEquals(logs, logcatAppender.formatLog("abc"))
        logs.clear()

        logcatAppender.maxLineLength = 2
        logs.add("[Chunk 1 of 2] ab")
        logs.add("[Chunk 2 of 2] cd")
        assertEquals(logs, logcatAppender.formatLog("abcd"))
        logs.clear()

        logcatAppender.maxLineLength = 2
        logs.add("[Chunk 1 of 3] ab")
        logs.add("[Chunk 2 of 3] cd")
        logs.add("[Chunk 3 of 3] e")
        assertEquals(logs, logcatAppender.formatLog("abcde"))
    }

    @Test
    fun testSetMaxLineLength() {
        val logcatAppender = LogcatAppender(packageName)

        logcatAppender.maxLineLength = 1
        assertEquals(1, logcatAppender.maxLineLength)
    }

    @Test
    fun testSetMinLogLevel() {
        val logcatAppender = LogcatAppender(packageName)

        logcatAppender.minLogLevel = LOG.PRIORITY.DEBUG
        assertEquals(LOG.PRIORITY.DEBUG, logcatAppender.minLogLevel)
    }

    @Test
    fun testDefaultSplitLinesAboveMaxLength() {
        val logcatAppender = LogcatAppender(packageName)

        assertTrue(logcatAppender.isSplitLinesAboveMaxLength)
    }

    @Test
    fun testEnableSplitLinesAboveMaxLength() {
        val logcatAppender = LogcatAppender(packageName)

        logcatAppender.isSplitLinesAboveMaxLength = true
        assertTrue(logcatAppender.isSplitLinesAboveMaxLength)
    }

    @Test
    fun testDisableSplitLinesAboveMaxLength() {
        val logcatAppender = LogcatAppender(packageName)

        logcatAppender.isSplitLinesAboveMaxLength = false
        assertFalse(logcatAppender.isSplitLinesAboveMaxLength)
    }

    @Test
    fun testLoggingDebug() {
        val logcatAppender = LogcatAppender(packageName)
        PowerMockito.mockStatic(Log::class.java)

        logcatAppender.log(LOG.PRIORITY.DEBUG, null, "hello friend")

        PowerMockito.verifyStatic(Log::class.java, VerificationModeFactory.times(1))
        Log.d(packageName, "hello friend", null)
    }

    @Test
    fun testLoggingWarn() {
        val logcatAppender = LogcatAppender(packageName)
        PowerMockito.mockStatic(Log::class.java)

        logcatAppender.log(LOG.PRIORITY.WARN, null, "hello friend")

        PowerMockito.verifyStatic(Log::class.java, VerificationModeFactory.times(1))
        Log.w(packageName, "hello friend", null)
    }

    @Test
    fun testLoggingError() {
        val logcatAppender = LogcatAppender(packageName)
        PowerMockito.mockStatic(Log::class.java)

        logcatAppender.log(LOG.PRIORITY.ERROR, null, "hello friend")

        PowerMockito.verifyStatic(Log::class.java, VerificationModeFactory.times(1))
        Log.e(packageName, "hello friend", null)
    }

    @Test
    fun testLoggingVerbose() {
        val logcatAppender = LogcatAppender(packageName)
        PowerMockito.mockStatic(Log::class.java)

        logcatAppender.log(LOG.PRIORITY.VERBOSE, null, "hello friend")

        PowerMockito.verifyStatic(Log::class.java, VerificationModeFactory.times(1))
        Log.v(packageName, "hello friend", null)
    }

    @Test
    fun testLoggingInfo() {
        val logcatAppender = LogcatAppender(packageName)
        PowerMockito.mockStatic(Log::class.java)

        logcatAppender.log(LOG.PRIORITY.INFO, null, "hello friend")

        PowerMockito.verifyStatic(Log::class.java, VerificationModeFactory.times(1))
        Log.i(packageName, "hello friend", null)
    }

    @Test
    fun testLoggingFatal() {
        val logcatAppender = LogcatAppender(packageName)
        PowerMockito.mockStatic(Log::class.java)

        logcatAppender.log(LOG.PRIORITY.FATAL, null, "hello friend")

        PowerMockito.verifyStatic(Log::class.java, VerificationModeFactory.times(1))
        Log.wtf(packageName, "hello friend", null)
    }

    @Test
    fun testNotLoggingBelowMinLogLevel() {
        val logcatAppender = LogcatAppender(packageName)
        logcatAppender.minLogLevel = LOG.PRIORITY.ERROR
        PowerMockito.mockStatic(Log::class.java)

        logcatAppender.log(LOG.PRIORITY.DEBUG, null, "hello friend")

        PowerMockito.verifyStatic(Log::class.java, VerificationModeFactory.times(0))
        Log.d(packageName, "hello friend", null)
    }
}
