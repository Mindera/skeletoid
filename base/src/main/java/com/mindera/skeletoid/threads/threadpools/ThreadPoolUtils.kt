package com.mindera.skeletoid.threads.threadpools

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * An utils to create thread pools with readable names
 */
object ThreadPoolUtils {
    @JvmField
    val threadTotal = AtomicInteger(0)

    /**
     * Get ThreadPool with fixed number of threads
     *
     * @param threadPoolName The name of the thread pool
     * @param MAX_THREAD     Max number of threads
     * @return Thread Pool
     */
    @JvmStatic
    fun getFixedThreadPool(
        threadPoolName: String, MAX_THREAD: Int
    ): java.util.concurrent.ThreadPoolExecutor {
        return ThreadPoolExecutor(
            MAX_THREAD,
            MAX_THREAD,
            0L,
            TimeUnit.MILLISECONDS,
            NamedThreadFactory(threadPoolName, MAX_THREAD)
        )
    }

    /**
     * Get ScheduledThreadPool with fixed number of threads
     *
     * @param threadPoolName The name of the thread pool
     * @param MAX_THREAD     Max number of threads
     * @return Thread Pool
     */
    @JvmStatic
    fun getScheduledThreadPool(
        threadPoolName: String,
        MAX_THREAD: Int
    ): ScheduledThreadPoolExecutor {
        return ScheduledThreadPoolExecutor(
            MAX_THREAD,
            NamedThreadFactory(threadPoolName, MAX_THREAD)
        )
    }

    /**
     * Shutdown thread pool and remove number of threads from total count
     *
     * @param threadPoolExecutor The threadpool to be shutdown
     */
    @JvmStatic
    fun shutdown(threadPoolExecutor: java.util.concurrent.ThreadPoolExecutor) {
        threadTotal.addAndGet(-threadPoolExecutor.corePoolSize)
        threadPoolExecutor.shutdown()
    }

    /**
     * Shutdown thread pool now and remove number of threads from total count
     *
     * @param threadPoolExecutor The threadpool to be shutdown now
     */
    @JvmStatic
    fun shutdownNow(threadPoolExecutor: java.util.concurrent.ThreadPoolExecutor) {
        threadTotal.addAndGet(-threadPoolExecutor.corePoolSize)
        threadPoolExecutor.shutdownNow()
    }
}