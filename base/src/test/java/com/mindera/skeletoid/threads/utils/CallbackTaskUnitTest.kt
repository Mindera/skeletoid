package com.mindera.skeletoid.threads.utils

import org.junit.Test

import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class CallbackTaskUnitTest {

    @Test
    fun testTaskCompleted() {

        val runnable = mock(Runnable::class.java)
        val callbackTask = mock(CallbackTask.ICallbackTask::class.java)

        val task = CallbackTask(runnable, callbackTask)
        task.run()

        verify(runnable, times(1)).run()
        verify(callbackTask, times(1)).taskComplete()
    }
}
