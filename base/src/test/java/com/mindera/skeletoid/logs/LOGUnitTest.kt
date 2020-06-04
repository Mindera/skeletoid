package com.mindera.skeletoid.logs

import android.content.Context
import com.mindera.skeletoid.generic.AndroidUtils
import com.mindera.skeletoid.generic.AndroidUtils.getApplicationPackage
import com.mindera.skeletoid.logs.LOG.addAppenders
import com.mindera.skeletoid.logs.LOG.d
import com.mindera.skeletoid.logs.LOG.deinit
import com.mindera.skeletoid.logs.LOG.e
import com.mindera.skeletoid.logs.LOG.i
import com.mindera.skeletoid.logs.LOG.init
import com.mindera.skeletoid.logs.LOG.isInitialized
import com.mindera.skeletoid.logs.LOG.removeAppenders
import com.mindera.skeletoid.logs.LOG.v
import com.mindera.skeletoid.logs.LOG.w
import com.mindera.skeletoid.logs.LOG.wtf
import com.mindera.skeletoid.logs.appenders.ILogAppender
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getLogString
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash
import com.mindera.skeletoid.threads.utils.ThreadUtils
import junit.framework.Assert
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.ArrayList
import java.util.HashSet

@RunWith(PowerMockRunner::class)
@PrepareForTest(LoggerManager::class, AndroidUtils::class)
class LOGUnitTest {

    companion object {
        /**
         * Should be the same as [LoggerManager.LOG_FORMAT_4ARGS]
         */
        private const val LOG_FORMAT_4ARGS = "%s %s %s | %s"
    }

    private val TAG = "TAG"
    private val TEXT = "Text"
    private val mPackageName = "my.package.name"

    @After
    fun cleanupLOG() {
        val context = Mockito.mock(Context::class.java)
        deinit()
    }

    @Test
    fun testIsNotInitialisedBuDefault() {
        Assert.assertFalse(isInitialized)
    }

    @Test
    @Throws(Exception::class)
    fun testInitWithContext() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        PowerMockito.mockStatic(AndroidUtils::class.java)
        Mockito.`when`(getApplicationPackage(context))
            .thenReturn("package")
        init(context)
        addAppenders(context, appenders)
        Mockito.verify(appenderA, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderB, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderC, Mockito.times(1)).enableAppender(context)
        Assert.assertTrue(isInitialized)
    }

    @Test
    fun testInitWithContextAndPackageName() {
        val context =
            Mockito.mock(Context::class.java)
        init(context, mPackageName)
        Assert.assertTrue(isInitialized)
    }

    @Test
    fun testInitWithContextAndAppenders() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        PowerMockito.mockStatic(AndroidUtils::class.java)
        Mockito.`when`(getApplicationPackage(context))
            .thenReturn("package")
        init(context, logAppenders = appenders)
        Mockito.verify(appenderA, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderB, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderC, Mockito.times(1)).enableAppender(context)
        Assert.assertTrue(isInitialized)
    }

    @Test
    fun testInitWithContextAndPackageNameAndAppenders() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName, appenders)
        Mockito.verify(appenderA, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderB, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderC, Mockito.times(1)).enableAppender(context)
        Assert.assertTrue(isInitialized)
    }

    @Test
    fun testDebugLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName, appenders)
        d(TAG, TEXT)

        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, null, log)
    }

    @Test
    fun testErrorLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName, appenders)
        e(TAG, TEXT)

        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.ERROR, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.ERROR, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.ERROR, null, log)
    }

    @Test
    fun testWarnLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName, appenders)
        w(TAG, TEXT)

        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.WARN, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.WARN, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.WARN, null, log)
    }

    @Test
    fun testFatalLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        init(context, mPackageName, appenders)
        wtf(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.FATAL, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.FATAL, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.FATAL, null, log)
    }

    @Test
    fun testInfoLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        init(context, mPackageName, appenders)
        i(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.INFO, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.INFO, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.INFO, null, log)
    }

    @Test
    fun testNoDebugLogDeinitialised() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        appenders.add(appenderA)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        init(context, mPackageName, appenders)
        deinit()
        d(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(0)).log(LOG.PRIORITY.DEBUG, null, log)
    }

    @Test
    fun testNoErrorLogDeinitialised() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        appenders.add(appenderA)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        init(context, mPackageName, appenders)
        deinit()
        e(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(0)).log(LOG.PRIORITY.ERROR, null, log)
    }

    @Test
    fun testNoWarnLogDeinitialised() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        appenders.add(appenderA)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        init(context, mPackageName, appenders)
        deinit()
        w(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(0)).log(LOG.PRIORITY.WARN, null, log)
    }

    @Test
    fun testNoFatalLogDeinitialised() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        appenders.add(appenderA)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        init(context, mPackageName, appenders)
        deinit()
        wtf(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(0)).log(LOG.PRIORITY.FATAL, null, log)
    }

    @Test
    fun testNoInfoLogDeinitialised() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        appenders.add(appenderA)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        init(context, mPackageName, appenders)
        deinit()
        i(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(0)).log(LOG.PRIORITY.INFO, null, log)
    }

    @Test
    fun testDebugWithExceptionLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName, appenders)
        val e = Exception()
        d(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, e, log)
    }

    @Test
    fun testErrorWithExceptionLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName, appenders)
        val e = Exception()
        e(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.ERROR, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.ERROR, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.ERROR, e, log)
    }

    @Test
    fun testWarnWithExceptionLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName, appenders)
        val e = Exception()
        w(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.WARN, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.WARN, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.WARN, e, log)
    }

    @Test
    fun testFatalWithExceptionLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName, appenders)
        val e = Exception()
        wtf(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.FATAL, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.FATAL, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.FATAL, e, log)
    }

    @Test
    fun testInfoWithExceptionLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName, appenders)
        val e = Exception()
        i(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.INFO, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.INFO, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.INFO, e, log)
    }

    @Test
    fun testVerboseWithExceptionLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName, appenders)
        val e = Exception()
        v(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.VERBOSE, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.VERBOSE, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.VERBOSE, e, log)
    }

    @Test
    fun testVerboseLog() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName, appenders)
        v(TAG, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.getCurrentThreadName(),
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1))
            .log(LOG.PRIORITY.VERBOSE, null, log)
        Mockito.verify(appenderB, Mockito.times(1))
            .log(LOG.PRIORITY.VERBOSE, null, log)
        Mockito.verify(appenderC, Mockito.times(1))
            .log(LOG.PRIORITY.VERBOSE, null, log)
    }

    @Test
    fun testRemoveAllAppenders() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName)
        val ids = addAppenders(context, appenders)
        removeAppenders(context, ids)
        Mockito.verify(appenderA).disableAppender()
        Mockito.verify(appenderB).disableAppender()
        Mockito.verify(appenderC).disableAppender()
    }

    @Test
    fun testRemoveSomeAppenders() {
        val context =
            Mockito.mock(Context::class.java)
        val appenders: MutableList<ILogAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, mPackageName)
        val ids = addAppenders(context, appenders)
        val idsList: MutableList<String> =
            ArrayList(ids)
        for (id in ids) {
            if (id == "A") {
                idsList.remove(id)
            }
        }
        val idsToRemove: Set<String> =
            HashSet(idsList)
        removeAppenders(context, idsToRemove)
        Mockito.verify(appenderA, Mockito.times(0)).disableAppender()
        Mockito.verify(appenderB).disableAppender()
        Mockito.verify(appenderC).disableAppender()
    }

    private fun mockAppender(loggerId: String): ILogAppender {
        val appender = Mockito.mock(ILogAppender::class.java)
        Mockito.`when`(appender.loggerId).thenReturn(loggerId)
        return appender
    }
}