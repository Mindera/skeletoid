package com.mindera.skeletoid.logs;

import android.content.Context;
import android.util.Log;

import com.mindera.skeletoid.logs.appenders.ILogAppender;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggerManagerUnitTest {

    @Test
    public void testAddAppendersNull() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);
        List<String> appendersIds = loggerManager.addAppenders(context, null);

        assertNotNull(appendersIds);
        assertEquals(0, appendersIds.size());
    }

    @Test
    public void testAddAppendersEmpty() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);
        List<String> appendersIds = loggerManager.addAppenders(context, new ArrayList<ILogAppender>());

        assertNotNull(appendersIds);
        assertEquals(0, appendersIds.size());
    }

    @Test
    public void testAddAppenders() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        List<String> appendersIds = loggerManager.addAppenders(context, appenders);

        verify(appenderA, times(1)).enableAppender(context);
        verify(appenderB, times(1)).enableAppender(context);
        verify(appenderC, times(1)).enableAppender(context);

        assertNotNull(appendersIds);
        assertEquals(3, appendersIds.size());
        assertEquals("A", appendersIds.get(0));
        assertEquals("B", appendersIds.get(1));
        assertEquals("C", appendersIds.get(2));
    }

    @Test
    @Ignore("Can't mock Log.d")
    public void testAddAppendersRepeated() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB1 = mockAppender("B");
        ILogAppender appenderB2 = mockAppender("B");

        appenders.add(appenderA);
        appenders.add(appenderB1);
        appenders.add(appenderB2);

        List<String> appendersIds = loggerManager.addAppenders(context, appenders);

        assertNotNull(appendersIds);
        assertEquals(2, appendersIds.size());
        assertEquals("A", appendersIds.get(0));
        assertEquals("B", appendersIds.get(1));
    }

    @Test
    public void testDisableAppendersNull() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);
        loggerManager.disableAppenders(context, null);
    }

    @Test
    public void testDisableAppendersEmpty() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);
        loggerManager.disableAppenders(context, new ArrayList<String>());
    }

    @Test
    public void testDisableAppenders() {
        Context context = mock(Context.class);

        LoggerManager loggerManager = new LoggerManager(context);

        List<ILogAppender> appenders = new ArrayList<>();

        ILogAppender appenderA = mockAppender("A");
        ILogAppender appenderB = mockAppender("B");
        ILogAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        List<String> appendersIds = loggerManager.addAppenders(context, appenders);

        loggerManager.disableAppenders(context, appendersIds);
        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testDisableAllAppenders() {
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

        loggerManager.disableAllAppenders();
        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
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
