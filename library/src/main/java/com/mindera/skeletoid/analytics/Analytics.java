package com.mindera.skeletoid.analytics;

import android.content.Context;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;

import java.util.List;
import java.util.Map;


public class Analytics {

    private static final String TAG = "Analytics";

    private static volatile IAnalyticsManager mInstance;

    /**
     * Init the analytics engine. This method MUST be called before using Analytics
     *
     */
    public static void init() {
        getInstance();
    }

    /**
     * Init the analytics engine. This method MUST be called before using Analytics
     *
     * @param context            Context app
     * @param analyticsAppenders The analytics appenders to be started
     */
    public static List<String> init(Context context, List<IAnalyticsAppender> analyticsAppenders) {
        return getInstance().addAppenders(context, analyticsAppenders);
    }

    /**
     * Deinit the analytics engine.
     * This method MUST be called if the Analytics engine is not needed any longer on the app
     */
    public static void deinit(Context context) {
        if (mInstance != null) {
            getInstance().disableAllAppenders();
            mInstance = null;
        }
    }

    /**
     * Obtain a instance of the analytics to guarantee it's unique
     *
     * @return
     */
    private static IAnalyticsManager getInstance() {
        IAnalyticsManager result = mInstance;
        if (result == null) {
            synchronized (Analytics.class) {
                result = mInstance;
                if (result == null) {
                    mInstance = new AnalyticsManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * Track Event method - Analytics generic method to send an event with a payload
     * @param screenName
     * @param analyticsPayload
     */
    public static void trackEvent(String screenName, Map<String, Object> analyticsPayload) {
        getInstance().trackEvent(screenName, analyticsPayload);
    }

    /**
     * Track Page Hits - Analytics generic method to track page hits
     * @param screenName
     * @param analyticsPayload
     */
    public static void trackPageHit(String screenName, Map<String, Object> analyticsPayload) {
        getInstance().trackPageHit(screenName, analyticsPayload);
    }
}
