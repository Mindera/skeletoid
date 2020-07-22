package com.mindera.skeletoid.analytics

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender

object Analytics {

    private var instance: IAnalyticsManager? = null

    /**
     * Init the analytics engine. This method MUST be called before using Analytics
     *
     * @param context            Context app
     * @param analyticsAppenders The analytics appenders to be started
     */
    fun init(
        context: Context,
        analyticsAppenders: List<IAnalyticsAppender> = emptyList()
    ): Set<String> {
        return getInstance().addAppenders(context, analyticsAppenders)
    }

    /**
     * Deinit the analytics engine.
     * This method MUST be called if the Analytics engine is not needed any longer on the app
     */
    fun deinit() {
        instance?.removeAllAppenders()
        instance = null
    }

    private fun getInstance(): IAnalyticsManager {

        instance ?: synchronized(Analytics.javaClass) {
            instance = AnalyticsManager()
        }
        return instance!!
    }

    /**
     * Enable analytics appenders
     *
     * @param context            Context
     * @param analyticsAppenders Analytics appenders to enable
     * @return Ids of the analytics appenders enabled by their order
     */
    fun addAppenders(
        context: Context,
        analyticsAppenders: List<IAnalyticsAppender>
    ): Set<String> {
        return instance?.addAppenders(context, analyticsAppenders)
            ?: throw UninitializedPropertyAccessException("Please call init() before using")
    }

    /**
     * Disable analytics appenders
     *
     * @param context      Context
     * @param analyticsIds Analytics ids of each of the analytics appenders disabled by the order sent
     */
    fun removeAppenders(
        context: Context,
        analyticsIds: Set<String>
    ) {
        instance?.removeAppenders(context, analyticsIds)
    }

    /**
     * Remove all analytics appenders
     */
    fun removeAllAppenders() {
        instance?.removeAllAppenders()
    }

    /**
     * Track Event method - Analytics generic method to send an event with a payload
     *
     * @param eventName        Event name
     * @param analyticsPayload Generic analytics payload
     */
    fun trackEvent(
        eventName: String,
        analyticsPayload: Map<String, Any>
    ) {
        instance?.trackEvent(eventName, analyticsPayload)
    }

    /**
     * Track Event method - Analytics generic method to send an event with a payload
     *
     * @param eventName        Event name
     * @param analyticsPayload Generic analytics payload
     */
    fun trackEvent(eventName: String, analyticsPayload: Bundle) {
        instance?.trackEvent(eventName, analyticsPayload)
    }

    /**
     * Track Page Hits - Analytics generic method to track page hits
     *
     * @param activity            Activity that represent
     * @param screenName          Name of screen
     * @param screenClassOverride Screen name class override
     */
    fun trackPageHit(
        activity: Activity,
        screenName: String,
        screenClassOverride: String? = null
    ) {
        instance?.trackPageHit(activity, screenName, screenClassOverride)
    }

    /**
     * Sets the user ID
     *
     * @param userID ID of the user
     */
    fun setUserID(userID: String) {
        instance?.setUserID(userID)
    }

    /**
     * Sets a custom property of the user
     *
     * @param name  Property name
     * @param value Property value
     */
    fun setUserProperty(name: String, value: String) {
        instance?.setUserProperty(name, value)
    }

    /**
     * Check if the analytics service is initialized
     *
     * @return true if initialized
     */
    val isInitialized: Boolean
        get() = instance != null

}