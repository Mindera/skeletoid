package com.mindera.skeletoid.analytics;

import android.app.Activity;
import android.content.Context;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Analytics interface
 */
public interface IAnalyticsManager {

    /**
     * Enable analytics appenders
     *
     * @param context            Context
     * @param analyticsAppenders Log appenders to enable
     * @return Ids of the logs enabled by their order
     */
    Set<String> addAppenders(Context context, List<IAnalyticsAppender> analyticsAppenders);

    /**
     * Disable analytics appenders
     *
     * @param context      Context
     * @param analyticsIds Log ids of each of the analytics enabled by the order sent
     */
    void removeAppenders(Context context, Set<String> analyticsIds);

    /**
     * Disable all analytics appenders
     */
    void removeAllAppenders();

    /**
     * Track app event
     *
     * @param eventName        Event name
     * @param analyticsPayload Generic analytics payload
     */
    void trackEvent(String eventName, Map<String, Object> analyticsPayload);

    /**
     * Track app page hit
     *
     * @param activity            Activity that represent
     * @param screenName          Screen name
     * @param screenClassOverride Screen class override name
     */
    void trackPageHit(Activity activity, String screenName, String screenClassOverride);

    /**
     * Sets the user ID
     *
     * @param userID ID of the user
     */
    void setUserID(String userID);

    /**
     * Sets a custom property of the user
     *
     * @param name  Property name
     * @param value Property value
     */
    void setUserProperty(String name, String value);

}
