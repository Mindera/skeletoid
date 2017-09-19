package com.mindera.skeletoid.analytics;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;
import com.mindera.skeletoid.logs.LOG;

import org.junit.Test;

import android.app.Activity;
import android.content.Context;

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

    private String mPackageName = "my.package.name";

    @Test
    public void testAddAppendersNull() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();
        Set<String> appendersIds = analyticsManager.addAppenders(context, null);

        assertNotNull(appendersIds);
        assertEquals(0, appendersIds.size());
    }

    @Test
    public void testAddAppendersEmpty() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();
        Set<String> appendersIds = analyticsManager.addAppenders(context, new ArrayList<IAnalyticsAppender>());

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

        Set<String> appendersIds = analyticsManager.addAppenders(context, appenders);

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

        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB1 = mockAppender("B");
        IAnalyticsAppender appenderB2 = mockAppender("B");

        appenders.add(appenderA);
        appenders.add(appenderB1);
        appenders.add(appenderB2);

        //We must initialize the LOG since it prints an error
        LOG.init(context, mPackageName);
        Set<String> appendersIds = analyticsManager.addAppenders(context, appenders);

        assertNotNull(appendersIds);
        assertEquals(2, appendersIds.size());
        assertTrue(appendersIds.contains("A"));
        assertTrue(appendersIds.contains("B"));
    }

    @Test
    public void testDisableAppendersNull() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();
        analyticsManager.removeAppenders(context, null);
    }

    @Test
    public void testDisableAppendersEmpty() {
        Context context = mock(Context.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();
        analyticsManager.removeAppenders(context, new HashSet<String>());
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

        Set<String> appendersIds = analyticsManager.addAppenders(context, appenders);

        analyticsManager.removeAppenders(context, appendersIds);
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

        analyticsManager.removeAllAppenders();
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

        Map<String, String> analyticsPayload = new HashMap<>();
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

        Map<String, String> analyticsPayload = new HashMap<>();
        analyticsPayload.put("A", "A1");
        analyticsPayload.put("B", "B1");
        analyticsPayload.put("C", "C1");

        analyticsManager.trackPageHit("test", analyticsPayload);

        verify(appenderA, times(1)).trackPageHit("test", analyticsPayload);
        verify(appenderB, times(1)).trackPageHit("test", analyticsPayload);
        verify(appenderC, times(1)).trackPageHit("test", analyticsPayload);
    }

    @Test
    public void testTrackPageHitOverload() {
        Context context = mock(Context.class);
        Activity activity = mock(Activity.class);

        AnalyticsManager analyticsManager = new AnalyticsManager();

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        analyticsManager.addAppenders(context, appenders);

        Map<String, String> analyticsPayload = new HashMap<>();
        analyticsPayload.put("A", "A1");
        analyticsPayload.put("B", "B1");
        analyticsPayload.put("C", "C1");

        analyticsManager.trackPageHit(activity, "test", "screen class", analyticsPayload);

        verify(appenderA, times(1)).trackPageHit(activity, "test", "screen class", analyticsPayload);
        verify(appenderB, times(1)).trackPageHit(activity, "test", "screen class", analyticsPayload);
        verify(appenderC, times(1)).trackPageHit(activity, "test", "screen class", analyticsPayload);
    }

    private IAnalyticsAppender mockAppender(String analyticsId) {
        IAnalyticsAppender appender = mock(IAnalyticsAppender.class);

        when(appender.getAnalyticsId()).thenReturn(analyticsId);

        return appender;
    }
}
