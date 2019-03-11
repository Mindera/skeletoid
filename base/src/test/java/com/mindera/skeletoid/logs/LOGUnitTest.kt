package com.mindera.skeletoid.logs

import android.content.Context
import com.mindera.skeletoid.logs.appenders.ILogAppender
import com.mindera.skeletoid.logs.utils.LogAppenderUtils
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash
import com.mindera.skeletoid.threads.utils.ThreadUtils
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.ArrayList

class LOGUnitTest {

    companion object {

        /**
         * Should be the same as [LoggerManager.LOG_FORMAT_4ARGS]
         */
        private val LOG_FORMAT_4ARGS = "%s %s %s | %s"
    }

    private val TAG = "TAG"
    private val TEXT = "Text"
    private val mPackageName = "my.package.name"

    @After
    fun cleanupLOG() {
        val context = mock(Context::class.java)
        LOG.deinit(context)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInitContextAndPackageNameNullFail() {
        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(null, null, appenders)

    }

    @Test(expected = IllegalArgumentException::class)
    fun testInitContextNullFail() {
        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(null, appenders)

    }

    @Test
    fun testInit() {
        val context = mock(Context::class.java)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName)
        LOG.addAppenders(context, appenders)

        verify(appenderA, times(1)).enableAppender(context)
        verify(appenderB, times(1)).enableAppender(context)
        verify(appenderC, times(1)).enableAppender(context)
        assertTrue(LOG.isInitialized())
    }

    @Test
    fun testInitWithAppenders() {
        val context = mock(Context::class.java)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        verify(appenderA, times(1)).enableAppender(context)
        verify(appenderB, times(1)).enableAppender(context)
        verify(appenderC, times(1)).enableAppender(context)
    }


    @Test
    fun testDebugLog() {
        val context = mock(Context::class.java)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        LOG.d(TAG, TEXT)

        //This is ugly.. but I don't see another way.
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

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        LOG.e(TAG, TEXT)

        //This is ugly.. but I don't see another way.
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

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        LOG.w(TAG, TEXT)

        //This is ugly.. but I don't see another way.
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

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        LOG.wtf(TAG, TEXT)
        //This is ugly.. but I don't see another way.
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

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        LOG.i(TAG, TEXT)
        //This is ugly.. but I don't see another way.
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
    fun testDebugWithExceptionLog() {
        val context = mock(Context::class.java)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        val e = Exception()
        LOG.d(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, e, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, e, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, e, log)
    }

    @Test
    fun testErrorWithExceptionLog() {
        val context = mock(Context::class.java)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        val e = Exception()
        LOG.e(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.ERROR, e, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.ERROR, e, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.ERROR, e, log)
    }

    @Test
    fun testWarnWithExceptionLog() {
        val context = mock(Context::class.java)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        val e = Exception()
        LOG.w(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.WARN, e, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.WARN, e, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.WARN, e, log)
    }

    @Test
    fun testFatalWithExceptionLog() {
        val context = mock(Context::class.java)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        val e = Exception()
        LOG.wtf(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.FATAL, e, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.FATAL, e, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.FATAL, e, log)
    }

    @Test
    fun testInfoWithExceptionLog() {
        val context = mock(Context::class.java)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        val e = Exception()
        LOG.i(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.INFO, e, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.INFO, e, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.INFO, e, log)
    }


    @Test
    fun testVerboseWithExceptionLog() {
        val context = mock(Context::class.java)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        val e = Exception()
        LOG.v(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            LogAppenderUtils.getLogString(TEXT)
        )

        verify(appenderA, times(1)).log(LOG.PRIORITY.VERBOSE, e, log)
        verify(appenderB, times(1)).log(LOG.PRIORITY.VERBOSE, e, log)
        verify(appenderC, times(1)).log(LOG.PRIORITY.VERBOSE, e, log)
    }

    @Test
    fun testVerboseLog() {
        val context = mock(Context::class.java)

        val appenders = ArrayList<ILogAppender>()

        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")

        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        LOG.init(context, mPackageName, appenders)

        LOG.v(TAG, TEXT)
        //This is ugly.. but I don't see another way.
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

    private fun mockAppender(loggerId: String): ILogAppender {
        val appender = mock(ILogAppender::class.java)

        `when`(appender.loggerId).thenReturn(loggerId)

        return appender
    }
}
