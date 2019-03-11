package com.mindera.skeletoid.logs

import android.content.Context
import com.mindera.skeletoid.logs.appenders.ILogAppender
import com.mindera.skeletoid.logs.utils.LogAppenderUtils
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash
import com.mindera.skeletoid.threads.utils.ThreadUtils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.ArrayList
import java.util.HashSet

class LoggerManagerUnitTest {

    companion object {

        /**
         * Should be the same as [LoggerManager.LOG_FORMAT_4ARGS]
         */
        private val LOG_FORMAT_4ARGS = "%s %s %s | %s"
    }

    private val TAG = "TAG"
    private val TEXT = "Text"
    private val mPackageName = "my.package.name"

    @Test
    fun testAddAppendersNull() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)
        val appendersIds = loggerManager.addAppenders(context, null)

        assertNotNull(appendersIds)
        assertEquals(0, appendersIds.size)
    }

    @Test
    fun testAddAppendersEmpty() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)
        val appendersIds = loggerManager.addAppenders(context, ArrayList())

        assertNotNull(appendersIds)
        assertEquals(0, appendersIds.size)
    }

    @Test
    fun testAddAppenders() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        val appendersIds = loggerManager.addAppenders(context, appenders)

        verify(appenderA, times(1)).enableAppender(context)
        verify(appenderB, times(1)).enableAppender(context)
        verify(appenderC, times(1)).enableAppender(context)

        assertNotNull(appendersIds)
        assertEquals(3, appendersIds.size)
        assertTrue(appendersIds.contains("A"))
        assertTrue(appendersIds.contains("B"))
        assertTrue(appendersIds.contains("C"))
    }

    @Test
    fun testAddAppendersRepeated() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB1 = mockAppender("B")
        val appenderB2 = mockAppender("B")

        appenders.add(appenderA)
        appenders.add(appenderB1)
        appenders.add(appenderB2)

        val appendersIds = loggerManager.addAppenders(context, appenders)

        assertNotNull(appendersIds)
        assertEquals(2, appendersIds.size)
        assertTrue(appendersIds.contains("A"))
        assertTrue(appendersIds.contains("B"))
    }

    @Test
    fun testDisableAppendersNull() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)
        loggerManager.removeAppenders(context, null)
    }

    @Test
    fun testDisableAppendersEmpty() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)
        loggerManager.removeAppenders(context, HashSet())
    }

    @Test
    fun testDisableAppenders() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        val appendersIds = loggerManager.addAppenders(context, appenders)

        loggerManager.removeAppenders(context, appendersIds)
        verify(appenderA, times(1)).disableAppender()
        verify(appenderB, times(1)).disableAppender()
        verify(appenderC, times(1)).disableAppender()
    }

    @Test
    fun testRemoveAppender() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)

        val appenders = ArrayList<ILogAppender>()
        val appendersId = ArrayList<String>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        appendersId.add("A")

        loggerManager.addAppenders(context, appenders)
        loggerManager.removeAppenders(context, HashSet(appendersId))
        verify(appenderA, times(1)).disableAppender()
    }

    @Test
    fun testRemoveAllAppenders() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        loggerManager.addAppenders(context, appenders)

        loggerManager.removeAllAppenders()
        verify(appenderA, times(1)).disableAppender()
        verify(appenderB, times(1)).disableAppender()
        verify(appenderC, times(1)).disableAppender()
    }

    @Test
    fun testDebugLog() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        loggerManager.addAppenders(context, appenders)

        loggerManager.log(TAG, LOG.PRIORITY.DEBUG, TEXT)

        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, null, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, null, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, null, log)
    }

    @Test
    fun testErrorLog() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        loggerManager.addAppenders(context, appenders)

        loggerManager.log(TAG, LOG.PRIORITY.ERROR, TEXT)

        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.ERROR, null, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.ERROR, null, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.ERROR, null, log)
    }

    @Test
    fun testWarnLog() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)
        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        loggerManager.addAppenders(context, appenders)

        loggerManager.log(TAG, LOG.PRIORITY.WARN, TEXT)

        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.WARN, null, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.WARN, null, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.WARN, null, log)
    }

    @Test
    fun testFatalLog() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)
        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        loggerManager.addAppenders(context, appenders)

        loggerManager.log(TAG, LOG.PRIORITY.FATAL, TEXT)

        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.FATAL, null, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.FATAL, null, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.FATAL, null, log)
    }

    @Test
    fun testInfoLog() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)
        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        loggerManager.addAppenders(context, appenders)

        loggerManager.log(TAG, LOG.PRIORITY.INFO, TEXT)

        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.INFO, null, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.INFO, null, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.INFO, null, log)
    }


    @Test
    fun testVerboseLog() {
        val context = mock(Context::class.java)

        val loggerManager = LoggerManager(mPackageName)
        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        loggerManager.addAppenders(context, appenders)

        loggerManager.log(TAG, LOG.PRIORITY.VERBOSE, TEXT)

        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.VERBOSE, null, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.VERBOSE, null, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.VERBOSE, null, log)
    }

    private fun mockAppender(analyticsId: String): ILogAppender {
        val appender = mock(ILogAppender::class.java)

        `when`(appender.loggerId).thenReturn(analyticsId)

        return appender
    }


}
