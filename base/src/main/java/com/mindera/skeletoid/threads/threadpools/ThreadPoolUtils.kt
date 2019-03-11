package com.mindera.skeletoid.threads.threadpools

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * An utils to create thread pools with readable names
 */
object ThreadPoolUtils {

    val totalThreads = AtomicInteger(0)

    /**
     * Get ThreadPool with fixed number of threads
     *
     * @param threadPoolName The name of the thread pool
     * @param maxThreads     Max number of threads
     * @param coreThreads    Core number of threads (if not defined = maxThreads)
     * @return Thread Pool
     */
    fun getFixedThreadPool(
        threadPoolName: String,
        maxThreads: Int,
        coreThreads: Int = maxThreads
    ): java.util.concurrent.ThreadPoolExecutor =
        ThreadPoolExecutor(
            coreThreads, maxThreads, 0L,
            TimeUnit.MILLISECONDS, NamedThreadFactory(
                threadPoolName,
                maxThreads
            )
        )


    /**
     * Get ScheduledThreadPool with fixed number of threads
     *
     * @param threadPoolName The name of the thread pool
     * @param maxThreads     Max number of threads
     * @return Thread Pool
     */
    fun getScheduledThreadPool(
        threadPoolName: String,
        maxThreads: Int,
        coreThreads: Int = maxThreads
    ): ScheduledThreadPoolExecutor {
        return ScheduledThreadPoolExecutor(
            coreThreads,
            NamedThreadFactory(threadPoolName, maxThreads)
        )
    }

    /**
     * Shutdown thread pool and remove number of threads from total count
     *
     * @param threadPoolExecutor The threadpool to be shutdown
     */
    fun shutdown(threadPoolExecutor: java.util.concurrent.ThreadPoolExecutor) {
        totalThreads.addAndGet(-threadPoolExecutor.corePoolSize)
        threadPoolExecutor.shutdown()
    }

    /**
     * Shutdown thread pool now and remove number of threads from total count
     *
     * @param threadPoolExecutor The threadpool to be shutdown now
     */
    fun shutdownNow(threadPoolExecutor: java.util.concurrent.ThreadPoolExecutor) {
        totalThreads.addAndGet(-threadPoolExecutor.corePoolSize)
        threadPoolExecutor.shutdownNow()
    }

}
