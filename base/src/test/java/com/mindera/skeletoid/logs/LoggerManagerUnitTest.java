package com.mindera.skeletoid.logs;

import android.content.Context;
import android.util.ArraySet;

import com.mindera.skeletoid.logs.appenders.ILogAppender;
import com.mindera.skeletoid.logs.utils.LogAppenderUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mindera.skeletoid.logs.LoggerManager.LOG_FORMAT_4ARGS;
import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash;
import static com.mindera.skeletoid.threads.utils.ThreadUtils.getCurrentThreadName;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggerManagerUnitTest {

    /**
     * Should be the same as {@link LoggerManager#LOG_FORMAT_4ARGS}
     */
    private static final String LOG_FORMAT_4ARGS = "%s %s %s | %s";

    private final String TAG = "TAG";
    private final String TEXT = "Text";
    private String mPackageName = "my.package.name";

    @Test
    public void testAddAppendersNull() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Set<String> appendersIds = loggerManager.addAppenders(context, null);

        assertNotNull(appendersIds);
        assertEquals(0, appendersIds.size());
    }

    @Test
    public void testAddAppendersEmpty() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Set<String> appendersIds = loggerManager.addAppenders(context, new ArrayList<ILogAppender>());

        assertNotNull(appendersIds);
        assertEquals(0, appendersIds.size());
    }

    @Test
    public void testAddAppenders() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Set<String> appendersIds = loggerManager.addAppenders(context, appenders);

        verify(appenderA, times(1)).enableAppender(context);
        verify(appenderB, times(1)).enableAppender(context);
        verify(appenderC, times(1)).enableAppender(context);

        assertNotNull(appendersIds);
        assertEquals(3, appendersIds.size());
        assertTrue(appendersIds.contains("A"));
        assertTrue(appendersIds.contains("B"));
        assertTrue(appendersIds.contains("C"));
    }

    @Test
    public void testAddAppendersRepeated() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB1 = mockAppender("B");
        ILogAppender appenderB2 = mockAppender("B");

        appenders.add(appenderA);
        appenders.add(appenderB1);
        appenders.add(appenderB2);

        Set<String> appendersIds = loggerManager.addAppenders(context, appenders);

        assertNotNull(appendersIds);
        assertEquals(2, appendersIds.size());
        assertTrue(appendersIds.contains("A"));
        assertTrue(appendersIds.contains("B"));
    }

    @Test
    public void testDisableAppendersNull() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.removeAppenders(context, null);
    }

    @Test
    public void testDisableAppendersEmpty() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.removeAppenders(context, new HashSet<String>());
    }

    @Test
    public void testDisableAppenders() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Set<String> appendersIds = loggerManager.addAppenders(context, appenders);

        loggerManager.removeAppenders(context, appendersIds);
        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testRemoveAppender() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);

        List<ILogAppender> appenders = new ArrayList<>();
        List<String> appendersId = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        appendersId.add("A");

        loggerManager.addAppenders(context, appenders);
        loggerManager.removeAppenders(context, new HashSet<>(appendersId));
        verify(appenderA, times(1)).disableAppender();
    }

    @Test
    public void testRemoveAllAppenders() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.removeAllAppenders();
        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testDebugLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.DEBUG, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
    }

    @Test
    public void testErrorLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.ERROR, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.ERROR, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.ERROR, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.ERROR, null, log);
    }

    @Test
    public void testWarnLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.WARN, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.WARN, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.WARN, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.WARN, null, log);
    }

    @Test
    public void testFatalLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.FATAL, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.FATAL, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.FATAL, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.FATAL, null, log);
    }

    @Test
    public void testInfoLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.INFO, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.INFO, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.INFO, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.INFO, null, log);
    }


    @Test
    public void testVerboseLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.VERBOSE, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.VERBOSE, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.VERBOSE, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.VERBOSE, null, log);
    }

    private ILogAppender mockAppender(String analyticsId) {
        ILogAppender appender = mock(ILogAppender.class);

        when(appender.getLoggerId()).thenReturn(analyticsId);

        return appender;
    }
}
