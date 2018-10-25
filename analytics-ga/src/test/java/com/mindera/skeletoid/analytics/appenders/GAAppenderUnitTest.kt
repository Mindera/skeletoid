package com.mindera.skeletoid.analytics.appenders


import android.content.Context
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GAAppenderUnitTest {

    private var mContext: Context? = null

    @Before
    fun setUp() {
        mContext = Mockito.mock<Context>(Context::class.java)
    }

    @Test
    fun testDisableAppender() {
        val appender = GAAppender(0)
        appender.disableAppender()

        Mockito.verify(appender, Mockito.times(1)).setUserProperty("mTracker", null)
    }
}

