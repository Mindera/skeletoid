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
import com.mindera.skeletoid.logs.appenders.interfaces.ILogAppender
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getLogString
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash
import com.mindera.skeletoid.threads.utils.ThreadUtils
import com.mindera.skeletoid.utils.extensions.mock
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.ArrayList
import java.util.HashSet

@RunWith(PowerMockRunner::class)
@PrepareForTest(LoggerManager::class, AndroidUtils::class)
class LOGUnitTest {

    companion object {
        private const val LOG_FORMAT_4ARGS = LoggerManager.LOG_FORMAT_4ARGS
        private const val TAG = "TAG"
        private const val TEXT = "Text"
        private const val packageName = "my.package.name"
    }

    @Mock
    private lateinit var context: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun cleanupLOG() {
        deinit()
    }

    @Test
    fun testIsNotInitialisedBuDefault() {
        Assert.assertFalse(isInitialized)
    }

    @Test
    @Throws(Exception::class)
    fun testInitWithContext() {
        val appenders: MutableList<ILogAppender> = ArrayList()
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
        init(context, packageName)
        Assert.assertTrue(isInitialized)
    }

    @Test
    fun testInitWithContextAndAppenders() {
        val appenders: MutableList<ILogAppender> = ArrayList()
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
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName, appenders)
        Mockito.verify(appenderA, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderB, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderC, Mockito.times(1)).enableAppender(context)
        Assert.assertTrue(isInitialized)
    }

    @Test
    fun testDebugLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName, appenders)
        d(TAG, TEXT)

        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, null, log)
    }

    @Test
    fun testErrorLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName, appenders)
        e(TAG, TEXT)

        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.ERROR, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.ERROR, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.ERROR, null, log)
    }

    @Test
    fun testWarnLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName, appenders)
        w(TAG, TEXT)

        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.WARN, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.WARN, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.WARN, null, log)
    }

    @Test
    fun testFatalLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
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
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        init(context, packageName, appenders)
        wtf(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.FATAL, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.FATAL, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.FATAL, null, log)
    }

    @Test
    fun testInfoLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
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
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        init(context, packageName, appenders)
        i(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.INFO, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.INFO, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.INFO, null, log)
    }

    @Test
    fun testNoDebugLogDeinitialised() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        appenders.add(appenderA)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        init(context, packageName, appenders)
        deinit()
        d(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(0)).log(LOG.PRIORITY.DEBUG, null, log)
    }

    @Test
    fun testNoErrorLogDeinitialised() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        appenders.add(appenderA)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        init(context, packageName, appenders)
        deinit()
        e(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(0)).log(LOG.PRIORITY.ERROR, null, log)
    }

    @Test
    fun testNoWarnLogDeinitialised() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        appenders.add(appenderA)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        init(context, packageName, appenders)
        deinit()
        w(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(0)).log(LOG.PRIORITY.WARN, null, log)
    }

    @Test
    fun testNoFatalLogDeinitialised() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        appenders.add(appenderA)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        init(context, packageName, appenders)
        deinit()
        wtf(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(0)).log(LOG.PRIORITY.FATAL, null, log)
    }

    @Test
    fun testNoInfoLogDeinitialised() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        appenders.add(appenderA)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        init(context, packageName, appenders)
        deinit()
        i(TAG, TEXT)
        Mockito.verify(appenderA, Mockito.times(0)).log(LOG.PRIORITY.INFO, null, log)
    }

    @Test
    fun testDebugWithExceptionLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName, appenders)
        val e = Exception()
        d(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.DEBUG, e, log)
    }

    @Test
    fun testErrorWithExceptionLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName, appenders)
        val e = Exception()
        e(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.ERROR, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.ERROR, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.ERROR, e, log)
    }

    @Test
    fun testWarnWithExceptionLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName, appenders)
        val e = Exception()
        w(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.WARN, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.WARN, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.WARN, e, log)
    }

    @Test
    fun testFatalWithExceptionLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName, appenders)
        val e = Exception()
        wtf(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.FATAL, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.FATAL, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.FATAL, e, log)
    }

    @Test
    fun testInfoWithExceptionLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName, appenders)
        val e = Exception()
        i(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.INFO, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.INFO, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.INFO, e, log)
    }

    @Test
    fun testVerboseWithExceptionLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName, appenders)
        val e = Exception()
        v(TAG, e, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(LOG.PRIORITY.VERBOSE, e, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(LOG.PRIORITY.VERBOSE, e, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(LOG.PRIORITY.VERBOSE, e, log)
    }

    @Test
    fun testVerboseLog() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName, appenders)
        v(TAG, TEXT)
        //This is ugly.. but I don't see another way.
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
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
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName)
        val ids = addAppenders(context, appenders)
        removeAppenders(context, ids)
        Mockito.verify(appenderA).disableAppender()
        Mockito.verify(appenderB).disableAppender()
        Mockito.verify(appenderC).disableAppender()
    }

    @Test
    fun testRemoveSomeAppenders() {
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context, packageName)
        val ids = addAppenders(context, appenders)
        val idsList: MutableList<String> =
            ArrayList(ids)
        for (id in ids) {
            if (id == "A") {
                idsList.remove(id)
            }
        }
        val idsToRemove: Set<String> = HashSet(idsList)
        removeAppenders(context, idsToRemove)
        Mockito.verify(appenderA, Mockito.times(0)).disableAppender()
        Mockito.verify(appenderB).disableAppender()
        Mockito.verify(appenderC).disableAppender()
    }

    private fun mockAppender(loggerId: String): ILogAppender {
        val appender: ILogAppender = mock()
        Mockito.`when`(appender.loggerId).thenReturn(loggerId)
        return appender
    }
}