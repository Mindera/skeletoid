package com.mindera.skeletoid.threads.utils;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CallbackTaskUnitTest {

    @Test
    public void testTaskCompleted() {

        Runnable runnable = mock(Runnable.class);
        CallbackTask.ICallbackTask callbackTask = mock(CallbackTask.ICallbackTask.class);

        CallbackTask task = new CallbackTask(runnable, callbackTask);
        task.run();

        verify(runnable, times(1)).run();
        verify(callbackTask, times(1)).taskComplete();
    }
}
