package com.mindera.skeletoid.logs;

import android.content.Context;

import com.mindera.skeletoid.logs.appenders.ILogAppender;
import com.mindera.skeletoid.logs.utils.LogAppenderUtils;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mindera.skeletoid.logs.LoggerManager.LOG_FORMAT_4ARGS;
import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getCurrentThreadId;
import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggerUnitTest {

    private String mPackageName = "my.package.name";
    private final String TAG = "TAG";
    private final String TEXT = "Text";

    @Test
    public void testInit() {
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
    }

    @Ignore
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
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadId(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, null, log);
    }

    @Ignore
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
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadId(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.ERROR, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.ERROR, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.ERROR, null, log);
    }

    @Ignore
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
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadId(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.WARN, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.WARN, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.WARN, null, log);
    }

    @Ignore
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

        LOG.init(context, mPackageName, appenders);

        LOG.wtf(TAG, TEXT);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadId(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.FATAL, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.FATAL, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.FATAL, null, log);
    }

    @Ignore
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

        LOG.init(context, mPackageName, appenders);

        LOG.i(TAG, TEXT);
        //This is ugly.. but I don't see another way.
        final String log = String.format(LOG_FORMAT_4ARGS, TAG, getObjectHash(TAG), getCurrentThreadId(), LogAppenderUtils.getLogString(TEXT));

        verify(appenderA, times(1)).log(LOG.PRIORITY.INFO, null, log);
        verify(appenderB, times(1)).log(LOG.PRIORITY.INFO, null, log);
        verify(appenderC, times(1)).log(LOG.PRIORITY.INFO, null, log);
    }


    private ILogAppender mockAppender(String loggerId) {
        ILogAppender appender = mock(ILogAppender.class);

        when(appender.getLoggerId()).thenReturn(loggerId);

        return appender;
    }
}
