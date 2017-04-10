package com.mindera.skeletoid.analytics;

import android.content.Context;
import android.util.Log;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

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
    public List<String> addAppenders(Context context, List<IAnalyticsAppender> analyticsAppenders) {
        if (analyticsAppenders == null || analyticsAppenders.size() == 0) {
            return new ArrayList<>();
        }

        final List<String> appenderIds = new ArrayList<>();

        for (IAnalyticsAppender analyticsAppender : analyticsAppenders) {
            analyticsAppender.enableAppender(context);

            final String analyticsId = analyticsAppender.getAnalyticsId();

            if (mAnalyticsAppenders.containsKey(analyticsId)) {
                Log.e(LOG_TAG, "Appender ERROR: Adding appender with the same ID: " + analyticsId);
                mAnalyticsAppenders.remove(analyticsId).disableAppender();
            }

            appenderIds.add(analyticsId);
            mAnalyticsAppenders.put(analyticsId, analyticsAppender);
        }
        return appenderIds;
    }

    @Override
    public void disableAppenders(Context context, List<String> analyticsIds) {
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
    public void disableAllAppenders() {
        for (String analyticsId : mAnalyticsAppenders.keySet()) {
            final IAnalyticsAppender analyticsAppender = mAnalyticsAppenders.remove(analyticsId);
            if (analyticsAppender != null) {
                analyticsAppender.disableAppender();
            }
        }
    }

    @Override
    public void trackEvent(String screenName, Map<String, String> analyticsPayload) {
        for (Map.Entry<String, IAnalyticsAppender> entry : mAnalyticsAppenders.entrySet()) {
            entry.getValue().trackEvent(screenName, analyticsPayload);
        }
    }

    @Override
    public void trackPageHit(String screenName, Map<String, String> analyticsPayload) {
        for (Map.Entry<String, IAnalyticsAppender> entry : mAnalyticsAppenders.entrySet()) {
            entry.getValue().trackPageHit(screenName, analyticsPayload);
        }
    }
}
