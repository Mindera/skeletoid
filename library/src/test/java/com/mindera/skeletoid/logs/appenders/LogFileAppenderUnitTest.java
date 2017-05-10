package com.mindera.skeletoid.logs.appenders;

import android.content.Context;

import com.mindera.skeletoid.logs.LOG;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LogFileAppenderUnitTest {

    private String PACKAGE_NAME = "my.package.name";
    private final String FILE_NAME = "FILE_NAME";


    @Test(expected = IllegalArgumentException.class)
    public void testConstructorAllNull() {
        LogFileAppender appender = new LogFileAppender(null, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPackageNameNull() {
        LogFileAppender appender = new LogFileAppender(null, FILE_NAME);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFileNameInvalid() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, "*");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFileNameNull() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, null);

    }

    @Test
    public void testFileNameIsValidEmpty() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        assertFalse(appender.isFilenameValid(""));
    }

    @Test
    public void testFileNameIsValidInvalid() {
        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        assertFalse(appender.isFilenameValid("*"));
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

        //TODO What should I validate here?
        verify(appender, times(1)).enableAppender(context);
    }


    @Test
    public void testDisableAppender() {
        Context context = mock(Context.class);

        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
        appender.disableAppender();

        //TODO What should I validate here?
        verify(appender, times(1)).disableAppender();
    }


    //TODO How can I handle the date part on this?
//    @Test
//    public void testFormatLog() {
//        LogFileAppender appender = new LogFileAppender(PACKAGE_NAME, FILE_NAME);
//        assertEquals("", appender.formatLog(LOG.PRIORITY.DEBUG, null, "Hello", "My friend"));
//    }


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

}
