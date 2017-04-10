package com.mindera.skeletoid.analytics.appenders;


import android.content.Context;

import java.util.Map;

/**
 * Interface for Analytics appenders
 */
public interface IAnalyticsAppender {

    /**
     * Enable analytics
     *
     * @param context Application context
     */
    void enableAppender(Context context);

    /**
     * Disable analytics.
     */
    void disableAppender();

    /**
     * Track app event
     *
     * @param screenName       Screen name
     * @param analyticsPayload generic analytics payload
     */
    void trackEvent(String screenName, Map<String, Object> analyticsPayload);

    /**
     * Track app page hit
     *
     * @param screenName       Screen name
     * @param analyticsPayload generic analytics payload
     */
    void trackPageHit(String screenName, Map<String, Object> analyticsPayload);

    /**
     * Get Analytics id (it should be unique within AnalyticsAppenders)
     */
    String getAnalyticsId();
}
