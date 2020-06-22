package com.mindera.skeletoid.logs

import android.content.Context
import com.mindera.skeletoid.generic.AndroidUtils
import com.mindera.skeletoid.generic.AndroidUtils.getApplicationPackage
import com.mindera.skeletoid.logs.LOG.PRIORITY
import com.mindera.skeletoid.logs.appenders.ILogAppender
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getLogString
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash
import com.mindera.skeletoid.threads.utils.ThreadUtils
import com.mindera.skeletoid.utils.extensions.mock
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
@PrepareForTest(AndroidUtils::class)
class LoggerManagerUnitTest {
    companion object {
        /**
         * Should be the same as [LoggerManager.LOG_FORMAT_4ARGS]
         */
        private const val LOG_FORMAT_4ARGS = "%s %s %s | %s"

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

    @Test
    fun testCreateLoggerManagerWithPackageName() {
        val loggerManager = LoggerManager(packageName)
        Assert.assertNotNull(loggerManager)
    }

    @Test
    fun testCreateLoggerManagerWithContext() {
        PowerMockito.mockStatic(AndroidUtils::class.java)
        Mockito.`when`(getApplicationPackage(context)).thenReturn(packageName)
        val loggerManager = LoggerManager(context)
        Assert.assertNotNull(loggerManager)
    }

    @Test
    fun testAddAppendersEmpty() {
        val loggerManager = LoggerManager(packageName)
        val appendersIds = loggerManager.addAppenders(context, ArrayList())
        Assert.assertNotNull(appendersIds)
        Assert.assertEquals(0, appendersIds.size)
    }

    @Test
    fun testAddAppenders() {
        val loggerManager = LoggerManager(packageName)
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        val appendersIds = loggerManager.addAppenders(context, appenders)
        Mockito.verify(appenderA, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderB, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderC, Mockito.times(1)).enableAppender(context)
        Assert.assertNotNull(appendersIds)
        Assert.assertEquals(3, appendersIds.size)
        Assert.assertTrue(appendersIds.contains("A"))
        Assert.assertTrue(appendersIds.contains("B"))
        Assert.assertTrue(appendersIds.contains("C"))
    }

    @Test
    fun testAddAppendersRepeated() {
        val loggerManager = LoggerManager(packageName)
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB1 = mockAppender("B")
        val appenderB2 = mockAppender("B")
        appenders.add(appenderA)
        appenders.add(appenderB1)
        appenders.add(appenderB2)
        val appendersIds = loggerManager.addAppenders(context, appenders)
        Assert.assertNotNull(appendersIds)
        Assert.assertEquals(2, appendersIds.size)
        Assert.assertTrue(appendersIds.contains("A"))
        Assert.assertTrue(appendersIds.contains("B"))
    }

    @Test
    fun testDisableAppendersEmpty() {
        val loggerManager = LoggerManager(packageName)
        loggerManager.removeAppenders(context, HashSet())
        Assert.assertNotNull(loggerManager)
    }

    @Test
    fun testDisableAppenders() {
        val loggerManager = LoggerManager(packageName)
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        val appendersIds = loggerManager.addAppenders(context, appenders)
        loggerManager.removeAppenders(context, appendersIds)
        Mockito.verify(appenderA, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderB, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderC, Mockito.times(1)).disableAppender()
    }

    @Test
    fun testRemoveAppender() {
        val loggerManager = LoggerManager(packageName)
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appendersId: MutableList<String> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        appendersId.add("A")
        loggerManager.addAppenders(context, appenders)
        loggerManager.removeAppenders(context, HashSet(appendersId))
        Mockito.verify(appenderA, Mockito.times(1)).disableAppender()
    }

    @Test
    fun testRemoveAllAppenders() {
        val loggerManager = LoggerManager(packageName)
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.removeAllAppenders()
        Mockito.verify(appenderA, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderB, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderC, Mockito.times(1)).disableAppender()
    }

    @Test
    fun testDebugLogWithTag() {
        val loggerManager = LoggerManager(packageName)
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.DEBUG, null, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(PRIORITY.DEBUG, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(PRIORITY.DEBUG, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(PRIORITY.DEBUG, null, log)
    }

    @Test
    fun testErrorLogWithTag() {
        val loggerManager = LoggerManager(packageName)
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.ERROR, null, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(PRIORITY.ERROR, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(PRIORITY.ERROR, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(PRIORITY.ERROR, null, log)
    }

    @Test
    fun testWarnLogWithTag() {
        val loggerManager = LoggerManager(packageName)
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.WARN, null, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(PRIORITY.WARN, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(PRIORITY.WARN, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(PRIORITY.WARN, null, log)
    }

    @Test
    fun testFatalLogWithTag() {
        val loggerManager = LoggerManager(packageName)
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.FATAL, null, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(PRIORITY.FATAL, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(PRIORITY.FATAL, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(PRIORITY.FATAL, null, log)
    }

    @Test
    fun testInfoLogWithTag() {
        val loggerManager = LoggerManager(packageName)
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.INFO, null, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1)).log(PRIORITY.INFO, null, log)
        Mockito.verify(appenderB, Mockito.times(1)).log(PRIORITY.INFO, null, log)
        Mockito.verify(appenderC, Mockito.times(1)).log(PRIORITY.INFO, null, log)
    }

    @Test
    fun testVerboseLogWithTag() {
        val loggerManager = LoggerManager(packageName)
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.VERBOSE, null, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1))
            .log(PRIORITY.VERBOSE, null, log)
        Mockito.verify(appenderB, Mockito.times(1))
            .log(PRIORITY.VERBOSE, null, log)
        Mockito.verify(appenderC, Mockito.times(1))
            .log(PRIORITY.VERBOSE, null, log)
    }

    @Test
    fun testDebugLogWithTagAndThrowable() {
        val loggerManager = LoggerManager(packageName)
        val throwable = Throwable()
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.DEBUG, throwable, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1))
            .log(PRIORITY.DEBUG, throwable, log)
        Mockito.verify(appenderB, Mockito.times(1))
            .log(PRIORITY.DEBUG, throwable, log)
        Mockito.verify(appenderC, Mockito.times(1))
            .log(PRIORITY.DEBUG, throwable, log)
    }

    @Test
    fun testErrorLogWithTagAndThrowable() {
        val loggerManager = LoggerManager(packageName)
        val throwable = Throwable()
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.ERROR, throwable, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1))
            .log(PRIORITY.ERROR, throwable, log)
        Mockito.verify(appenderB, Mockito.times(1))
            .log(PRIORITY.ERROR, throwable, log)
        Mockito.verify(appenderC, Mockito.times(1))
            .log(PRIORITY.ERROR, throwable, log)
    }

    @Test
    fun testWarnLog() {
        val loggerManager = LoggerManager(packageName)
        val throwable = Throwable()
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.WARN, throwable, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1))
            .log(PRIORITY.WARN, throwable, log)
        Mockito.verify(appenderB, Mockito.times(1))
            .log(PRIORITY.WARN, throwable, log)
        Mockito.verify(appenderC, Mockito.times(1))
            .log(PRIORITY.WARN, throwable, log)
    }

    @Test
    fun testFatalLogWithTagAndThrowable() {
        val loggerManager = LoggerManager(packageName)
        val throwable = Throwable()
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.FATAL, throwable, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1))
            .log(PRIORITY.FATAL, throwable, log)
        Mockito.verify(appenderB, Mockito.times(1))
            .log(PRIORITY.FATAL, throwable, log)
        Mockito.verify(appenderC, Mockito.times(1))
            .log(PRIORITY.FATAL, throwable, log)
    }

    @Test
    fun testInfoLogWithTagAndThrowable() {
        val loggerManager = LoggerManager(packageName)
        val throwable = Throwable()
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.INFO, throwable, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1))
            .log(PRIORITY.INFO, throwable, log)
        Mockito.verify(appenderB, Mockito.times(1))
            .log(PRIORITY.INFO, throwable, log)
        Mockito.verify(appenderC, Mockito.times(1))
            .log(PRIORITY.INFO, throwable, log)
    }

    @Test
    fun testVerboseLogWithTagAndThrowable() {
        val loggerManager = LoggerManager(packageName)
        val throwable = Throwable()
        val appenders: MutableList<ILogAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.VERBOSE, throwable, TEXT)
        val log = String.format(
            LOG_FORMAT_4ARGS,
            TAG,
            getObjectHash(TAG),
            ThreadUtils.currentThreadName,
            getLogString(TEXT)
        )
        Mockito.verify(appenderA, Mockito.times(1))
            .log(PRIORITY.VERBOSE, throwable, log)
        Mockito.verify(appenderB, Mockito.times(1))
            .log(PRIORITY.VERBOSE, throwable, log)
        Mockito.verify(appenderC, Mockito.times(1))
            .log(PRIORITY.VERBOSE, throwable, log)
    }

    @Test
    fun testNoLogForEmptyAppendersWithTag() {
        val appenders: List<ILogAppender> = ArrayList()
        val spyAppenders = Mockito.spy(appenders)
        val loggerManager = LoggerManager(packageName)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.VERBOSE, null, TEXT)
        Mockito.verify(spyAppenders, Mockito.times(0)).listIterator()
    }

    @Test
    fun testNoLogForEmptyAppendersWithTagAndThrowable() {
        val appenders: List<ILogAppender> = ArrayList()
        val spyAppenders = Mockito.spy(appenders)
        val loggerManager = LoggerManager(packageName)
        loggerManager.addAppenders(context, appenders)
        loggerManager.log(TAG, PRIORITY.VERBOSE, Throwable(), TEXT)
        Mockito.verify(spyAppenders, Mockito.times(0)).listIterator()
    }

    private fun mockAppender(analyticsId: String): ILogAppender {
        val appender :ILogAppender = mock()
        Mockito.`when`(appender.loggerId).thenReturn(analyticsId)
        return appender
    }

    @Test
    fun testSetMethodNameVisible() {
        val loggerManager = LoggerManager(packageName)
        loggerManager.setMethodNameVisible(true)
        Assert.assertTrue(loggerManager.addMethodName)
    }
}