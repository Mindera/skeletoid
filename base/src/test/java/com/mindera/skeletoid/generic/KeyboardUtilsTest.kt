package com.mindera.skeletoid.generic

import android.app.Activity
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class KeyboardUtilsTest {

    @Test(expected = NullPointerException::class)
    fun testHideKeyboard() {
        val activity = mock(Activity::class.java)
        KeyboardUtils.hideKeyboard(activity)
    }
}
