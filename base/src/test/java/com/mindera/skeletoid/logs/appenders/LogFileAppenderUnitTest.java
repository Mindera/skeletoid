package com.mindera.skeletoid.logs.appenders;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.FileProvider;

import com.mindera.skeletoid.generic.AndroidUtils;
import com.mindera.skeletoid.logs.LOG;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileProvider.class, Intent.class, AndroidUtils.class})
public class LogFileAppenderUnitTest {

    private String PACKAGE_NAME = "my.package.name";
    private final String FILE_NAME = "FILENAME";


    @Test(expected = IllegalArgumentException.class)
    public void testConstructorAllNull() {
        new LogFileAppender(null, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPackageNameNull() {
        new LogFileAppender(null, FILE_NAME);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFileNameInvalid() {
        new LogFileAppender(PACKAGE_NAME, "*");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFileNameNull() {
        new LogFileAppender(PACKAGE_NAME, null);

    }

    @Test
    public void testFileNameIsValidEmpty() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertFalse(appender.isFilenameValid(""));
    }

    @Test
    public void testFileNameIsValidInvalid() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertFalse(appender.isFilenameValid("//"));
    }

    @Test
    public void testFileNameIsValid() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertTrue(appender.isFilenameValid(FILE_NAME));
    }


    @Test
    public void testConstructor() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertEquals("LogFileAppender", appender.getLoggerId());
    }

    @Test
    public void testEnableAppender() {
        Context context = mock(Context.class);
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        appender.enableAppender(context);

        assertFalse(appender.canWriteToFile());
    }

    @Test
    public void testDisableAppender() throws InterruptedException {
        Context context = mock(Context.class);
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        appender.enableAppender(context);
        appender.disableAppender();
        Thread.sleep(600);

        assertFalse(appender.canWriteToFile());
    }

    @Test
    public void testCanWriteToFile() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertFalse(appender.canWriteToFile());
    }

    @Test
    public void testFormatLog() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertTrue(appender.formatLog(LOG.PRIORITY.DEBUG, null, "Hello", "My friend").matches("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d: D/" + PACKAGE_NAME + "\\(" + Thread.currentThread().getId() + "\\): Hello My friend "));
    }

    @Test
    public void testSetMaxLineLength() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        appender.setLogFileSize(1000);

        assertEquals(1000, appender.getLogFileSize());
    }

    @Test
    public void testSetNumberOfLogFiles() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        appender.setNumberOfLogFiles(5);

        assertEquals(5, appender.getNumberOfLogFiles());
    }

    @Test
    public void testSetMinLogLevel() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        appender.setMinLogLevel(LOG.PRIORITY.DEBUG);

        assertEquals(LOG.PRIORITY.DEBUG, appender.getMinLogLevel());
    }

    @Test
    public void testGetFileLogPath() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        Context context = mock(Context.class);

        File file = mock(File.class);
        when(context.getFilesDir()).thenReturn(file);
        when(file.getPath()).thenReturn("/com/mindera/skeletoid");

        assertEquals("/com/mindera/skeletoid/FILENAME.log", appender.getFileLogPath(context));
    }

    @Test
    public void testGetExternalFileLogPath() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME, true);
        Context context = mock(Context.class);
        PowerMockito.mockStatic(AndroidUtils.class);
        when(AndroidUtils.getExternalPublicDirectory(String.format("%s%s.log", File.separator, FILE_NAME))).thenReturn("external/com/mindera/skeletoid/FILENAME.log");

        assertEquals("external/com/mindera/skeletoid/FILENAME.log", appender.getFileLogPath(context));
    }

    @Test
    public void testFileHandlerLevelDebug() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertEquals(Level.SEVERE, appender.getFileHandlerLevel(LOG.PRIORITY.ERROR));
    }

    @Test
    public void testFileHandlerLevelFatal() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertEquals(Level.SEVERE, appender.getFileHandlerLevel(LOG.PRIORITY.FATAL));
    }

    @Test
    public void testFileHandlerLevelInfo() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertEquals(Level.INFO, appender.getFileHandlerLevel(LOG.PRIORITY.INFO));
    }

    @Test
    public void testFileHandlerLevelVerbose() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertEquals(Level.ALL, appender.getFileHandlerLevel(LOG.PRIORITY.VERBOSE));
    }

    @Test
    public void testFileHandlerLevelWarn() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertEquals(Level.WARNING, appender.getFileHandlerLevel(LOG.PRIORITY.WARN));
    }

    @Test
    public void testIsThreadPoolRunningWhenAppenderEnabled() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        Context context = mock(Context.class);
        appender.enableAppender(context);

        assertTrue(appender.isThreadPoolRunning());
    }

    @Test
    public void testIsThreadPoolNotRunningWhenAppenderInitialised() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);

        assertFalse(appender.isThreadPoolRunning());
    }

    @Test
    public void testIsThreadPoolNotRunningWhenAppenderDisabled() {
        Context context = mock(Context.class);
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        appender.enableAppender(context);

        appender.disableAppender();

        assertFalse(appender.isThreadPoolRunning());
    }

    @Test
    public void testLog() throws InterruptedException {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        FileHandler fileHandler = mock(FileHandler.class);
        Context context = mock(Context.class);
        File file = mock(File.class);
        when(context.getFilesDir()).thenReturn(file);
        when(file.getPath()).thenReturn("/com/mindera/skeletoid");
        appender.enableAppender(context);
        Thread.sleep(500);
        appender.mCanWriteToFile = true;
        appender.mFileHandler = fileHandler;

        appender.log(LOG.PRIORITY.DEBUG, new Throwable("oops"), "hello");

        verify(fileHandler).publish(any(LogRecord.class));
    }

    @Test
    public void testNoLogIfWritingNotEnabled() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        FileHandler fileHandler = mock(FileHandler.class);
        Context context = mock(Context.class);
        File file = mock(File.class);
        when(context.getFilesDir()).thenReturn(file);
        when(file.getPath()).thenReturn("/com/mindera/skeletoid");

        appender.log(LOG.PRIORITY.DEBUG, null, "hello");

        verify(fileHandler, times(0)).publish(any(LogRecord.class));
    }

    @Test
    public void testNoLogIfAppenderNotEnabled() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        FileHandler fileHandler = mock(FileHandler.class);

        appender.log(LOG.PRIORITY.DEBUG, null, "hello");

        verify(fileHandler, times(0)).publish(any(LogRecord.class));
    }

    @Test
    public void testNoLogIfAppenderDisabled() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        FileHandler fileHandler = mock(FileHandler.class);
        Context context = mock(Context.class);
        File file = mock(File.class);
        when(context.getFilesDir()).thenReturn(file);
        when(file.getPath()).thenReturn("/com/mindera/skeletoid");
        appender.enableAppender(context);
        appender.mCanWriteToFile = true;
        appender.mFileHandler = fileHandler;
        appender.disableAppender();

        appender.log(LOG.PRIORITY.DEBUG, null, "hello");

        verify(fileHandler, times(0)).publish(any(LogRecord.class));
    }

    @Test
    public void testNoLogIfLogLevelBelowMinLogLevel() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        appender.setMinLogLevel(LOG.PRIORITY.ERROR);
        FileHandler fileHandler = mock(FileHandler.class);
        Context context = mock(Context.class);
        File file = mock(File.class);
        when(context.getFilesDir()).thenReturn(file);
        when(file.getPath()).thenReturn("/com/mindera/skeletoid");
        appender.enableAppender(context);
        appender.mFileHandler = fileHandler;

        appender.log(LOG.PRIORITY.DEBUG, null, "hello");

        verify(fileHandler, times(0)).publish(any(LogRecord.class));
    }
}
