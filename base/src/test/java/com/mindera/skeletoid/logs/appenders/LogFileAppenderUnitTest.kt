package com.mindera.skeletoid.logs.appenders

import android.content.Intent
import androidx.core.content.FileProvider
import com.mindera.skeletoid.generic.AndroidUtils
import com.mindera.skeletoid.logs.LOG
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.io.File
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.LogRecord
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*")
@PrepareForTest(FileProvider::class, Intent::class, AndroidUtils::class)
public class LogFileAppenderUnitTest {

    companion object {
        private const val PACKAGE_NAME = "my.package.name"
        private const val FILE_NAME = "FILENAME"
    }

    @Rule
    @JvmField
    public var rule = PowerMockRule()

    @Test(expected = IllegalArgumentException::class)
    fun testConstructorFileNameInvalid() {
        LogFileAppender(PACKAGE_NAME, "*")
    }

    @Test
    fun testFileNameIsValidEmpty() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertFalse(appender.isFilenameValid(""))
    }

    @Test
    fun testFileNameIsValidInvalid() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertFalse(appender.isFilenameValid("//"))
    }

    @Test
    fun testFileNameIsValid() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertTrue(appender.isFilenameValid(FILE_NAME))
    }

    @Test
    fun testConstructor() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertEquals("LogFileAppender", appender.loggerId)
    }

    @Test
    fun testEnableAppender() {
        val context = RuntimeEnvironment.application
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        appender.enableAppender(context)
        Thread.sleep(2000)

        assertTrue(appender.canWriteToFile())
    }

    @Test
    fun testDisableAppender() {
        val context = RuntimeEnvironment.application
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)
        appender.enableAppender(context)
        Thread.sleep(2000)
        appender.disableAppender()

        assertFalse(appender.canWriteToFile())
    }

    @Test
    fun testCanWriteToFile() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertFalse(appender.canWriteToFile())
    }

    @Test
    fun testFormatLog() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertTrue(
            appender.formatLog(
                LOG.PRIORITY.DEBUG,
                "",
                "Hello",
                "My friend"
            ).matches(("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d: D/" + PACKAGE_NAME + "\\(" + Thread.currentThread().id + "\\): Hello My friend ").toRegex())
        )
    }

    @Test
    fun testSetMaxLineLength() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        appender.logFileSize = 1000

        assertEquals(1000, appender.logFileSize)
    }

    @Test
    fun testSetNumberOfLogFiles() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        appender.numberOfLogFiles = 5

        assertEquals(5, appender.numberOfLogFiles)
    }

    @Test
    fun testSetMinLogLevel() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        appender.minLogLevel = LOG.PRIORITY.DEBUG

        assertEquals(LOG.PRIORITY.DEBUG, appender.minLogLevel)
    }

    @Test
    fun testGetFileLogPath() {
        val context = RuntimeEnvironment.application
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)
        PowerMockito.mockStatic(AndroidUtils::class.java)
        `when`(
            AndroidUtils.getFileDirPath(
                context,
                String.format(
                    "%s%s.log",
                    File.separator,
                    FILE_NAME
                )
            )
        ).thenReturn("internal/com/mindera/skeletoid/FILENAME.log")
        assertEquals(
            "internal/com/mindera/skeletoid/FILENAME.log",
            appender.getFileLogPath(context)
        )
    }

    @Test
    fun testGetExternalFileLogPath() {
        val context = RuntimeEnvironment.application
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME, true)
        PowerMockito.mockStatic(AndroidUtils::class.java)
        `when`(
            AndroidUtils.getExternalPublicDirectory(
                String.format(
                    "%s%s.log",
                    File.separator,
                    FILE_NAME
                )
            )
        ).thenReturn("external/com/mindera/skeletoid/FILENAME.log")

        assertEquals(
            "external/com/mindera/skeletoid/FILENAME.log",
            appender.getFileLogPath(context)
        )
    }

    @Test
    fun testFileHandlerLevelDebug() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertEquals(Level.SEVERE, appender.getFileHandlerLevel(LOG.PRIORITY.ERROR))
    }

    @Test
    fun testFileHandlerLevelFatal() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertEquals(Level.SEVERE, appender.getFileHandlerLevel(LOG.PRIORITY.FATAL))
    }

    @Test
    fun testFileHandlerLevelInfo() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertEquals(Level.INFO, appender.getFileHandlerLevel(LOG.PRIORITY.INFO))
    }

    @Test
    fun testFileHandlerLevelVerbose() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertEquals(Level.ALL, appender.getFileHandlerLevel(LOG.PRIORITY.VERBOSE))
    }

    @Test
    fun testFileHandlerLevelWarn() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertEquals(Level.WARNING, appender.getFileHandlerLevel(LOG.PRIORITY.WARN))
    }

    @Test
    fun testIsThreadPoolRunningWhenAppenderEnabled() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)
        val context = RuntimeEnvironment.application

        appender.enableAppender(context)

        assertTrue(appender.isThreadPoolRunning)
    }

    @Test
    fun testIsThreadPoolNotRunningWhenAppenderInitialised() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)

        assertFalse(appender.isThreadPoolRunning)
    }

    @Test
    fun testIsThreadPoolNotRunningWhenAppenderDisabled() {
        val context = RuntimeEnvironment.application
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)
        appender.enableAppender(context)

        appender.disableAppender()

        assertFalse(appender.isThreadPoolRunning)
    }

    @Test
    @Ignore
    fun testLog() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)
        val context = RuntimeEnvironment.application
        val fileHandler = mock(FileHandler::class.java)

        appender.enableAppender(context)
        Thread.sleep(5000)
        appender.fileHandler = fileHandler

        appender.log(LOG.PRIORITY.DEBUG, Throwable("oops"), "hello")

        verify(fileHandler).publish(any(LogRecord::class.java))
    }

    @Test
    fun testNoLogIfWritingNotEnabled() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)
        val fileHandler = mock(FileHandler::class.java)
        appender.fileHandler = fileHandler

        appender.log(LOG.PRIORITY.DEBUG, null, "hello")

        verifyNoMoreInteractions(fileHandler)
    }

    @Test
    fun testNoLogIfAppenderNotEnabled() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)
        val fileHandler = mock(FileHandler::class.java)
        appender.fileHandler = fileHandler

        appender.log(LOG.PRIORITY.DEBUG, null, "hello")

        verifyNoMoreInteractions(fileHandler)
    }

    @Test
    fun testNoLogIfAppenderDisabled() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)
        val fileHandler = mock(FileHandler::class.java)
        val context = RuntimeEnvironment.application
        appender.enableAppender(context)
        Thread.sleep(2000)
        appender.fileHandler = fileHandler
        appender.disableAppender()

        appender.log(LOG.PRIORITY.DEBUG, null, "hello")

        verify(fileHandler, times(0)).publish(any(LogRecord::class.java))
    }

    @Test
    fun testNoLogIfLogLevelBelowMinLogLevel() {
        val appender = LogFileAppender(PACKAGE_NAME, FILE_NAME)
        appender.minLogLevel = LOG.PRIORITY.ERROR
        val fileHandler = mock(FileHandler::class.java)
        val context = RuntimeEnvironment.application
        appender.enableAppender(context)
        Thread.sleep(2000)
        appender.fileHandler = fileHandler

        appender.log(LOG.PRIORITY.DEBUG, null, "hello")

        verifyNoMoreInteractions(fileHandler)
    }
}
