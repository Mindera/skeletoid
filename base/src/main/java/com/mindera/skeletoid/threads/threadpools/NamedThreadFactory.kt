package com.mindera.skeletoid.threads.threadpools

import com.mindera.skeletoid.logs.LOG
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * Factory for threads that provides custom naming
 */

/**
 * Default constructor
 *
 * @param threadPoolName The name of the ThreadPool
 * @param maxFactoryThreads Max number of threads
 */
open class NamedThreadFactory internal constructor(
    threadPoolName: String,
    maxFactoryThreads: Int
) : ThreadFactory {
    private val group: ThreadGroup
    private val threadPoolNumber = AtomicInteger(0)
    private val namePrefix: String
    private val maxFactoryThreads: Int
    val threads: Queue<Thread>

    companion object {
        private const val LOG_TAG = "NamedThreadFactory"
    }


    init {
        val securityManager = System.getSecurityManager()
        group = if (securityManager != null) {
            securityManager.threadGroup
        } else {
            Thread.currentThread().threadGroup ?: throw IllegalStateException("No value for thread group")
        }
        namePrefix = threadPoolName
        this.maxFactoryThreads = maxFactoryThreads
        threads = ArrayBlockingQueue(maxFactoryThreads)
    }

    /**
     * Creates a new named thread
     *
     * @param runnable Runnable
     * @return Thread
     */
    override fun newThread(runnable: Runnable): Thread {
        val threadNumber = threadPoolNumber.incrementAndGet()
        val threadName = "$namePrefix [#$threadNumber/$maxFactoryThreads]"
        val thread = Thread(group, runnable, threadName, 0)

        //Make sure thread is not daemon
        thread.isDaemon = false
        //Make sure thread has normal priority
        thread.priority = Thread.NORM_PRIORITY

        val threadTotal = ThreadPoolUtils.threadTotal.incrementAndGet()
        LOG.d(LOG_TAG, "Created one more thread: $threadName | Total number of threads (currently): $threadTotal")
        threads.add(thread)
        return thread
    }

    fun clearThreads() {
        threads.clear()
    }

    /**
     * Mark threads name after shutdown to provide accurate logs
     */
    fun changeThreadsNameAfterShutdown() {
        val shutdownThreadName = "SHUTDOWN"
        for (thread in threads) {
            val threadName = thread.name
            if (!threadName.startsWith(shutdownThreadName)) {
                thread.name = shutdownThreadName + " " + thread.name
            }
        }
        clearThreads()
    }
}