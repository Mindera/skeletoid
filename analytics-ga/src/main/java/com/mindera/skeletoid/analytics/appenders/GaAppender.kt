package com.mindera.skeletoid.analytics.appenders

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.google.android.gms.analytics.ecommerce.Product
import com.google.android.gms.analytics.ecommerce.ProductAction
import com.google.android.gms.common.util.VisibleForTesting
import com.mindera.skeletoid.analytics.appenders.interfaces.IAnalyticsAppender
import com.mindera.skeletoid.logs.LOG

class GaAppender(private val configurationFileId: Int) : IAnalyticsAppender {

    companion object {
        private const val LOG_TAG = "GAAppender"
        const val CATEGORY = "CATEGORY"
        const val ACTION = "ACTION"
        const val LABEL = "LABEL"
        const val VALUE = "VALUE"
        const val PRODUCT = "PRODUCT"
        const val PRODUCT_ACTION = "PRODUCT_ACTION"
    }

    @VisibleForTesting
    var tracker: Tracker? = null

    override fun enableAppender(context: Context) {
        val analytics = GoogleAnalytics.getInstance(context)

        //This can be configured via XML or code. Since we need to maintain this code on the lib, it must
        // be via xml (dummy file) that will ne replaced by the apps real file
        // examples of both configurations are available: https://developers.google.com/analytics/devguides/collection/android/v4/advanced
        tracker = analytics.newTracker(configurationFileId)
    }

    override fun disableAppender() {
        tracker = null
    }

    override fun trackEvent(eventName: String, analyticsPayload: Map<String, Any>) {
        if (tracker == null) {
            LOG.e(LOG_TAG, "trackEvent failed: tracker is null")
            return
        }

        tracker?.run {
            setScreenName(eventName)
            send(parsePayload(analyticsPayload))
        }
    }

    override fun trackEvent(eventName: String, analyticsPayload: Bundle) {
        if (tracker == null) {
            LOG.e(LOG_TAG, "trackEvent failed: tracker is null")
            return
        }

        tracker?.run {
            setReferrer(eventName)
            send(parsePayload(analyticsPayload))
        }
    }

    override fun trackPageHit(activity: Activity, screenName: String, screenClassOverride: String?) {
        if (tracker == null) {
            LOG.e(LOG_TAG, "trackPageHit failed: tracker is null")
            return
        }

        tracker?.run {
            // Should we do something with screenClassOverride?
            setScreenName(screenName)
            send(HitBuilders.ScreenViewBuilder().build())
        }
    }

    private fun parsePayload(analyticsPayload: Map<String, Any>): Map<String, String> {
        val eventBuilder = HitBuilders.EventBuilder()

        //TODO Should this be protected, or crash in purpose to avoid mistakes on dev side?
        if (analyticsPayload.containsKey(CATEGORY)) {
            eventBuilder.setCategory(analyticsPayload[CATEGORY] as String?)
        }

        if (analyticsPayload.containsKey(ACTION)) {
            eventBuilder.setAction(analyticsPayload[ACTION] as String?)
        }
        if (analyticsPayload.containsKey(LABEL)) {
            eventBuilder.setLabel(analyticsPayload[LABEL] as String?)
        }

        if (analyticsPayload.containsKey(VALUE)) {
            eventBuilder.setValue((analyticsPayload[VALUE] as Long?)!!)
        }

        if (analyticsPayload.containsKey(PRODUCT)) {
            eventBuilder.addProduct(analyticsPayload[PRODUCT] as Product?)
        }

        if (analyticsPayload.containsKey(PRODUCT_ACTION)) {
            eventBuilder.setProductAction(analyticsPayload[PRODUCT_ACTION] as ProductAction?)
        }

        return eventBuilder.build()
    }

    private fun parsePayload(analyticsPayload: Bundle): Map<String, String> {
        val eventBuilder = HitBuilders.EventBuilder()

        //TODO Should this be protected, or crash in purpose to avoid mistakes on dev side?
        if (analyticsPayload.containsKey(CATEGORY)) {
            eventBuilder.setCategory(analyticsPayload.get(CATEGORY) as String)
        }

        if (analyticsPayload.containsKey(ACTION)) {
            eventBuilder.setAction(analyticsPayload.get(ACTION) as String)
        }
        if (analyticsPayload.containsKey(LABEL)) {
            eventBuilder.setLabel(analyticsPayload.get(LABEL) as String)
        }

        if (analyticsPayload.containsKey(VALUE)) {
            eventBuilder.setValue(analyticsPayload.get(VALUE) as Long)
        }

        if (analyticsPayload.containsKey(PRODUCT)) {
            eventBuilder.addProduct(analyticsPayload.get(PRODUCT) as Product)
        }

        if (analyticsPayload.containsKey(PRODUCT_ACTION)) {
            eventBuilder.setProductAction(analyticsPayload.get(PRODUCT_ACTION) as ProductAction)
        }

        return eventBuilder.build()
    }

    override val analyticsId: String = "GoogleAnalytics"

    override fun setUserId(userId: String) {
        tracker?.setClientId(userId)
    }

    override fun setUserProperty(name: String, value: String?) {
        //TODO How can this be added?
    }
}
