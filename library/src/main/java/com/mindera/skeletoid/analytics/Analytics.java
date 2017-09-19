package com.mindera.skeletoid.analytics;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Analytics {

//    private static final String TAG = "Analytics";

    private static volatile IAnalyticsManager mInstance;

    @VisibleForTesting
    Analytics() {
        throw new UnsupportedOperationException();
    }

    /**
     * Init the analytics engine. This method MUST be called before using Analytics
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
    public static Set<String> init(Context context, List<IAnalyticsAppender> analyticsAppenders) {
        return getInstance().addAppenders(context, analyticsAppenders);
    }

    /**
     * Deinit the analytics engine.
     * This method MUST be called if the Analytics engine is not needed any longer on the app
     */
    public static void deinit(Context context) {
        if (mInstance != null) {
            getInstance().removeAllAppenders();
            mInstance = null;
        }
    }

    /**
     * Obtain a instance of the analytics to guarantee it's unique
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
     * Enable analytics appenders
     *
     * @param context            Context
     * @param analyticsAppenders Analytics appenders to enable
     * @return Ids of the analytics appenders enabled by their order
     */
    public static Set<String> addAppenders(Context context, List<IAnalyticsAppender> analyticsAppenders) {
        return getInstance().addAppenders(context, analyticsAppenders);
    }

    /**
     * Disable analytics appenders
     *
     * @param context      Context
     * @param analyticsIds Analytics ids of each of the analytics appenders disabled by the order sent
     */
    public static void removeAppenders(Context context, Set<String> analyticsIds) {
        getInstance().removeAppenders(context, analyticsIds);
    }

    /**
     * Remove all analytics appenders
     */
    public static void removeAllAppenders() {
        getInstance().removeAllAppenders();
    }

    /**
     * Track Event method - Analytics generic method to send an event with a payload
     *
     * @param screenName       Name of screen
     * @param analyticsPayload Generic analytics payload
     */
    public static void trackEvent(String screenName, Map<String, String> analyticsPayload) {
        getInstance().trackEvent(screenName, analyticsPayload);
    }

    /**
     * Track Page Hits - Analytics generic method to track page hits
     *
     * @param activity            Activity that represent
     * @param screenName          Name of screen
     * @param screenClassOverride Screen name class override
     * @param analyticsPayload    Generic analytics payload
     */
    public static void trackPageHit(Activity activity, String screenName, String screenClassOverride, Map<String, String> analyticsPayload) {
        getInstance().trackPageHit(activity, screenName, screenClassOverride, analyticsPayload);
    }

    /**
     * Check if the analytics service is initialized
     *
     * @return true if initialized
     */
    public static boolean isInitialized() {
        return mInstance != null;
    }

}
