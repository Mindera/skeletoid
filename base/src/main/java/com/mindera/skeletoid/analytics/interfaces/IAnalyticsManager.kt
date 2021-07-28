package com.mindera.skeletoid.analytics.interfaces

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.mindera.skeletoid.analytics.appenders.interfaces.IAnalyticsAppender

/**
 * Analytics interface
 */
interface IAnalyticsManager {
    /**
     * Enable analytics appenders
     *
     * @param context            Context
     * @param analyticsAppenders Log appenders to enable
     * @return Ids of the logs enabled by their order
     */
    fun addAppenders(context: Context, analyticsAppenders: List<IAnalyticsAppender>): Set<String>

    /**
     * Disable analytics appenders
     *
     * @param context      Context
     * @param analyticsIds Log ids of each of the analytics enabled by the order sent
     */
    fun removeAppenders(
        context: Context,
        analyticsIds: Set<String>
    )

    /**
     * Disable all analytics appenders
     */
    fun removeAllAppenders()

    /**
     * Track app event
     *
     * @param eventName        Event name
     * @param analyticsPayload Generic analytics payload
     */
    fun trackEvent(eventName: String, analyticsPayload: Map<String, Any>)

    /**
     * Track app event
     *
     * @param eventName        Event name
     * @param analyticsPayload Generic analytics payload
     */
    fun trackEvent(eventName: String, analyticsPayload: Bundle)

    /**
     * Track app page hit
     *
     * @param activity            Activity that represent
     * @param screenName          Screen name
     * @param screenClassOverride Screen class override name
     */
    fun trackPageHit(activity: Activity, screenName: String, screenClassOverride: String? = null)

    /**
     * Track app page hit manually
     *
     * @param screenClass         Screen class name
     * @param screenName          Screen name
     */
    fun trackPageHit(screenClass: String, screenName: String)

    /**
     * Sets the user ID
     *
     * @param userID ID of the user
     */
    fun setUserID(userID: String)

    /**
     * Sets a custom property of the user
     *
     * @param name  Property name
     * @param value Property value
     */
    fun setUserProperty(name: String, value: String?)
}