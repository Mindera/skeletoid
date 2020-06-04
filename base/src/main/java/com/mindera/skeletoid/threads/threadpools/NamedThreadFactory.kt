package com.mindera.skeletoid.threads.threadpools

import com.mindera.skeletoid.logs.LOG
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * Factory for threads that provides custom naming
 */
class NamedThreadFactory internal constructor(
    threadPoolName: String,
    maxFactoryThreads: Int
) : ThreadFactory {
    private val mGroup: ThreadGroup
    private val mThreadPoolNumber =
        AtomicInteger(0)
    private val mNamePrefix: String
    private val mMaxFactoryThreads: Int
    val threads: Queue<Thread>

    /**
     * Creates a new named thread
     *
     * @param r Runnable
     * @return Thread
     */
    override fun newThread(r: Runnable): Thread {
        val threadNumber = mThreadPoolNumber.incrementAndGet()
        val threadName =
            "$mNamePrefix [#$threadNumber/$mMaxFactoryThreads]"
        val t = Thread(mGroup, r, threadName, 0)
        if (t.isDaemon) {
            t.isDaemon = false
        }
        if (t.priority != Thread.NORM_PRIORITY) {
            t.priority = Thread.NORM_PRIORITY
        }
        val threadTotal = ThreadPoolUtils.mThreadTotal.incrementAndGet()
        LOG.d(
            LOG_TAG,
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
        mGroup = if (s != null) s.threadGroup else Thread.currentThread()
            .threadGroup
        mNamePrefix = threadPoolName
        mMaxFactoryThreads = maxFactoryThreads
        threads = ArrayBlockingQueue(maxFactoryThreads)
    }
}