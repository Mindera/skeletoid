package com.mindera.skeletoid.generic

import android.app.Activity
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class KeyboardUtilsTest {

    lateinit var activity: Activity
    lateinit var inputMethodManager: InputMethodManager

    @Before
    fun setUp() {
        activity = mock(Activity::class.java)
        inputMethodManager = mock(InputMethodManager::class.java)

        `when`(activity.getSystemService(Activity.INPUT_METHOD_SERVICE)).thenReturn(inputMethodManager)
    }

    @Test
    fun testHideKeyboard() {
        val view = mock(View::class.java)
        val binder = mock(IBinder::class.java)
        `when`(activity.currentFocus).thenReturn(view)
        `when`(view.windowToken).thenReturn(binder)

        KeyboardUtils.hideKeyboard(activity)

        verify(inputMethodManager).hideSoftInputFromWindow(binder, 0)
    }

    @Test
    fun testHideKeyboardFrom() {
        val view = mock(View::class.java)
        val binder = mock(IBinder::class.java)
        `when`(view.windowToken).thenReturn(binder)

        KeyboardUtils.hideKeyboardFrom(activity, view)

        verify(inputMethodManager).hideSoftInputFromWindow(binder, 0)
    }

    @Test
    fun testShowKeyboard() {
        KeyboardUtils.showKeyboard(activity)

        verify(inputMethodManager).toggleSoftInput(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())
    }
}
