package com.mindera.skeletoid.analytics.appenders

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.mindera.skeletoid.BuildConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.eq
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*")
@PrepareForTest(FirebaseAnalytics::class)
class FbAppenderUnitTest {

    private lateinit var appender: FbAppender
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var bundleArgumentCaptor: ArgumentCaptor<Bundle>

    @Rule
    @JvmField
    public var rule = PowerMockRule()

    @Before
    fun setUp() {
        appender = FbAppender()

        val context = PowerMockito.mock<Context>(Context::class.java)
        firebaseAnalytics = mock(FirebaseAnalytics::class.java)

        PowerMockito.mockStatic(FirebaseAnalytics::class.java)
        Mockito.`when`(FirebaseAnalytics.getInstance(context)).thenReturn(firebaseAnalytics)

        appender.firebaseAnalytics = firebaseAnalytics

        bundleArgumentCaptor = ArgumentCaptor.forClass(Bundle::class.java)
    }

    @Test
    fun testDisableNotEnabledAppender() {
        val appender = FbAppender()
        appender.disableAppender()

        assertNull(appender.firebaseAnalytics)
    }

    @Test
    fun testDisableAppender() {
        appender.disableAppender()

        assertNull(appender.firebaseAnalytics)
    }

    @Test
    fun testTrackEvent() {
        val screenName = "screenName"
        val payloadMap = HashMap<String, Any>().apply {
            put("CATEGORY", "category")
            put("ACTION", "action")
            put("VALUE", 2500L)
        }

        val payload = Bundle().apply {
            putString("CATEGORY", "category")
            putString("ACTION", "action")
            putLong("VALUE", 2500L)
        }

        appender.trackEvent(screenName, payloadMap)

        assertNotNull(appender.firebaseAnalytics)

        verify(firebaseAnalytics)?.logEvent(eq(screenName), bundleArgumentCaptor.capture())
        assertEquals(payload.toString(), bundleArgumentCaptor.value.toString())
    }

    @Test
    fun testNoTrackEventDisabledAppender() {
        val screenName = "screenName"
        val payloadMap = HashMap<String, Any>().apply {
            put("CATEGORY", "category")
            put("ACTION", "action")
            put("VALUE", 2500L)
        }

        appender.disableAppender()
        appender.trackEvent(screenName, payloadMap)

        verifyNoMoreInteractions(firebaseAnalytics)
    }

    @Test
    fun testTrackEventWithBundle() {
        val eventName = "eventName"
        val payload = Bundle().apply {
            putString("CATEGORY", "category")
            putString("ACTION", "action")
            putLong("VALUE", 2500L)
        }

        appender.trackEvent(eventName, payload)

        assertNotNull(appender.firebaseAnalytics)
        verify(firebaseAnalytics)?.logEvent(eq(eventName), bundleArgumentCaptor.capture())
        assertEquals(payload.toString(), bundleArgumentCaptor.value.toString())
    }

    @Test
    fun testNoTrackEventWithBundleDisabledAppender() {
        val eventName = "eventName"
        val payload = Bundle().apply {
            putString("CATEGORY", "category")
            putString("ACTION", "action")
            putLong("VALUE", 2500L)
        }

        appender.disableAppender()
        appender.trackEvent(eventName, payload)

        verifyNoMoreInteractions(firebaseAnalytics)
    }

    @Test
    fun testTrackPageHit() {
        val activity = Activity()
        val screenName = "screenName"
        val screenClassOverride = "com.mindera.skeletoid.ScreenName"

        appender.trackPageHit(activity, screenName, screenClassOverride)

        verify(firebaseAnalytics).setCurrentScreen(activity, screenName, screenClassOverride)
    }

    @Test
    fun testTrackPageHitDisabledAppender() {
        val activity = Activity()
        val screenName = "screenName"
        val screenClassOverride = "com.mindera.skeletoid.ScreenName"

        appender.disableAppender()
        appender.trackPageHit(activity, screenName, screenClassOverride)

        verifyNoMoreInteractions(firebaseAnalytics)
    }

    @Test
    fun testGetAnalyticsId() {
        val appender = FbAppender()

        assertEquals("FirebaseAnalytics", appender.analyticsId)
    }

    @Test
    fun testSetUserId() {
        val userId = "userId"

        appender.setUserId(userId)

        verify(firebaseAnalytics).setUserId(userId)
    }

    @Test
    fun testSetUserIdDisabledAppender() {
        val userId = "userId"

        appender.disableAppender()
        appender.setUserId(userId)

        verifyNoMoreInteractions(firebaseAnalytics)
    }

    @Test
    fun testSetUserProperty() {
        val userNameProperty = "name"
        val userName = "Minder"

        appender.setUserProperty(userNameProperty, userName)

        verify(firebaseAnalytics).setUserProperty(userNameProperty, userName)
    }

    @Test
    fun testSetUserPropertyDisabledAppender() {
        val userNameProperty = "name"
        val userName = "Minder"

        appender.disableAppender()
        appender.setUserProperty(userNameProperty, userName)

        verifyNoMoreInteractions(firebaseAnalytics)
    }
}
