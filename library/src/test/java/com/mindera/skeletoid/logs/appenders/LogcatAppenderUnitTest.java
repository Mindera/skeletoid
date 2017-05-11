package com.mindera.skeletoid.logs.appenders;

import com.mindera.skeletoid.logs.LOG;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

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

//    @Test
//    public void testEnableAppender() {
//        Context context = mock(Context.class);
//
//        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);
//        logcatAppender.enableAppender(context);
//
//        verify(logcatAppender, times(1)).enableAppender(context);
//    }
//
//
//    @Test
//    public void testDisableAppender() {
//        Context context = mock(Context.class);
//
//        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);
//        logcatAppender.disableAppender();
//
//        verify(logcatAppender, times(1)).disableAppender();
//    }


    @Test
    public void testFormatLog() {
        LogcatAppender logcatAppender = new LogcatAppender(mPackageName);

        List<String> logs = new ArrayList();
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

}
