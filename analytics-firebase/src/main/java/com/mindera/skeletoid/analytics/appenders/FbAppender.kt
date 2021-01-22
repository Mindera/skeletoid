package com.mindera.skeletoid.analytics.appenders

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.annotation.VisibleForTesting

import com.google.firebase.analytics.FirebaseAnalytics
import com.mindera.skeletoid.analytics.appenders.interfaces.IAnalyticsAppender
import com.mindera.skeletoid.logs.LOG

class FbAppender : IAnalyticsAppender {

    companion object {
        private const val LOG_TAG = "FbAppender"
    }

    @VisibleForTesting
    var firebaseAnalytics: FirebaseAnalytics? = null

    override fun enableAppender(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    override fun disableAppender() {
        firebaseAnalytics = null
    }

    override fun trackEvent(eventName: String, analyticsPayload: Map<String, Any>) {
        if (firebaseAnalytics == null) {
            LOG.e(LOG_TAG, "trackEvent failed: firebaseAnalytics is null")
            return
        }

        firebaseAnalytics?.logEvent(eventName, mapToBundle(analyticsPayload))
    }

    override fun trackEvent(eventName: String, analyticsPayload: Bundle) {
        if (firebaseAnalytics == null) {
            LOG.e(LOG_TAG, "trackEvent failed: firebaseAnalytics is null")
            return
        }

        firebaseAnalytics?.logEvent(eventName, analyticsPayload)
    }

    override fun trackPageHit(activity: Activity, screenName: String, screenClassOverride: String?) {
        if (firebaseAnalytics == null) {
            LOG.e(LOG_TAG, "trackPageHit failed: firebaseAnalytics is null")
            return
        }

        firebaseAnalytics?.setCurrentScreen(activity, screenName, screenClassOverride)
    }

    override fun setUserId(userId: String) {
        if (firebaseAnalytics == null) {
            LOG.e(LOG_TAG, "setUserId failed: firebaseAnalytics is null")
            return
        }

        firebaseAnalytics?.setUserId(userId)
    }

    override fun setUserProperty(name: String, value: String?) {
        if (firebaseAnalytics == null) {
            LOG.e(LOG_TAG, "setUserProperty failed: firebaseAnalytics is null")
            return
        }

        firebaseAnalytics?.setUserProperty(name, value)
    }

    override val analyticsId: String  = "FirebaseAnalytics"

    private fun mapToBundle(map: Map<String, Any>): Bundle {
        return Bundle().apply {
            for ((key, value) in map) {
                putString(key, value.toString())
            }
        }
    }
}
