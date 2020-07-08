package com.mindera.skeletoid.threads.utils

import com.mindera.skeletoid.threads.utils.CallbackTask.ICallbackTask
import org.junit.Test
import org.mockito.Mockito

class CallbackTaskUnitTest {
    @Test
    fun testTaskCompleted() {
        val runnable =
            Mockito.mock(Runnable::class.java)
        val callbackTask = Mockito.mock(ICallbackTask::class.java)
        val task = CallbackTask(runnable, callbackTask)
        task.run()
        Mockito.verify(runnable, Mockito.times(1)).run()
        Mockito.verify(callbackTask, Mockito.times(1)).taskComplete()
    }
}