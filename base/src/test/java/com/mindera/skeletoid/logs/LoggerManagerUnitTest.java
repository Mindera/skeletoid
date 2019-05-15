package com.mindera.skeletoid.logs;

import android.content.Context;

import com.mindera.skeletoid.generic.AndroidUtils;
import com.mindera.skeletoid.logs.appenders.ILogAppender;
import com.mindera.skeletoid.logs.utils.LogAppenderUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash;
import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getTag;
import static com.mindera.skeletoid.threads.utils.ThreadUtils.getCurrentThreadName;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AndroidUtils.class})
public class LoggerManagerUnitTest {

    /**
     * Should be the same as {@link LoggerManager#LOG_FORMAT_4ARGS}
     */
    private static final String LOG_FORMAT_4ARGS = "%s %s %s | %s";
    private static final String LOG_FORMAT_3ARGS = "%s %s | %s";

    private final String TAG = "TAG";
    private final String TEXT = "Text";
    private String mPackageName = "my.package.name";

    @Test
    public void testCreateLoggerManagerWithPackageName() {
        LoggerManager loggerManager = new LoggerManager(mPackageName);

        assertNotNull(loggerManager);
    }

    @Test
    public void testCreateLoggerManagerWithContext() {
        Context context = mock(Context.class);
        mockStatic(AndroidUtils.class);
        when(AndroidUtils.getApplicationPackage(context)).thenReturn(mPackageName);

        LoggerManager loggerManager = new LoggerManager(context);

        assertNotNull(loggerManager);
    }

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
    public void testDebugLogWithTag() {
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
    public void testErrorLogWithTag() {
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
    public void testWarnLogWithTag() {
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
    public void testFatalLogWithTag() {
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
    public void testInfoLogWithTag() {
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
    public void testVerboseLogWithTag() {
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
    @Test
    public void testDebugLogWithTagAndThrowable() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.DEBUG, throwable, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, throwable, log);
    }

    @Test
    public void testErrorLogWithTagAndThrowable() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.ERROR, throwable, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.ERROR, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.ERROR, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.ERROR, throwable, log);
    }

    @Test
    public void testWarnLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.WARN, throwable, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.WARN, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.WARN, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.WARN, throwable, log);
    }

    @Test
    public void testFatalLogWithTagAndThrowable() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.FATAL, throwable, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.FATAL, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.FATAL, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.FATAL, throwable, log);
    }

    @Test
    public void testInfoLogWithTagAndThrowable() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.INFO, throwable, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.INFO, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.INFO, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.INFO, throwable, log);
    }


    @Test
    public void testVerboseLogWithTagAndThrowable() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.VERBOSE, throwable, TEXT);

        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.VERBOSE, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.VERBOSE, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.VERBOSE, throwable, log);
    }

    @Test
    public void testDebugLogWithClass() {
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

        loggerManager.log(String.class, LOG.PRIORITY.DEBUG, TEXT);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
    }

    @Test
    public void testErrorLogWithClass() {
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

        loggerManager.log(String.class, LOG.PRIORITY.ERROR, TEXT);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.ERROR, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.ERROR, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.ERROR, null, log);
    }

    @Test
    public void testWarnLogWithClass() {
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

        loggerManager.log(String.class, LOG.PRIORITY.WARN, TEXT);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.WARN, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.WARN, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.WARN, null, log);
    }

    @Test
    public void testFatalLogWithClass() {
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

        loggerManager.log(String.class, LOG.PRIORITY.FATAL, TEXT);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.FATAL, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.FATAL, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.FATAL, null, log);
    }

    @Test
    public void testInfoLogWithClass() {
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

        loggerManager.log(String.class, LOG.PRIORITY.INFO, TEXT);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.INFO, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.INFO, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.INFO, null, log);
    }


    @Test
    public void testVerboseLogWithClass() {
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

        loggerManager.log(String.class, LOG.PRIORITY.VERBOSE, TEXT);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.VERBOSE, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.VERBOSE, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.VERBOSE, null, log);
    }

    @Test
    public void testDebugLogWithClassAndThrowable() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, LOG.PRIORITY.DEBUG, TEXT, throwable);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, throwable, log);
    }

    @Test
    public void testErrorLogWithClassAndThrowable() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, LOG.PRIORITY.ERROR, TEXT, throwable);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.ERROR, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.ERROR, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.ERROR, throwable, log);
    }

    @Test
    public void testWarnLogWithClassAndThrowable() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, LOG.PRIORITY.WARN, TEXT, throwable);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.WARN, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.WARN, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.WARN, throwable, log);
    }

    @Test
    public void testFatalLogWithClassAndThrowable() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, LOG.PRIORITY.FATAL, TEXT, throwable);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.FATAL, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.FATAL, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.FATAL, throwable, log);
    }

    @Test
    public void testInfoLogWithClassAndThrowable() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, LOG.PRIORITY.INFO, TEXT, throwable);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.INFO, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.INFO, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.INFO, throwable, log);
    }


    @Test
    public void testVerboseLogWithClassAndThrowable() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        Throwable throwable =  new Throwable();
        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, LOG.PRIORITY.VERBOSE, TEXT, throwable);

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(String.class, false, "", false), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.VERBOSE, throwable, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.VERBOSE, throwable, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.VERBOSE, throwable, log);
    }

    @Test
    public void testNoLogForEmptyAppendersWithTag() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = mock(List.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.VERBOSE, TEXT);

        verify(appenders, times(0)).listIterator();
    }

    @Test
    public void testNoLogForEmptyAppendersWithTagAndThrowable() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = mock(List.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.VERBOSE, new Throwable(), TEXT);

        verify(appenders, times(0)).listIterator();
    }

    @Test
    public void testNoLogForEmptyAppendersWithClass() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = mock(List.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, LOG.PRIORITY.VERBOSE, TEXT);

        verify(appenders, times(0)).listIterator();
    }

    @Test
    public void testNoLogForEmptyAppendersWithClassAndThrowable() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = mock(List.class);

        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, LOG.PRIORITY.VERBOSE, TEXT, new Throwable());

        verify(appenders, times(0)).listIterator();
    }

    @Test
    public void testNoLogForNullTagWithTag() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(null, LOG.PRIORITY.VERBOSE, TEXT, TEXT);

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullTagWithTagAndThrowable() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(null, LOG.PRIORITY.VERBOSE, new Throwable(), TEXT);

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullClassWithClass() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(null, LOG.PRIORITY.VERBOSE, TEXT);

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullClassWithClassAndThrowable() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(null, LOG.PRIORITY.VERBOSE, TEXT, new Throwable());

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullPriorityWithTag() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, null, TEXT);

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullPriorityWithTagAndThrowable() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, null, new Throwable(), TEXT);

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullPriorityWithClass() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, null, TEXT);

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullPriorityWithClassAndThrowable() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, null, TEXT, new Throwable());

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullTextWithTag() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.DEBUG, null);

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullTextWithTagAndThrowable() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.DEBUG, new Throwable(), null);

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullTextWithClass() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, LOG.PRIORITY.DEBUG, null);

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullTextWithClassAndThrowable() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, LOG.PRIORITY.DEBUG, null, new Throwable());

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullThrowableWithTagAndThrowable() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(TAG, LOG.PRIORITY.DEBUG, (Throwable) null, TEXT);

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    @Test
    public void testNoLogForNullThrowableWithClassAndThrowable() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.addAppenders(context, appenders);

        loggerManager.log(String.class, LOG.PRIORITY.DEBUG, TEXT, null);

        verify(appenderA, times(0)).log(any(LOG.PRIORITY.class), any(Throwable.class), anyString());
    }

    private ILogAppender mockAppender(String analyticsId) {
        ILogAppender appender = mock(ILogAppender.class);

        when(appender.getLoggerId()).thenReturn(analyticsId);

        return appender;
    }

    @Test
    public void testSetMethodNameVisible() {
        LoggerManager loggerManager = new LoggerManager(mPackageName);
        loggerManager.setMethodNameVisible(true);

        assertTrue(loggerManager.mAddMethodName);
    }
}
