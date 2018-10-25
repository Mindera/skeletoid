package com.mindera.skeletoid.rxbindings.searchview

import android.content.Context
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class RxSearchViewUnitTest {

    private var mContext: Context? = null

    @Before
    fun setUp() {
        mContext = Mockito.mock<Context>(Context::class.java)
    }

    @Test(expected = AssertionError::class)
    fun testDisableAppender() {
        val appender = RxSearchView()
    }
}