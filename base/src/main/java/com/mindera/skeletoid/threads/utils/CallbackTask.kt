package com.mindera.skeletoid.threads.utils

/**
 * CallbackTask is used to determine when a runnable finished.
 */
class CallbackTask
/**
 * Constructor
 *
 * @param task     The "real" runnable
 * @param callback The callback reference
 */
    (
    /**
     * The "real" runnable
     */
    private val task: Runnable,
    /**
     * The callback reference
     */
    private val callback: ICallbackTask
) : Runnable {

    /**
     * Interface to receive callback when the runnable finishes
     */
    interface ICallbackTask {

        fun taskComplete()
    }

    override fun run() {
        task.run()
        callback.taskComplete()
    }


}
