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
 * @param threadNamePrefix The name of the ThreadPool
 * @param maxThreads Max number of threads
 */
class NamedThreadFactory(private val threadNamePrefix: String, private val maxThreads: Int) :
    ThreadFactory {

    companion object {
        private val LOG_TAG = "NamedThreadFactory"
    }

    private val mGroup: ThreadGroup
    private val mThreadPoolNumber = AtomicInteger(0)
    val threads: Queue<Thread>

    init {
        val s = System.getSecurityManager()
        mGroup = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
        threads = ArrayBlockingQueue(maxThreads)
    }

    /**
     * Creates a new named thread
     *
     * @param r Runnable
     * @return Thread
     */
    override fun newThread(r: Runnable): Thread {

        val threadNumber = mThreadPoolNumber.incrementAndGet()
        val threadName = "$threadNamePrefix [#$threadNumber/$maxThreads]"

        val t = Thread(mGroup, r, threadName, 0)

        if (t.isDaemon) {
            t.isDaemon = false
        }
        if (t.priority != Thread.NORM_PRIORITY) {
            t.priority = Thread.NORM_PRIORITY
        }

        val threadTotal = ThreadPoolUtils.totalThreads.incrementAndGet()

        LOG.d(
            LOG_TAG,
            """Created one more thread: $threadName | Total number of threads (currently): $threadTotal"""
        )

        threads.add(t)

        return t
    }

    fun clearThreads() {
        threads.clear()
    }
}
