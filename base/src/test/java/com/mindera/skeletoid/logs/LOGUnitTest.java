package com.mindera.skeletoid.logs;

import android.content.Context;

import com.mindera.skeletoid.generic.AndroidUtils;
import com.mindera.skeletoid.logs.appenders.ILogAppender;
import com.mindera.skeletoid.logs.utils.LogAppenderUtils;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash;
import static com.mindera.skeletoid.threads.utils.ThreadUtils.getCurrentThreadName;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerManager.class, AndroidUtils.class})
public class LOGUnitTest {

    /**
     * Should be the same as {@link LoggerManager#LOG_FORMAT_4ARGS}
     */
    private static final String LOG_FORMAT_4ARGS = "%s %s %s | %s";

    private final String TAG = "TAG";
    private final String TEXT = "Text";
    private String mPackageName = "my.package.name";

    @After
    public void cleanupLOG() {
        Context context = mock(Context.class);
        LOG.deinit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitContextAndPackageNameNullFail() {
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");
        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(null, null, appenders);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitContextNullFail() {
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");
        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(null, appenders);
    }

    @Test()
    public void testIsNotInitialisedBuDefault() {
        assertFalse(LOG.isInitialized());
    }

    @Test
    public void testInitWithContext() throws Exception {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");
        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);
        mockStatic(AndroidUtils.class);
        when(AndroidUtils.getApplicationPackage(context)).thenReturn("package");

        LOG.init(context);
        LOG.addAppenders(context, appenders);

        verify(appenderA, times(1)).enableAppender(context);
        verify(appenderB, times(1)).enableAppender(context);
        verify(appenderC, times(1)).enableAppender(context);
        assertTrue(LOG.isInitialized());
    }

    @Test
    public void testInitWithContextAndPackageName() {
        Context context = mock(Context.class);

        LOG.init(context, mPackageName);

        assertTrue(LOG.isInitialized());
    }

    @Test
    public void testInitWithContextAndAppenders() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");
        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        mockStatic(AndroidUtils.class);
        when(AndroidUtils.getApplicationPackage(context)).thenReturn("package");

        LOG.init(context, appenders);

        verify(appenderA, times(1)).enableAppender(context);
        verify(appenderB, times(1)).enableAppender(context);
        verify(appenderC, times(1)).enableAppender(context);
        assertTrue(LOG.isInitialized());
    }

    @Test
    public void testInitWithContextAndPackageNameAndAppenders() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");
        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName, appenders);

        verify(appenderA, times(1)).enableAppender(context);
        verify(appenderB, times(1)).enableAppender(context);
        verify(appenderC, times(1)).enableAppender(context);
        assertTrue(LOG.isInitialized());
    }

    @Test
    public void testDebugLog() {
        Context context = mock(Context.class);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName, appenders);

        LOG.d(TAG, TEXT);

        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
    }

    @Test
    public void testErrorLog() {
        Context context = mock(Context.class);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName, appenders);

        LOG.e(TAG, TEXT);

        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.ERROR, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.ERROR, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.ERROR, null, log);
    }

    @Test
    public void testWarnLog() {
        Context context = mock(Context.class);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName, appenders);

        LOG.w(TAG, TEXT);

        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.WARN, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.WARN, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.WARN, null, log);
    }

    @Test
    public void testFatalLog() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");
        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        LOG.init(context, mPackageName, appenders);
        LOG.wtf(TAG, TEXT);

        verify(appenderA, times(1)).log(LOG.PRIORITY.FATAL, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.FATAL, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.FATAL, null, log);
    }

    @Test
    public void testInfoLog() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");
        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        LOG.init(context, mPackageName, appenders);
        LOG.i(TAG, TEXT);

        verify(appenderA, times(1)).log(LOG.PRIORITY.INFO, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.INFO, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.INFO, null, log);
    }

    @Test
    public void testNoDebugLogDeinitialised() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        LOG.init(context, mPackageName, appenders);
        LOG.deinit();
        LOG.d(TAG, TEXT);

        verify(appenderA, times(0)).log(LOG.PRIORITY.DEBUG, null, log);
    }

    @Test
    public void testNoErrorLogDeinitialised() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        LOG.init(context, mPackageName, appenders);
        LOG.deinit();
        LOG.e(TAG, TEXT);

        verify(appenderA, times(0)).log(LOG.PRIORITY.ERROR, null, log);
    }

    @Test
    public void testNoWarnLogDeinitialised() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        LOG.init(context, mPackageName, appenders);
        LOG.deinit();
        LOG.w(TAG, TEXT);

        verify(appenderA, times(0)).log(LOG.PRIORITY.WARN, null, log);
    }

    @Test
    public void testNoFatalLogDeinitialised() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        LOG.init(context, mPackageName, appenders);
        LOG.deinit();
        LOG.wtf(TAG, TEXT);

        verify(appenderA, times(0)).log(LOG.PRIORITY.FATAL, null, log);
    }

    @Test
    public void testNoInfoLogDeinitialised() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        appenders.add(appenderA);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        LOG.init(context, mPackageName, appenders);
        LOG.deinit();
        LOG.i(TAG, TEXT);

        verify(appenderA, times(0)).log(LOG.PRIORITY.INFO, null, log);
    }

    @Test
    public void testDebugWithExceptionLog() {
        Context context = mock(Context.class);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName, appenders);

        Exception e = new Exception();
        LOG.d(TAG, e, TEXT);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, e, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, e, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, e, log);
    }

    @Test
    public void testErrorWithExceptionLog() {
        Context context = mock(Context.class);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName, appenders);

        Exception e = new Exception();
        LOG.e(TAG, e, TEXT);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.ERROR, e, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.ERROR, e, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.ERROR, e, log);
    }

    @Test
    public void testWarnWithExceptionLog() {
        Context context = mock(Context.class);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName, appenders);

        Exception e = new Exception();
        LOG.w(TAG, e, TEXT);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.WARN, e, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.WARN, e, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.WARN, e, log);
    }

    @Test
    public void testFatalWithExceptionLog() {
        Context context = mock(Context.class);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName, appenders);

        Exception e = new Exception();
        LOG.wtf(TAG, e, TEXT);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.FATAL, e, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.FATAL, e, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.FATAL, e, log);
    }

    @Test
    public void testInfoWithExceptionLog() {
        Context context = mock(Context.class);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName, appenders);

        Exception e = new Exception();
        LOG.i(TAG, e, TEXT);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.INFO, e, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.INFO, e, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.INFO, e, log);
    }


    @Test
    public void testVerboseWithExceptionLog() {
        Context context = mock(Context.class);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName, appenders);

        Exception e = new Exception();
        LOG.v(TAG, e, TEXT);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.VERBOSE, e, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.VERBOSE, e, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.VERBOSE, e, log);
    }

    @Test
    public void testVerboseLog() {
        Context context = mock(Context.class);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName, appenders);

        LOG.v(TAG, TEXT);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadName(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.VERBOSE, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.VERBOSE, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.VERBOSE, null, log);
    }

    @Test
    public void testRemoveAllAppenders() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");
        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName);
        final Set<String> ids = LOG.addAppenders(context, appenders);

        LOG.removeAppenders(context, ids);

        verify(appenderA).disableAppender();
        verify(appenderB).disableAppender();
        verify(appenderC).disableAppender();
    }

    @Test
    public void testRemoveSomeAppenders() {
        Context context = mock(Context.class);
        List<ILogAppender> appenders = new ArrayList<>();
        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");
        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName);
        final Set<String> ids = LOG.addAppenders(context, appenders);
        final List<String> idsList = new ArrayList<>(ids);

        for (String id : ids) {
            if (id.equals("A")) {
                idsList.remove(id);
            }
        }
        final Set<String> idsToRemove = new HashSet<>(idsList);

        LOG.removeAppenders(context, idsToRemove);

        verify(appenderA, times(0)).disableAppender();
        verify(appenderB).disableAppender();
        verify(appenderC).disableAppender();
    }

    @Test
    public void testRemoveNullAppenders() {
        Context context = mock(Context.class);
        LOG.init(context, mPackageName);

        LOG.removeAppenders(context, null);
    }

    private ILogAppender mockAppender(String loggerId) {
        ILogAppender appender = mock(ILogAppender.class);

        when(appender.getLoggerId()).thenReturn(loggerId);

        return appender;
    }
}
