package com.mindera.skeletoid.routing

import android.app.Activity
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class CloseActivityCommandUnitTest {

    @Test
    fun navigate() {

        val activity = mock(Activity::class.java)
        val command = CloseActivityCommand(activity)
        command.navigate()

        verify(activity, times(1)).finish()
    }
}
