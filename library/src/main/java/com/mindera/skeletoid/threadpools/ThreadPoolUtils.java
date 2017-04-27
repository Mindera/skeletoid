package com.mindera.skeletoid.threadpools;

import android.support.annotation.VisibleForTesting;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An utils to create thread pools with readable names
 */
public final class ThreadPoolUtils {

    public static final AtomicInteger mThreadTotal = new AtomicInteger(0);

    @VisibleForTesting
    ThreadPoolUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get ThreadPool with fixed number of threads
     *
     * @param threadPoolName The name of the thread pool
     * @param MAX_THREAD     Max number of threads
     * @return Thread Pool
     */
    public static java.util.concurrent.ThreadPoolExecutor getFixedThreadPool(final String threadPoolName, final int MAX_THREAD) {
        return new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD, 0L,
                TimeUnit.MILLISECONDS, new NamedThreadFactory(threadPoolName,
                MAX_THREAD));
    }

    /**
     * Get ScheduledThreadPool with fixed number of threads
     *
     * @param threadPoolName The name of the thread pool
     * @param MAX_THREAD     Max number of threads
     * @return Thread Pool
     */
    public static ScheduledThreadPoolExecutor getScheduledThreadPool(final String threadPoolName, final int MAX_THREAD) {
        return new ScheduledThreadPoolExecutor(MAX_THREAD,
                new NamedThreadFactory(threadPoolName, MAX_THREAD));
    }

    /**
     * Shutdown thread pool and remove number of threads from total count
     *
     * @param threadPoolExecutor The threadpool to be shutdown
     */
    public static void shutdown(java.util.concurrent.ThreadPoolExecutor threadPoolExecutor) {
        if (threadPoolExecutor == null) {
            return;
        }
        mThreadTotal.addAndGet(-threadPoolExecutor.getCorePoolSize());
        threadPoolExecutor.shutdown();
    }

    /**
     * Shutdown thread pool now and remove number of threads from total count
     *
     * @param threadPoolExecutor The threadpool to be shutdown now
     */
    public static void shutdownNow(java.util.concurrent.ThreadPoolExecutor threadPoolExecutor) {
        if (threadPoolExecutor == null) {
            return;
        }
        mThreadTotal.addAndGet(-threadPoolExecutor.getCorePoolSize());
        threadPoolExecutor.shutdownNow();
    }
}
