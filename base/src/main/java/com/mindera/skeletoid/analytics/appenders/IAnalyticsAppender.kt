package com.mindera.skeletoid.analytics.appenders

import android.app.Activity
import android.content.Context
import android.os.Bundle

/**
 * Interface for Analytics appenders
 */
interface IAnalyticsAppender {
    /**
     * Enable analytics
     *
     * @param context Application context
     */
    fun enableAppender(context: Context)

    /**
     * Disable analytics.
     */
    fun disableAppender()

    /**
     * Track app event
     *
     * @param eventName        Event name
     * @param analyticsPayload Generic analytics payload
     */
    fun trackEvent(
        eventName: String,
        analyticsPayload: Map<String, Any>
    )

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
    fun trackPageHit(
        activity: Activity,
        screenName: String,
        screenClassOverride: String?
    )

    /**
     * Get Analytics id (it should be unique within AnalyticsAppenders)
     */
    val analyticsId: String

    /**
     * Sets the user ID
     *
     * @param userId ID of the user
     */
    fun setUserId(userId: String)

    /**
     * Sets a custom property of the user
     *
     * @param name  Property name
     * @param value Property value
     */
    fun setUserProperty(name: String, value: String)
}