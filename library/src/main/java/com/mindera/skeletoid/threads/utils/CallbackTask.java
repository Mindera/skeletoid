package com.mindera.skeletoid.threads.utils;

/**
 * CallbackTask is used to determine when a runnable finished.
 */
public class CallbackTask implements Runnable {


    /**
     * Interface to receive callback when the runnable finishes
     */
    public interface ICallbackTask {

        void taskComplete();
    }

    /**
     * The "real" runnable
     */
    private final Runnable task;
    /**
     * The callback reference
     */
    private final ICallbackTask callback;

    /**
     * Constructor
     *
     * @param task     The "real" runnable
     * @param callback The callback reference
     */
    public CallbackTask(Runnable task, ICallbackTask callback) {
        this.task = task;
        this.callback = callback;
    }

    @Override
    public void run() {
        task.run();
        callback.taskComplete();
    }


}
