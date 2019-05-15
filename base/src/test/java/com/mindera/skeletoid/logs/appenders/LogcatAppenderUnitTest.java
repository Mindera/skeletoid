package com.mindera.skeletoid.logs.appenders;

import android.content.Context;
import android.util.Log;

import com.mindera.skeletoid.logs.LOG;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class LogcatAppenderUnitTest {

    private String mPackageName = "my.package.name";

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNull() {
        new LogcatAppender(null);
    }

    @Test
    public void testConstructor() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);

        assertEquals("LogcatAppender", logcatAppender.getLoggerId());
    }

    @Test
    public void testEnableAppender() {
        Context context = mock(Context.class);

        LogcatAppender logcatAppender = mock(LogcatAppender.class);
        logcatAppender.enableAppender(context);

        verify(logcatAppender, times(1)).enableAppender(context);
    }

    @Test
    public void testDisableAppender() {
        LogcatAppender logcatAppender = mock(LogcatAppender.class);
        logcatAppender.disableAppender();

        verify(logcatAppender, times(1)).disableAppender();
    }

    @Test
    public void testFormatLog() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);

        List<String> logs = new ArrayList();

        assertEquals(logs, logcatAppender.formatLog(null));

        logs.add("abc");

        assertEquals(logs, logcatAppender.formatLog("abc"));
        logs.clear();

        logcatAppender.setMaxLineLength(2);
        logs.add("[Chunk 1 of 2] ab");
        logs.add("[Chunk 2 of 2] cd");
        assertEquals(logs, logcatAppender.formatLog("abcd"));
        logs.clear();

        logcatAppender.setMaxLineLength(2);
        logs.add("[Chunk 1 of 3] ab");
        logs.add("[Chunk 2 of 3] cd");
        logs.add("[Chunk 3 of 3] e");
        assertEquals(logs, logcatAppender.formatLog("abcde"));
    }

    @Test
    public void testSetMaxLineLength() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);

        logcatAppender.setMaxLineLength(1);
        assertEquals(1, logcatAppender.getMaxLineLength());
    }

    @Test
    public void testSetMinLogLevel() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);

        logcatAppender.setMinLogLevel(LOG.PRIORITY.DEBUG);
        assertEquals(LOG.PRIORITY.DEBUG, logcatAppender.getMinLogLevel());
    }

    @Test
    public void testDefaultSplitLinesAboveMaxLength() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);

        assertTrue(logcatAppender.isSplitLinesAboveMaxLength());
    }

    @Test
    public void testEnableSplitLinesAboveMaxLength() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);

        logcatAppender.setSplitLinesAboveMaxLength(true);
        assertTrue(logcatAppender.isSplitLinesAboveMaxLength());
    }

    @Test
    public void testDisableSplitLinesAboveMaxLength() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);

        logcatAppender.setSplitLinesAboveMaxLength(false);
        assertFalse(logcatAppender.isSplitLinesAboveMaxLength());
    }

    @Test
    public void testLoggingDebug() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);
        PowerMockito.mockStatic(Log.class);

        logcatAppender.log(LOG.PRIORITY.DEBUG, null, "hello");

        PowerMockito.verifyStatic(Log.class, VerificationModeFactory.times(1));
        Log.d(mPackageName, "hello ", null);
    }

    @Test
    public void testLoggingWarn() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);
        PowerMockito.mockStatic(Log.class);

        logcatAppender.log(LOG.PRIORITY.WARN, null, "hello");

        PowerMockito.verifyStatic(Log.class, VerificationModeFactory.times(1));
        Log.w(mPackageName, "hello ", null);
    }

    @Test
    public void testLoggingError() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);
        PowerMockito.mockStatic(Log.class);

        logcatAppender.log(LOG.PRIORITY.ERROR, null, "hello");

        PowerMockito.verifyStatic(Log.class, VerificationModeFactory.times(1));
        Log.e(mPackageName, "hello ", null);
    }

    @Test
    public void testLoggingVerbose() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);
        PowerMockito.mockStatic(Log.class);

        logcatAppender.log(LOG.PRIORITY.VERBOSE, null, "hello");

        PowerMockito.verifyStatic(Log.class, VerificationModeFactory.times(1));
        Log.v(mPackageName, "hello ", null);
    }

    @Test
    public void testLoggingInfo() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);
        PowerMockito.mockStatic(Log.class);

        logcatAppender.log(LOG.PRIORITY.INFO, null, "hello");

        PowerMockito.verifyStatic(Log.class, VerificationModeFactory.times(1));
        Log.i(mPackageName, "hello ", null);
    }

    @Test
    public void testLoggingFatal() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);
        PowerMockito.mockStatic(Log.class);

        logcatAppender.log(LOG.PRIORITY.FATAL, null, "hello");

        PowerMockito.verifyStatic(Log.class, VerificationModeFactory.times(1));
        Log.wtf(mPackageName, "hello ", null);
    }

    @Test
    public void testNullTextNotLogged() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);
        PowerMockito.mockStatic(Log.class);

        logcatAppender.log(LOG.PRIORITY.FATAL, null, null);

        PowerMockito.verifyStatic(Log.class, VerificationModeFactory.times(0));
        Log.wtf(mPackageName, "hello ", null);
    }

    @Test
    public void testNotLoggingBelowMinLogLevel() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);
        logcatAppender.setMinLogLevel(LOG.PRIORITY.ERROR);
        PowerMockito.mockStatic(Log.class);

        logcatAppender.log(LOG.PRIORITY.DEBUG, null, "hello");

        PowerMockito.verifyStatic(Log.class, VerificationModeFactory.times(0));
        Log.d(mPackageName, "hello ", null);
    }
}
