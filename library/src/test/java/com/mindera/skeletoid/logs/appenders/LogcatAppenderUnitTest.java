package com.mindera.skeletoid.logs.appenders;

import android.content.Context;

import com.mindera.skeletoid.logs.LOG;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class LogcatAppenderUnitTest {

    private String mPackageName = "my.package.name";
    private final String TAG = "TAG";
    private final String TEXT = "Text";


    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNull() {
        new LogcatAppender(null);
    }

    @Test
    public void testConstructor() {
        Context context = mock(Context.class);

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

        assertEquals(new ArrayList<String>() {{
            add("abc");
        }}, logcatAppender.formatLog("abc"));

        logcatAppender.setMaxLineLength(2);

        assertEquals(new ArrayList<String>() {{
            add("[Chunk 1 of 2] ab");
            add("[Chunk 2 of 2] cd");
        }}, logcatAppender.formatLog("abcd"));

        assertEquals(new ArrayList<String>() {{
            add("[Chunk 1 of 3] ab");
            add("[Chunk 2 of 3] cd");
            add("[Chunk 3 of 3] e");
        }}, logcatAppender.formatLog("abcde"));
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
