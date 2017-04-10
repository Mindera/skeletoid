package com.mindera.skeletoid.analytics;

import android.content.Context;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;

import java.util.List;
import java.util.Map;

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
    List<String> addAppenders(Context context, List<IAnalyticsAppender> analyticsAppenders);


    /**
     * Disable analytics appenders
     *
     * @param context   Context
     * @param loggerIds Log ids of each of the analytics enabled by the order sent
     */
    void disableAppenders(Context context, List<String> loggerIds);

    /**
     * Disable all analytics appenders
     */
    void disableAllAppenders();

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
}
