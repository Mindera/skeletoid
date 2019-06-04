package com.mindera.skeletoid.analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;
import com.mindera.skeletoid.logs.LOG;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnalyticsManagerUnitTest {

    private static final String mPackageName = "my.package.name";

    private Context mContext;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
    }

    @Test
    public void testAddAppendersNull() {
        AnalyticsManager analyticsManager = new AnalyticsManager();
        Set<String> appendersIds = analyticsManager.addAppenders(mContext, null);

        assertNotNull(appendersIds);
        assertEquals(0, appendersIds.size());
    }

    @Test
    public void testAddAppendersEmpty() {
        AnalyticsManager analyticsManager = new AnalyticsManager();
        Set<String> appendersIds = analyticsManager.addAppenders(mContext, new ArrayList<IAnalyticsAppender>());

        assertNotNull(appendersIds);
        assertEquals(0, appendersIds.size());
    }

    @Test
    public void testAddAppenders() {
        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Set<String> appendersIds = analyticsManager.addAppenders(mContext, appenders);

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
        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB1 = mockAppender("B");
        IAnalyticsAppender appenderB2 = mockAppender("B");

        appenders.add(appenderA);
        appenders.add(appenderB1);
        appenders.add(appenderB2);

        //We must initialize the LOG since it prints an error
        LOG.init(mContext, mPackageName);
        Set<String> appendersIds = analyticsManager.addAppenders(mContext, appenders);

        assertNotNull(appendersIds);
        assertEquals(2, appendersIds.size());
        assertTrue(appendersIds.contains("A"));
        assertTrue(appendersIds.contains("B"));
    }

    @Test
    public void testDisableAppendersNull() {
        AnalyticsManager analyticsManager = new AnalyticsManager();
        analyticsManager.removeAppenders(mContext, null);

        assertNotNull(analyticsManager);
    }

    @Test
    public void testDisableAppendersEmpty() {
        AnalyticsManager analyticsManager = new AnalyticsManager();
        analyticsManager.removeAppenders(mContext, new HashSet<String>());

        assertNotNull(analyticsManager);
    }

    @Test
    public void testDisableAppenders() {
        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Set<String> appendersIds = analyticsManager.addAppenders(mContext, appenders);

        analyticsManager.removeAppenders(mContext, appendersIds);
        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testDisableAllAppenders() {
        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        analyticsManager.addAppenders(mContext, appenders);

        analyticsManager.removeAllAppenders();
        verify(appenderA, times(1)).disableAppender();
        verify(appenderB, times(1)).disableAppender();
        verify(appenderC, times(1)).disableAppender();
    }

    @Test
    public void testTrackEvent() {
        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        analyticsManager.addAppenders(mContext, appenders);

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
    public void testTrackEventWithBundle() {
        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        analyticsManager.addAppenders(mContext, appenders);

        Bundle analyticsPayload = new Bundle();
        analyticsPayload.putString("A", "A1");
        analyticsPayload.putString("B", "B1");
        analyticsPayload.putString("C", "C1");

        analyticsManager.trackEvent("test", analyticsPayload);

        verify(appenderA, times(1)).trackEvent("test", analyticsPayload);
        verify(appenderB, times(1)).trackEvent("test", analyticsPayload);
        verify(appenderC, times(1)).trackEvent("test", analyticsPayload);
    }

    @Test
    public void testTrackPageHit() {
        Activity activity = mock(Activity.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        analyticsManager.addAppenders(mContext, appenders);

        analyticsManager.trackPageHit(activity, "test", "screen class");

        verify(appenderA, times(1)).trackPageHit(activity, "test", "screen class");
        verify(appenderB, times(1)).trackPageHit(activity, "test", "screen class");
        verify(appenderC, times(1)).trackPageHit(activity, "test", "screen class");
    }

    @Test
    public void testSetUserID() {
        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        analyticsManager.addAppenders(mContext, appenders);

        analyticsManager.setUserID("1234");

        verify(appenderA, times(1)).setUserId("1234");
        verify(appenderB, times(1)).setUserId("1234");
        verify(appenderC, times(1)).setUserId("1234");
    }

    @Test
    public void testSetUserProperty() {
        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        analyticsManager.addAppenders(mContext, appenders);

        analyticsManager.setUserProperty("name", "banana");
        analyticsManager.setUserProperty("age", "30");

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
