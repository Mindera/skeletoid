package com.mindera.skeletoid.analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;
import com.mindera.skeletoid.logs.LOG;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

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

    private Context mContext;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
    }

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
        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        //Must have logger initialized for this test
        LOG.init(mContext);
        Analytics.init(mContext, appenders);

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
        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        //Must have logger initialized for this test
        LOG.init(mContext);
        Analytics.init(mContext, appenders);

        Analytics.deinit(mContext);

        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testAddAppenders() {
        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Set<String> appendersIds = Analytics.init(mContext, appenders);

        verify(appenderA, times(1)).enableAppender(mContext);
        verify(appenderB, times(1)).enableAppender(mContext);
        verify(appenderC, times(1)).enableAppender(mContext);

        assertNotNull(appendersIds);
        assertEquals(3, appendersIds.size());
        assertTrue(appendersIds.contains("A"));
        assertTrue(appendersIds.contains("B"));
        assertTrue(appendersIds.contains("C"));
    }

    @Test
    public void testAddAppendersRepeated() {
        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB1 = mockAppender("B");
        IAnalyticsAppender appenderB2 = mockAppender("B");

        appenders.add(appenderA);
        appenders.add(appenderB1);
        appenders.add(appenderB2);

        LOG.init(mContext, mPackageName);
        Set<String> appendersIds = Analytics.init(mContext, appenders);

        assertNotNull(appendersIds);
        assertEquals(2, appendersIds.size());
        assertTrue(appendersIds.contains("A"));
        assertTrue(appendersIds.contains("B"));
    }

    @Test
    public void testDisableAppendersNull() {
        Analytics.removeAppenders(mContext, null);
    }

    @Test
    public void testDisableAppendersEmpty() {
        Analytics.removeAppenders(mContext, new HashSet<String>());
    }

    @Test
    public void testRemoveAppenders() {
        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(mContext, mPackageName);
        Set<String> appendersIds = Analytics.addAppenders(mContext, appenders);
        Analytics.removeAppenders(mContext, appendersIds);

        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testRemoveAllAppenders() {
        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        LOG.init(mContext, mPackageName);
        Analytics.addAppenders(mContext, appenders);
        Analytics.removeAllAppenders();

        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testTrackEvent() {
        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Analytics.init(mContext, appenders);

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
    public void testTrackEventWithBundle() {
        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Analytics.init(mContext, appenders);

        Bundle analyticsPayload = new Bundle();
        analyticsPayload.putString("A", "A1");
        analyticsPayload.putString("B", "B1");
        analyticsPayload.putString("C", "C1");

        Analytics.trackEvent("test", analyticsPayload);

        verify(appenderA, times(1)).trackEvent("test", analyticsPayload);
        verify(appenderB, times(1)).trackEvent("test", analyticsPayload);
        verify(appenderC, times(1)).trackEvent("test", analyticsPayload);
    }

    @Test
    public void testTrackPageHit() {
        Activity activity = mock(Activity.class);

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Analytics.init(mContext, appenders);

        Analytics.trackPageHit(activity, "test", "screen class");

        verify(appenderA, times(1)).trackPageHit(activity, "test", "screen class");
        verify(appenderB, times(1)).trackPageHit(activity, "test", "screen class");
        verify(appenderC, times(1)).trackPageHit(activity, "test", "screen class");
    }

    @Test
    public void testSetUserID() {
        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Analytics.init(mContext, appenders);

        Analytics.setUserID("1234");

        verify(appenderA, times(1)).setUserID("1234");
        verify(appenderB, times(1)).setUserID("1234");
        verify(appenderC, times(1)).setUserID("1234");
    }

    @Test
    public void testSetUserProperty() {
        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Analytics.init(mContext, appenders);

        Analytics.setUserProperty("name", "banana");
        Analytics.setUserProperty("age", "30");

        verify(appenderA, times(1)).setUserProperty("name", "banana");
        verify(appenderB, times(1)).setUserProperty("name", "banana");
        verify(appenderC, times(1)).setUserProperty("name", "banana");

        verify(appenderA, times(1)).setUserProperty("age", "30");
        verify(appenderB, times(1)).setUserProperty("age", "30");
        verify(appenderC, times(1)).setUserProperty("age", "30");
    }

    private IAnalyticsAppender mockAppender(String analyticsId) {
        IAnalyticsAppender appender = mock(IAnalyticsAppender.class);

        when(appender.getAnalyticsId()).thenReturn(analyticsId);

        return appender;
    }
}
