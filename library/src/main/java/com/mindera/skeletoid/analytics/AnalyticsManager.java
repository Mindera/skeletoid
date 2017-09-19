package com.mindera.skeletoid.analytics;

import android.app.Activity;
import android.content.Context;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;
import com.mindera.skeletoid.logs.LOG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnalyticsManager implements IAnalyticsManager {

    private static final String LOG_TAG = "AnalyticsManager";

    /**
     * List of appenders (it can be improved to an ArrayMap if we want to add the support lib as dependency
     */
    private Map<String, IAnalyticsAppender> mAnalyticsAppenders;

    AnalyticsManager() {
        mAnalyticsAppenders = new HashMap<>();
    }

    @Override
    public Set<String> addAppenders(Context context, List<IAnalyticsAppender> analyticsAppenders) {
        if (analyticsAppenders == null || analyticsAppenders.size() == 0) {
            return new HashSet<>();
        }

        final Set<String> appenderIds = new HashSet<>();

        for (IAnalyticsAppender analyticsAppender : analyticsAppenders) {
            analyticsAppender.enableAppender(context);

            final String analyticsId = analyticsAppender.getAnalyticsId();

            if (mAnalyticsAppenders.containsKey(analyticsId)) {
                IAnalyticsAppender oldAnalyticsAppender = mAnalyticsAppenders.remove(analyticsId);
                oldAnalyticsAppender.disableAppender();
                LOG.e(LOG_TAG, "Replacing Analytics Appender with ID: " + analyticsId);
            }

            appenderIds.add(analyticsId);
            mAnalyticsAppenders.put(analyticsId, analyticsAppender);
        }
        return appenderIds;
    }

    @Override
    public void removeAppenders(Context context, Set<String> analyticsIds) {
        if (analyticsIds == null || mAnalyticsAppenders.isEmpty()) {
            return;
        }

        for (String analyticsId : analyticsIds) {
            final IAnalyticsAppender analyticsAppender = mAnalyticsAppenders.remove(analyticsId);
            if (analyticsAppender != null) {
                analyticsAppender.disableAppender();
            }
        }
    }

    @Override
    public void removeAllAppenders() {
        List<String> appendersKeys = new ArrayList<>(mAnalyticsAppenders.keySet());
        for (String analyticsId : appendersKeys) {
            final IAnalyticsAppender analyticsAppender = mAnalyticsAppenders.remove(analyticsId);
            if (analyticsAppender != null) {
                analyticsAppender.disableAppender();
            }
        }
    }

    @Override
    public void trackEvent(String eventName, Map<String, String> analyticsPayload) {
        for (IAnalyticsAppender appender : mAnalyticsAppenders.values()) {
            appender.trackEvent(eventName, analyticsPayload);
        }
    }

    @Override
    public void trackPageHit(Activity activity, String screenName, String screenClassOverride, Map<String, String> analyticsPayload) {
        for (IAnalyticsAppender appender : mAnalyticsAppenders.values()) {
            appender.trackPageHit(activity, screenName, screenClassOverride, analyticsPayload);
        }
    }

    @Override
    public void setUserID(String userID) {
        for (IAnalyticsAppender appender : mAnalyticsAppenders.values()) {
            appender.setUserID(userID);
        }
    }

    @Override
    public void setUserProperty(String name, String value) {
        for (IAnalyticsAppender appender : mAnalyticsAppenders.values()) {
            appender.setUserProperty(name, value);
        }
    }
}
