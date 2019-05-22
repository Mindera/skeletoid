package com.mindera.skeletoid.analytics.appenders

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.annotation.VisibleForTesting

import com.google.firebase.analytics.FirebaseAnalytics
import com.mindera.skeletoid.logs.LOG

class FbAppender : IAnalyticsAppender {

    companion object {
        private val LOG_TAG = "FBAppender"
    }

    @VisibleForTesting
    var firebaseAnalytics: FirebaseAnalytics? = null

    override fun enableAppender(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    override fun disableAppender() {
        firebaseAnalytics = null
    }

    override fun trackEvent(eventName: String, map: Map<String, Any>) {
        if (firebaseAnalytics == null) {
            LOG.e(LOG_TAG, "trackEvent failed: firebaseAnalytics is null")
            return
        }

        firebaseAnalytics?.logEvent(eventName, mapToBundle(map))
    }

    override fun trackEvent(eventName: String, bundle: Bundle) {
        if (firebaseAnalytics == null) {
            LOG.e(LOG_TAG, "trackEvent failed: firebaseAnalytics is null")
            return
        }

        firebaseAnalytics?.logEvent(eventName, bundle)
    }

    override fun trackPageHit(activity: Activity, screenName: String, screenClassOverload: String) {
        if (firebaseAnalytics == null) {
            LOG.e(LOG_TAG, "trackPageHit failed: firebaseAnalytics is null")
            return
        }

        firebaseAnalytics?.setCurrentScreen(activity, screenName, screenClassOverload)
    }

    override fun setUserID(userID: String) {
        if (firebaseAnalytics == null) {
            LOG.e(LOG_TAG, "setUserID failed: firebaseAnalytics is null")
            return
        }

        firebaseAnalytics?.setUserId(userID)
    }

    override fun setUserProperty(name: String, value: String) {
        if (firebaseAnalytics == null) {
            LOG.e(LOG_TAG, "setUserProperty failed: firebaseAnalytics is null")
            return
        }

        firebaseAnalytics?.setUserProperty(name, value)
    }

    override fun getAnalyticsId(): String {
        return "FirebaseAnalytics"
    }

    private fun mapToBundle(map: Map<String, Any>): Bundle {
        return Bundle().apply {
            for ((key, value) in map) {
                putString(key, value.toString())
            }
        }
    }
}
