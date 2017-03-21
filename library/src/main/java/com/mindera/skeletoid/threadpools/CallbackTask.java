package com.mindera.skeletoid.threadpools;

/**
 * CallbackTask to help determine when a task is finished.
 */
public class CallbackTask implements Runnable{

    public interface ICallbackTask {
        void taskComplete();
    }

    private final Runnable task;

    private final ICallbackTask callback;

    public CallbackTask(Runnable task, ICallbackTask callback) {
        this.task = task;
        this.callback = callback;
    }

    public void run() {
        task.run();
        callback.taskComplete();
    }
}
