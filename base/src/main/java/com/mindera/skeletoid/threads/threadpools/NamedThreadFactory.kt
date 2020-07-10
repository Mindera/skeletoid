package com.mindera.skeletoid.threads.threadpools

import com.mindera.skeletoid.logs.LOG
import java.lang.IllegalStateException
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * Factory for threads that provides custom naming
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

    /**
     * Default constructor
     *
     * @param threadPoolName The name of the ThreadPool
     */
    init {
        val s = System.getSecurityManager()
        group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
            ?: throw IllegalStateException("No value for thread group")
        namePrefix = threadPoolName
        this.maxFactoryThreads = maxFactoryThreads
        threads = ArrayBlockingQueue(maxFactoryThreads)
    }

    /**
     * Creates a new named thread
     *
     * @param r Runnable
     * @return Thread
     */
    override fun newThread(r: Runnable): Thread {
        val threadNumber = threadPoolNumber.incrementAndGet()
        val threadName = "$namePrefix [#$threadNumber/$maxFactoryThreads]"
        val t = Thread(group, r, threadName, 0)
        if (t.isDaemon) {
            t.isDaemon = false
        }
        if (t.priority != Thread.NORM_PRIORITY) {
            t.priority = Thread.NORM_PRIORITY
        }
        val threadTotal = ThreadPoolUtils.threadTotal.incrementAndGet()
        LOG.d(LOG_TAG,
            "Created one more thread: "
                    + threadName
                    + " | Total number of threads (currently): "
                    + threadTotal
        )
        threads.add(t)
        return t
    }

    fun clearThreads() {
        threads.clear()
    }
}