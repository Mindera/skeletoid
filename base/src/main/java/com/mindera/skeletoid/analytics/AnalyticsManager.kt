package com.mindera.skeletoid.analytics

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender
import com.mindera.skeletoid.logs.LOG
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

class AnalyticsManager internal constructor() : IAnalyticsManager {

    companion object {
        private const val LOG_TAG = "AnalyticsManager"
    }

    /**
     * List of appenders (it can be improved to an ArrayMap if we want to add the support lib as dependency
     */
    private val analyticsAppenders: MutableMap<String, IAnalyticsAppender> = HashMap()

    override fun addAppenders(
        context: Context,
        analyticsAppenders: List<IAnalyticsAppender>
    ): Set<String> {
        val appenderIds: MutableSet<String> = HashSet()
        for (analyticsAppender in analyticsAppenders) {
            analyticsAppender.enableAppender(context)
            val analyticsId = analyticsAppender.analyticsId
            if (this.analyticsAppenders.containsKey(analyticsId)) {
                val oldAnalyticsAppender = this.analyticsAppenders.remove(analyticsId)
                oldAnalyticsAppender!!.disableAppender()
                LOG.e(LOG_TAG, "Replacing Analytics Appender with ID: $analyticsId")
            }
            appenderIds.add(analyticsId)
            this.analyticsAppenders[analyticsId] = analyticsAppender
        }
        return appenderIds
    }

    override fun removeAppenders(
        context: Context,
        analyticsIds: Set<String>
    ) {
        for (analyticsId in analyticsIds) {
            val analyticsAppender = analyticsAppenders.remove(analyticsId)
            analyticsAppender?.disableAppender()
        }
    }

    override fun removeAllAppenders() {
        val appendersKeys: List<String> = ArrayList(analyticsAppenders.keys)
        for (analyticsId in appendersKeys) {
            val analyticsAppender = analyticsAppenders.remove(analyticsId)
            analyticsAppender?.disableAppender()
        }
    }

    override fun trackEvent(
        eventName: String,
        analyticsPayload: Map<String, Any>
    ) {
        for (appender in analyticsAppenders.values) {
            appender.trackEvent(eventName, analyticsPayload)
        }
    }

    override fun trackEvent(
        eventName: String,
        analyticsPayload: Bundle
    ) {
        for (appender in analyticsAppenders.values) {
            appender.trackEvent(eventName, analyticsPayload)
        }
    }

    override fun trackPageHit(
        activity: Activity,
        screenName: String,
        screenClassOverride: String
    ) {
        for (appender in analyticsAppenders.values) {
            appender.trackPageHit(activity, screenName, screenClassOverride)
        }
    }

    override fun setUserID(userID: String) {
        for (appender in analyticsAppenders.values) {
            appender.setUserId(userID)
        }
    }

    override fun setUserProperty(name: String, value: String) {
        for (appender in analyticsAppenders.values) {
            appender.setUserProperty(name, value)
        }
    }
}