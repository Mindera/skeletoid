package com.mindera.skeletoid.analytics

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.mindera.skeletoid.analytics.appenders.interfaces.IAnalyticsAppender
import com.mindera.skeletoid.analytics.interfaces.IAnalyticsManager
import com.mindera.skeletoid.logs.LOG.init
import com.mindera.skeletoid.utils.extensions.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

class AnalyticsManagerUnitTest {
    companion object {
        private const val packageName = "my.package.name"
    }

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = Mockito.mock(Context::class.java)
    }

    @Test
    fun testAddAppendersEmpty() {
        val analyticsManager: IAnalyticsManager = AnalyticsManager()
        val appendersIds =
            analyticsManager.addAppenders(context, ArrayList())
        Assert.assertNotNull(appendersIds)
        Assert.assertEquals(0, appendersIds.size)
    }

    @Test
    fun testAddAppenders() {
        val analyticsManager: IAnalyticsManager = AnalyticsManager()
        val appenders: MutableList<IAnalyticsAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        val appendersIds = analyticsManager.addAppenders(context, appenders)
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
        val analyticsManager: IAnalyticsManager = AnalyticsManager()
        val appenders: MutableList<IAnalyticsAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB1 = mockAppender("B")
        val appenderB2 = mockAppender("B")
        appenders.add(appenderA)
        appenders.add(appenderB1)
        appenders.add(appenderB2)

        //We must initialize the LOG since it prints an error
        init(context, packageName)
        val appendersIds = analyticsManager.addAppenders(context, appenders)
        Assert.assertNotNull(appendersIds)
        Assert.assertEquals(2, appendersIds.size)
        Assert.assertTrue(appendersIds.contains("A"))
        Assert.assertTrue(appendersIds.contains("B"))
    }

    @Test
    fun testDisableAppendersEmpty() {
        val analyticsManager = AnalyticsManager()
        analyticsManager.removeAppenders(context, HashSet())
        Assert.assertNotNull(analyticsManager)
    }

    @Test
    fun testDisableAppenders() {
        val analyticsManager: IAnalyticsManager = AnalyticsManager()
        val appenders: MutableList<IAnalyticsAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        val appendersIds = analyticsManager.addAppenders(context, appenders)
        analyticsManager.removeAppenders(context, appendersIds)
        Mockito.verify(appenderA, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderB, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderC, Mockito.times(1)).disableAppender()
    }

    @Test
    fun testDisableAllAppenders() {
        val analyticsManager: IAnalyticsManager = AnalyticsManager()
        val appenders: MutableList<IAnalyticsAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        analyticsManager.addAppenders(context, appenders)
        analyticsManager.removeAllAppenders()
        Mockito.verify(appenderA, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderB, Mockito.times(1)).disableAppender()
        Mockito.verify(appenderC, Mockito.times(1)).disableAppender()
    }

    @Test
    fun testTrackEvent() {
        val analyticsManager: IAnalyticsManager = AnalyticsManager()
        val appenders: MutableList<IAnalyticsAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        analyticsManager.addAppenders(context, appenders)
        val analyticsPayload: MutableMap<String, Any> = HashMap()
        analyticsPayload["A"] = "A1"
        analyticsPayload["B"] = "B1"
        analyticsPayload["C"] = "C1"
        analyticsManager.trackEvent("test", analyticsPayload)
        Mockito.verify(appenderA, Mockito.times(1))
            .trackEvent("test", analyticsPayload)
        Mockito.verify(appenderB, Mockito.times(1))
            .trackEvent("test", analyticsPayload)
        Mockito.verify(appenderC, Mockito.times(1))
            .trackEvent("test", analyticsPayload)
    }

    @Test
    fun testTrackEventWithBundle() {
        val analyticsManager: IAnalyticsManager = AnalyticsManager()
        val appenders: MutableList<IAnalyticsAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        analyticsManager.addAppenders(context, appenders)
        val analyticsPayload = Bundle()
        analyticsPayload.putString("A", "A1")
        analyticsPayload.putString("B", "B1")
        analyticsPayload.putString("C", "C1")
        analyticsManager.trackEvent("test", analyticsPayload)
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
        val analyticsManager: IAnalyticsManager = AnalyticsManager()
        val appenders: MutableList<IAnalyticsAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        analyticsManager.addAppenders(context, appenders)
        analyticsManager.trackPageHit(activity, "test", "screen class")
        Mockito.verify(appenderA, Mockito.times(1))
            .trackPageHit(activity, "test", "screen class")
        Mockito.verify(appenderB, Mockito.times(1))
            .trackPageHit(activity, "test", "screen class")
        Mockito.verify(appenderC, Mockito.times(1))
            .trackPageHit(activity, "test", "screen class")
    }

    @Test
    fun testSetUserID() {
        val analyticsManager: IAnalyticsManager = AnalyticsManager()
        val appenders: MutableList<IAnalyticsAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        analyticsManager.addAppenders(context, appenders)
        analyticsManager.setUserID("1234")
        Mockito.verify(appenderA, Mockito.times(1)).setUserId("1234")
        Mockito.verify(appenderB, Mockito.times(1)).setUserId("1234")
        Mockito.verify(appenderC, Mockito.times(1)).setUserId("1234")
    }

    @Test
    fun testSetUserProperty() {
        val analyticsManager: IAnalyticsManager = AnalyticsManager()
        val appenders: MutableList<IAnalyticsAppender> = ArrayList()
        val appenderA = mockAppender("A")
        val appenderB = mockAppender("B")
        val appenderC = mockAppender("C")
        appenders.add(appenderA)
        appenders.add(appenderB)
        appenders.add(appenderC)
        analyticsManager.addAppenders(context, appenders)
        analyticsManager.setUserProperty("name", "banana")
        analyticsManager.setUserProperty("age", "30")
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
        val appender: IAnalyticsAppender = mock()
        Mockito.`when`(appender.analyticsId).thenReturn(analyticsId)
        return appender
    }
}