package com.mindera.skeletoid.analytics;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;

import org.junit.After;
import org.junit.Test;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnalyticsUnitTest {

    @After
    public void cleanUp() {
        Analytics.deinit(null);
    }

    @Test
    public void testInit() {
        Context context = mock(Context.class);

        List<IAnalyticsAppender> appenders = new ArrayList<>();

        IAnalyticsAppender appenderA = mockAppender("A");
        IAnalyticsAppender appenderB = mockAppender("B");
        IAnalyticsAppender appenderC = mockAppender("C");

        appenders.add(appenderA);
        appenders.add(appenderB);
        appenders.add(appenderC);

        Analytics.init(context, appenders);

        verify(appenderA, times(1)).enableAppender(context);
        verify(appenderB, times(1)).enableAppender(context);
        verify(appenderC, times(1)).enableAppender(context);
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

        Analytics.init(context, appenders);

        Analytics.deinit(context);

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
