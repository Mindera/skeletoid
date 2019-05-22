package com.mindera.skeletoid.analytics.appenders

import android.app.Activity
import android.os.Bundle
import com.google.android.gms.analytics.Tracker
import com.google.android.gms.analytics.ecommerce.Product
import com.google.android.gms.analytics.ecommerce.ProductAction
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

class GAAppenderUnitTest {

    private lateinit var appender: GAAppender
    private lateinit var tracker: Tracker

    @Before
    fun setUp() {
        appender = GAAppender(0)
        tracker = mock(Tracker::class.java)
    }

    @Test
    fun testDisableNotEnabledAppender() {
        appender.disableAppender()

        assertNull(appender.tracker)
    }

    @Test
    fun testDisableAppender() {
        appender.tracker = tracker
        appender.disableAppender()

        assertNull(appender.tracker)
    }

    @Test
    fun testTrackEvent() {
        val product = Product().setName("product_name")
        val screenName = "screenName"
        val payloadMap = HashMap<String, Any>().apply {
            put(GAAppender.CATEGORY, "category")
            put(GAAppender.ACTION, "action")
            put(GAAppender.LABEL, "label")
            put(GAAppender.PRODUCT, product)
            put(GAAppender.PRODUCT_ACTION, ProductAction("product_action"))
            put(GAAppender.VALUE, 2500L)
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
        appender.tracker = tracker

        appender.trackEvent(screenName, payloadMap)

        assertNotNull(appender.tracker)
        verify(tracker)?.setScreenName(screenName)
        verify(tracker)?.send(expectedPayload)
    }

    @Test
    fun testNoTrackEventDisabledAppender() {
        val screenName = "screenName"
        val payloadMap = HashMap<String, Any>().apply {
            put(GAAppender.CATEGORY, "category")
            put(GAAppender.ACTION, "action")
            put(GAAppender.LABEL, "label")
            put(GAAppender.PRODUCT, Product())
            put(GAAppender.PRODUCT_ACTION, ProductAction("product_action"))
            put(GAAppender.VALUE, 2500L)
        }
        appender.tracker = tracker

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
        `when`(payload.get(GAAppender.CATEGORY)).thenReturn("category")
        `when`(payload.get(GAAppender.ACTION)).thenReturn("action")
        `when`(payload.get(GAAppender.VALUE)).thenReturn(2500L)
        `when`(payload.get(GAAppender.LABEL)).thenReturn("label")
        `when`(payload.get(GAAppender.PRODUCT)).thenReturn(product)
        `when`(payload.get(GAAppender.PRODUCT_ACTION)).thenReturn(ProductAction("product_action"))
        val expectedPayload = HashMap<String, String>().apply {
            put("&ea", "action")
            put("&ec", "category")
            put("&el", "label")
            put("&t", "event")
            put("&ev", "2500")
            put("&pa", "product_action")
            put("&pr1nm", "product_name")
        }
        appender.tracker = tracker

        appender.trackEvent(eventName, payload)

        assertNotNull(appender.tracker)
        verify(tracker)?.setReferrer(eventName)
        verify(tracker)?.send(expectedPayload)
    }

    @Test
    fun testNoTrackEventWithBundleDisabledAppender() {
        val eventName = "eventName"
        val payload = mock(Bundle::class.java)
        `when`(payload.containsKey(GAAppender.CATEGORY)).thenReturn(true)
        `when`(payload.containsKey(GAAppender.ACTION)).thenReturn(true)
        `when`(payload.containsKey(GAAppender.VALUE)).thenReturn(true)
        `when`(payload.get(GAAppender.CATEGORY)).thenReturn("category")
        `when`(payload.get(GAAppender.ACTION)).thenReturn("action")
        `when`(payload.get(GAAppender.LABEL)).thenReturn("action")
        `when`(payload.get(GAAppender.PRODUCT)).thenReturn(Product())
        `when`(payload.get(GAAppender.PRODUCT_ACTION)).thenReturn(ProductAction("product_action"))
        `when`(payload.get(GAAppender.VALUE)).thenReturn(2500L)
        appender.tracker = tracker

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
        appender.tracker = tracker

        appender.trackPageHit(activity, screenName, screenClassOverride)

        verify(tracker).setScreenName(screenName)
        verify(tracker).send(expectedPayload)
    }

    @Test
    fun testTrackPageHitDisabledAppender() {
        val activity = mock(Activity::class.java)
        val screenName = "screenName"
        val screenClassOverride = "com.mindera.skeletoid.ScreenName"

        appender.tracker = tracker

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
        appender.tracker = tracker

        appender.setUserID(userId)

        verify(tracker).setClientId(userId)
    }

    @Test
    fun testSetUserIdDisabledAppender() {
        val userId = "userId"
        appender.tracker = tracker

        appender.disableAppender()
        appender.setUserID(userId)

        verifyNoMoreInteractions(tracker)
    }
}

