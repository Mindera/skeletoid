package com.mindera.skeletoid.analytics.appenders

import android.app.Activity
import android.os.Bundle
import com.google.android.gms.analytics.Tracker
import com.google.android.gms.analytics.ecommerce.Product
import com.google.android.gms.analytics.ecommerce.ProductAction
import com.mindera.skeletoid.analytics.appenders.interfaces.IAnalyticsAppender
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

class GaAppenderUnitTest {

    private lateinit var appender: IAnalyticsAppender
    private lateinit var tracker: Tracker

    @Before
    fun setUp() {
        appender = GaAppender(0)
        tracker = mock(Tracker::class.java)
    }

    @Test
    fun testDisableNotEnabledAppender() {
        appender.disableAppender()

        assertNull((appender as GaAppender).tracker)
    }

    @Test
    fun testDisableAppender() {
        (appender as GaAppender).tracker = tracker
        appender.disableAppender()

        assertNull((appender as GaAppender).tracker)
    }

    @Test
    fun testTrackEvent() {
        val product = Product().setName("product_name")
        val screenName = "screenName"
        val payloadMap = HashMap<String, Any>().apply {
            put(GaAppender.CATEGORY, "category")
            put(GaAppender.ACTION, "action")
            put(GaAppender.LABEL, "label")
            put(GaAppender.PRODUCT, product)
            put(GaAppender.PRODUCT_ACTION, ProductAction("product_action"))
            put(GaAppender.VALUE, 2500L)
        }
        val expectedPayload = HashMap<String, String>().apply {
            put("&ea", "action")
            put("&ec", "category")
            put("&el", "label")
            put("&t", "event")
            put("&ev", "2500")
            put("&pa", "product_action")
            put("&pr1nm", "product_name")
        }
        (appender as GaAppender).tracker = tracker

        appender.trackEvent(screenName, payloadMap)

        assertNotNull((appender as GaAppender).tracker)
        verify(tracker)?.setScreenName(screenName)
        verify(tracker)?.send(expectedPayload)
    }

    @Test
    fun testNoTrackEventDisabledAppender() {
        val screenName = "screenName"
        val payloadMap = HashMap<String, Any>().apply {
            put(GaAppender.CATEGORY, "category")
            put(GaAppender.ACTION, "action")
            put(GaAppender.LABEL, "label")
            put(GaAppender.PRODUCT, Product())
            put(GaAppender.PRODUCT_ACTION, ProductAction("product_action"))
            put(GaAppender.VALUE, 2500L)
        }
        (appender as GaAppender).tracker = tracker

        appender.disableAppender()
        appender.trackEvent(screenName, payloadMap)

        verifyNoMoreInteractions(tracker)
    }

    @Test
    fun testTrackEventWithBundle() {
        val product = Product().setName("product_name")
        val eventName = "eventName"
        val payload = mock(Bundle::class.java)
        `when`(payload.containsKey(ArgumentMatchers.anyString())).thenReturn(true)
        `when`(payload.get(GaAppender.CATEGORY)).thenReturn("category")
        `when`(payload.get(GaAppender.ACTION)).thenReturn("action")
        `when`(payload.get(GaAppender.VALUE)).thenReturn(2500L)
        `when`(payload.get(GaAppender.LABEL)).thenReturn("label")
        `when`(payload.get(GaAppender.PRODUCT)).thenReturn(product)
        `when`(payload.get(GaAppender.PRODUCT_ACTION)).thenReturn(ProductAction("product_action"))
        val expectedPayload = HashMap<String, String>().apply {
            put("&ea", "action")
            put("&ec", "category")
            put("&el", "label")
            put("&t", "event")
            put("&ev", "2500")
            put("&pa", "product_action")
            put("&pr1nm", "product_name")
        }
        (appender as GaAppender).tracker = tracker

        appender.trackEvent(eventName, payload)

        assertNotNull((appender as GaAppender).tracker)
        verify(tracker)?.setReferrer(eventName)
        verify(tracker)?.send(expectedPayload)
    }

    @Test
    fun testNoTrackEventWithBundleDisabledAppender() {
        val eventName = "eventName"
        val payload = mock(Bundle::class.java)
        `when`(payload.containsKey(GaAppender.CATEGORY)).thenReturn(true)
        `when`(payload.containsKey(GaAppender.ACTION)).thenReturn(true)
        `when`(payload.containsKey(GaAppender.VALUE)).thenReturn(true)
        `when`(payload.get(GaAppender.CATEGORY)).thenReturn("category")
        `when`(payload.get(GaAppender.ACTION)).thenReturn("action")
        `when`(payload.get(GaAppender.LABEL)).thenReturn("action")
        `when`(payload.get(GaAppender.PRODUCT)).thenReturn(Product())
        `when`(payload.get(GaAppender.PRODUCT_ACTION)).thenReturn(ProductAction("product_action"))
        `when`(payload.get(GaAppender.VALUE)).thenReturn(2500L)
        (appender as GaAppender).tracker = tracker

        appender.disableAppender()
        appender.trackEvent(eventName, payload)

        verifyNoMoreInteractions(tracker)
    }

    @Test
    fun testTrackPageHit() {
        val activity = mock(Activity::class.java)
        val screenName = "screenName"
        val screenClassOverride = "com.mindera.skeletoid.ScreenName"
        val expectedPayload = HashMap<String, String>().apply {
            put("&t", "screenview")
        }
        (appender as GaAppender).tracker = tracker

        appender.trackPageHit(activity, screenName, screenClassOverride)

        verify(tracker).setScreenName(screenName)
        verify(tracker).send(expectedPayload)
    }

    @Test
    fun testTrackPageHitDisabledAppender() {
        val activity = mock(Activity::class.java)
        val screenName = "screenName"
        val screenClassOverride = "com.mindera.skeletoid.ScreenName"

        (appender as GaAppender).tracker = tracker

        appender.disableAppender()
        appender.trackPageHit(activity, screenName, screenClassOverride)

        verifyNoMoreInteractions(tracker)
    }

    @Test
    fun testGetAnalyticsId() {
        assertEquals("GoogleAnalytics", appender.analyticsId)
    }

    @Test
    fun testSetUserId() {
        val userId = "userId"
        (appender as GaAppender).tracker = tracker

        appender.setUserId(userId)

        verify(tracker).setClientId(userId)
    }

    @Test
    fun testSetUserIdDisabledAppender() {
        val userId = "userId"
        (appender as GaAppender).tracker = tracker

        appender.disableAppender()
        appender.setUserId(userId)

        verifyNoMoreInteractions(tracker)
    }
}

