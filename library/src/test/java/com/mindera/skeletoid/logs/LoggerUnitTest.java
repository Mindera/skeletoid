package com.mindera.skeletoid.logs;

import android.content.Context;
import android.util.Log;

import com.mindera.skeletoid.logs.appenders.ILogAppender;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggerUnitTest {

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

        LOG.init(context, appenders);

        verify(appenderA, times(1)).enableAppender(context);
        verify(appenderB, times(1)).enableAppender(context);
        verify(appenderC, times(1)).enableAppender(context);
    }

    @Ignore
    @Test
    public void testDebugLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        Log.d("TAG", "Test");

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
    }

    @Ignore
    @Test
    public void testErrorLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        Log.e("TAG", "Test");

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
    }

    @Ignore
    @Test
    public void testWarnLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        Log.w("TAG", "Test");

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
    }

    @Ignore
    @Test
    public void testFatalLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        LOG.wtf("TAG", "Test");

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
    }

    @Ignore
    @Test
    public void testInfoLog() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        loggerManager.addAppenders(context, appenders);

        LOG.i("TAG", "Test");

        verify(appenderA, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
        verify(appenderB, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
        verify(appenderC, times(1)).log(LOG.PRIORITY.DEBUG, null, "TAG Test");
    }


    private ILogAppender mockAppender(String analyticsId) {
        ILogAppender appender = mock(ILogAppender.class);

        when(appender.getLoggerId()).thenReturn(analyticsId);

        return appender;
    }
}
