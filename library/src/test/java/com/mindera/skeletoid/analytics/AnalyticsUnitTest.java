package com.mindera.skeletoid.analytics;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;
import com.mindera.skeletoid.logs.LOG;

import org.junit.After;
import org.junit.Test;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnalyticsUnitTest {

    private String mPackageName = "my.package.name";

    @After
    public void cleanUp() {
        Analytics.deinit(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new Analytics();
    }

    @Test
    public void testInit() {
        Analytics.init();
    }

    @Test
    public void testInitWithParams() {
        Context context = mock(Context.class);

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        //Must have logger initialized for this test
        LOG.init(context);
        Analytics.init(context, appenders);

        assertTrue(Analytics.isInitialized());
    }

    @Test
    public void testIsInitialized() {
        assertFalse(Analytics.isInitialized());
        Analytics.init();
        assertTrue(Analytics.isInitialized());
    }

    @Test
    public void testDeinit() {
        Context context = mock(Context.class);

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        //Must have logger initialized for this test
        LOG.init(context);
        Analytics.init(context, appenders);

        Analytics.deinit(context);

        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testAddAppenders() {
        Context context = mock(Context.class);

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Set<String> appendersIds = Analytics.init(context, appenders);

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

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB1 = mockAppender("B");
        IAnalyticsAppender appenderB2 = mockAppender("B");

        appenders.add(appenderA);
        appenders.add(appenderB1);
        appenders.add(appenderB2);

        LOG.init(context, mPackageName);
        Set<String> appendersIds = Analytics.init(context, appenders);

        assertNotNull(appendersIds);
        assertEquals(2, appendersIds.size());
        assertTrue(appendersIds.contains("A"));
        assertTrue(appendersIds.contains("B"));
    }

    @Test
    public void testDisableAppendersNull() {
        Context context = mock(Context.class);

        Analytics.removeAppenders(context, null);
    }

    @Test
    public void testDisableAppendersEmpty() {
        Context context = mock(Context.class);

        Analytics.removeAppenders(context, new HashSet<String>());
    }

    @Test
    public void testRemoveAppenders() {
        Context context = mock(Context.class);

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName);
        Set<String> appendersIds = Analytics.addAppenders(context, appenders);
        Analytics.removeAppenders(context, appendersIds);

        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testRemoveAllAppenders() {
        Context context = mock(Context.class);

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(context, mPackageName);
        Analytics.addAppenders(context, appenders);
        Analytics.removeAllAppenders();

        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testTrackEvent() {
        Context context = mock(Context.class);

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Analytics.init(context, appenders);

        Map<String, Object> analyticsPayload = new HashMap<>();
        analyticsPayload.put("A", "A1");
        analyticsPayload.put("B", "B1");
        analyticsPayload.put("C", "C1");

        Analytics.trackEvent("test", analyticsPayload);

        verify(appenderA, times(1)).trackEvent("test", analyticsPayload);
        verify(appenderB, times(1)).trackEvent("test", analyticsPayload);
        verify(appenderC, times(1)).trackEvent("test", analyticsPayload);
    }

    @Test
    public void testTrackPageHit() {
        Context context = mock(Context.class);

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Analytics.init(context, appenders);

        Map<String, Object> analyticsPayload = new HashMap<>();
        analyticsPayload.put("A", "A1");
        analyticsPayload.put("B", "B1");
        analyticsPayload.put("C", "C1");

        Analytics.trackPageHit("test", analyticsPayload);

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
