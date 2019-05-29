package com.mindera.skeletoid.routing.unfreeze

import android.content.Context
import android.content.Intent
import com.mindera.skeletoid.utils.extensions.mock
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify

class OpenUnfreezeScreenCommandTest {

    @Test
    fun testNavigate() {
        val context = mock<Context>()
        val command = OpenUnfreezeScreenCommand(context)
        command.navigate()
        verify(context).startActivity(ArgumentMatchers.any(Intent::class.java))
    }
}