package com.mindera.skeletoid.analytics

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender
import com.mindera.skeletoid.logs.LOG.init
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

class AnalyticsUnitTest {
    private val packageName = "my.package.name"
    private var context: Context? = null

    @Before
    fun setUp() {
        context = Mockito.mock(Context::class.java)
    }

    @After
    fun cleanUp() {
        Analytics.deinit()
    }

    @Test
    fun testInit() {
        Analytics.init(context!!)
    }

    @Test
    fun testInitWithParams() {
        val appenders: MutableList<IAnalyticsAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        //Must have logger initialized for this test
        init(context!!)
        Analytics.init(context!!, appenders)
        Assert.assertTrue(Analytics.isInitialized)
    }

    @Test
    fun testIsInitialized() {
        Assert.assertFalse(Analytics.isInitialized)
        Analytics.init(context!!)
        Assert.assertTrue(Analytics.isInitialized)
    }

    @Test
    fun testDeinit() {
        val appenders: MutableList<IAnalyticsAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)

        //Must have logger initialized for this test
        init(context!!)
        Analytics.init(context!!, appenders)
        Analytics.deinit()
        Mockito.verify(appenderA, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderB, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderC, Mockito.times(1)).disableAppender()
    }

    @Test
    fun testAddAppenders() {
        val appenders: MutableList<IAnalyticsAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        val appendersIds =
            Analytics.init(context!!, appenders)
        Mockito.verify(appenderA, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderB, Mockito.times(1)).enableAppender(context)
        Mockito.verify(appenderC, Mockito.times(1)).enableAppender(context)
        Assert.assertNotNull(appendersIds)
        Assert.assertEquals(3, appendersIds.size)
        Assert.assertTrue(appendersIds.contains("A"))
        Assert.assertTrue(appendersIds.contains("B"))
        Assert.assertTrue(appendersIds.contains("C"))
    }

    @Test
    fun testAddAppendersRepeated() {
        val appenders: MutableList<IAnalyticsAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB1 = mockAppender("B")
        val appenderB2 = mockAppender("B")
        appenders.add(appenderA)
        appenders.add(appenderB1)
        appenders.add(appenderB2)
        init(context!!, packageName)
        val appendersIds =
            Analytics.init(context!!, appenders)
        Assert.assertNotNull(appendersIds)
        Assert.assertEquals(2, appendersIds.size)
        Assert.assertTrue(appendersIds.contains("A"))
        Assert.assertTrue(appendersIds.contains("B"))
    }

    @Test
    fun testDisableAppendersEmpty() {
        Analytics.removeAppenders(
            context!!,
            HashSet()
        )
    }

    @Test
    fun testRemoveAppenders() {
        val appenders: MutableList<IAnalyticsAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context!!, packageName)
        val appendersIds =
            Analytics.addAppenders(context!!, appenders)
        Analytics.removeAppenders(context!!, appendersIds)
        Mockito.verify(appenderA, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderB, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderC, Mockito.times(1)).disableAppender()
    }

    @Test
    fun testRemoveAllAppenders() {
        val appenders: MutableList<IAnalyticsAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        init(context!!, packageName)
        Analytics.addAppenders(context!!, appenders)
        Analytics.removeAllAppenders()
        Mockito.verify(appenderA, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderB, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderC, Mockito.times(1)).disableAppender()
    }

    @Test
    fun testTrackEvent() {
        val appenders: MutableList<IAnalyticsAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        Analytics.init(context!!, appenders)
        val analyticsPayload: MutableMap<String, Any> =
            HashMap()
        analyticsPayload["A"] = "A1"
        analyticsPayload["B"] = "B1"
        analyticsPayload["C"] = "C1"
        Analytics.trackEvent("test", analyticsPayload)
        Mockito.verify(appenderA, Mockito.times(1))
            .trackEvent("test", analyticsPayload)
        Mockito.verify(appenderB, Mockito.times(1))
            .trackEvent("test", analyticsPayload)
        Mockito.verify(appenderC, Mockito.times(1))
            .trackEvent("test", analyticsPayload)
    }

    @Test
    fun testTrackEventWithBundle() {
        val appenders: MutableList<IAnalyticsAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        Analytics.init(context!!, appenders)
        val analyticsPayload = Bundle()
        analyticsPayload.putString("A", "A1")
        analyticsPayload.putString("B", "B1")
        analyticsPayload.putString("C", "C1")
        Analytics.trackEvent("test", analyticsPayload)
        Mockito.verify(appenderA, Mockito.times(1))
            .trackEvent("test", analyticsPayload)
        Mockito.verify(appenderB, Mockito.times(1))
            .trackEvent("test", analyticsPayload)
        Mockito.verify(appenderC, Mockito.times(1))
            .trackEvent("test", analyticsPayload)
    }

    @Test
    fun testTrackPageHit() {
        val activity = Mockito.mock(Activity::class.java)
        val appenders: MutableList<IAnalyticsAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        Analytics.init(context!!, appenders)
        Analytics.trackPageHit(activity, "test", "screen class")
        Mockito.verify(appenderA, Mockito.times(1))
            .trackPageHit(activity, "test", "screen class")
        Mockito.verify(appenderB, Mockito.times(1))
            .trackPageHit(activity, "test", "screen class")
        Mockito.verify(appenderC, Mockito.times(1))
            .trackPageHit(activity, "test", "screen class")
    }

    @Test
    fun testSetUserID() {
        val appenders: MutableList<IAnalyticsAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        Analytics.init(context!!, appenders)
        Analytics.setUserID("1234")
        Mockito.verify(appenderA, Mockito.times(1)).setUserId("1234")
        Mockito.verify(appenderB, Mockito.times(1)).setUserId("1234")
        Mockito.verify(appenderC, Mockito.times(1)).setUserId("1234")
    }

    @Test
    fun testSetUserProperty() {
        val appenders: MutableList<IAnalyticsAppender> =
            ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        Analytics.init(context!!, appenders)
        Analytics.setUserProperty("name", "banana")
        Analytics.setUserProperty("age", "30")
        Mockito.verify(appenderA, Mockito.times(1))
            .setUserProperty("name", "banana")
        Mockito.verify(appenderB, Mockito.times(1))
            .setUserProperty("name", "banana")
        Mockito.verify(appenderC, Mockito.times(1))
            .setUserProperty("name", "banana")
        Mockito.verify(appenderA, Mockito.times(1)).setUserProperty("age", "30")
        Mockito.verify(appenderB, Mockito.times(1)).setUserProperty("age", "30")
        Mockito.verify(appenderC, Mockito.times(1)).setUserProperty("age", "30")
    }

    private fun mockAppender(analyticsId: String): IAnalyticsAppender {
        val appender =
            Mockito.mock(IAnalyticsAppender::class.java)
        Mockito.`when`(appender.analyticsId).thenReturn(analyticsId)
        return appender
    }
}