package com.mindera.skeletoid.routing

import org.junit.Test

import android.app.Activity

import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class CloseActivityCommandUnitTest {

    @Test
    fun testNavigate() {
        val activity = mock(Activity::class.java)
        val command = CloseActivityCommand(activity)

        command.navigate()

        verify(activity, times(1)).finish()
    }

    @Test
    fun testNavigateWithEnterAnimation() {
        val enterAnimation = 1
        val activity = mock(Activity::class.java)
        val command = CloseActivityCommand(activity, enterAnimation = enterAnimation)

        command.navigate()

        verify(activity, times(1)).finish()
        verify(activity, times(1)).overridePendingTransition(enterAnimation, 0)
    }

    @Test
    fun testNavigateWithExitAnimation() {
        val exitAnimation = 2
        val activity = mock(Activity::class.java)
        val command = CloseActivityCommand(activity, exitAnimation = exitAnimation)

        command.navigate()

        verify(activity, times(1)).finish()
        verify(activity, times(1)).overridePendingTransition(0, exitAnimation)
    }

    @Test
    fun testNavigateWithEnterAndExitAnimation() {
        val enterAnimation = 1
        val exitAnimation = 2
        val activity = mock(Activity::class.java)
        val command = CloseActivityCommand(activity, enterAnimation = enterAnimation, exitAnimation = exitAnimation)

        command.navigate()

        verify(activity, times(1)).finish()
        verify(activity, times(1)).overridePendingTransition(enterAnimation, exitAnimation)
    }
}
