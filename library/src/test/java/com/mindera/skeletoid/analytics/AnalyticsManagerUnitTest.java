package com.mindera.skeletoid.analytics;

import android.content.Context;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;
import com.mindera.skeletoid.logs.LOG;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnalyticsManagerUnitTest {

    private String mPackageName = "my.package.name";


    @Test
    public void testAddAppendersNull() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();
        List<String> appendersIds = analyticsManager.addAppenders(context, null);

        assertNotNull(appendersIds);
        assertEquals(0, appendersIds.size());
    }

    @Test
    public void testAddAppendersEmpty() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();
        List<String> appendersIds = analyticsManager.addAppenders(context, new ArrayList<IAnalyticsAppender>());

        assertNotNull(appendersIds);
        assertEquals(0, appendersIds.size());
    }

    @Test
    public void testAddAppenders() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        List<String> appendersIds = analyticsManager.addAppenders(context, appenders);

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
    public void testAddAppendersRepeated() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB1 = mockAppender("B");
        IAnalyticsAppender appenderB2 = mockAppender("B");

        appenders.add(appenderA);
        appenders.add(appenderB1);
        appenders.add(appenderB2);

        LOG.init(context, mPackageName);

        List<String> appendersIds = analyticsManager.addAppenders(context, appenders);

        assertNotNull(appendersIds);
        assertEquals(2, appendersIds.size());
        assertEquals("A", appendersIds.get(0));
        assertEquals("B", appendersIds.get(1));
    }

    @Test
    public void testDisableAppendersNull() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();
        analyticsManager.disableAppenders(context, null);
    }

    @Test
    public void testDisableAppendersEmpty() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();
        analyticsManager.disableAppenders(context, new ArrayList<String>());
    }

    @Test
    public void testDisableAppenders() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        List<String> appendersIds = analyticsManager.addAppenders(context, appenders);

        analyticsManager.disableAppenders(context, appendersIds);
        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testDisableAllAppenders() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        analyticsManager.addAppenders(context, appenders);

        analyticsManager.disableAllAppenders();
        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testTrackEvent() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        analyticsManager.addAppenders(context, appenders);

        Map<String, Object> analyticsPayload = new HashMap<>();
        analyticsPayload.put("A", "A1");
        analyticsPayload.put("B", "B1");
        analyticsPayload.put("C", "C1");

        analyticsManager.trackEvent("test", analyticsPayload);

        verify(appenderA, times(1)).trackEvent("test", analyticsPayload);
        verify(appenderB, times(1)).trackEvent("test", analyticsPayload);
        verify(appenderC, times(1)).trackEvent("test", analyticsPayload);
    }

    @Test
    public void testTrackPageHit() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        analyticsManager.addAppenders(context, appenders);

        Map<String, Object> analyticsPayload = new HashMap<>();
        analyticsPayload.put("A", "A1");
        analyticsPayload.put("B", "B1");
        analyticsPayload.put("C", "C1");

        analyticsManager.trackPageHit("test", analyticsPayload);

        verify(appenderA, times(1)).trackPageHit("test", analyticsPayload);
        verify(appenderB, times(1)).trackPageHit("test", analyticsPayload);
        verify(appenderC, times(1)).trackPageHit("test", analyticsPayload);
    }

    private IAnalyticsAppender mockAppender(String analyticsId) {
        IAnalyticsAppender appender = mock(IAnalyticsAppender.class);

        when(appender.getAnalyticsId()).thenReturn(analyticsId);

        return appender;
    }
}
