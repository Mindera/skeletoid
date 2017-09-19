package com.mindera.skeletoid.analytics.appenders;

import android.app.Activity;
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
    void trackEvent(String screenName, Map<String, String> analyticsPayload);

    /**
     * Track app page hit
     *
     * @param screenName       Screen name
     * @param analyticsPayload generic analytics payload
     */
    void trackPageHit(String screenName, Map<String, String> analyticsPayload);

    /**
     * Track app page hit
     *
     * @param activity            Activity that represent
     * @param screenName          Screen name
     * @param screenClassOverride Screen class override name
     * @param analyticsPayload    Generic analytics payload
     */
    void trackPageHit(Activity activity, String screenName, String screenClassOverride, Map<String, String> analyticsPayload);

    /**
     * Get Analytics id (it should be unique within AnalyticsAppenders)
     */
    String getAnalyticsId();
}
