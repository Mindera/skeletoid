package com.mindera.skeletoid.routing

import org.junit.Test

import android.app.Activity
import android.content.Context
import android.content.Intent
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.any

import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class OpenUriCommandUnitTest {

    @Test
    fun testNavigate() {
        val context = mock(Context::class.java)
        val command = OpenUriCommand(context, "")

        command.navigate()

        verify(context, times(1)).startActivity(ArgumentMatchers.any(Intent::class.java))
    }
}
