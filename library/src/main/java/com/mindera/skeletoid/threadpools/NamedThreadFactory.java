package com.mindera.skeletoid.threadpools;

import com.mindera.skeletoid.logs.LOG;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Factory for threads that provides custom naming
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final String LOG_TAG = "NamedThreadFactory";

    private final ThreadGroup mGroup;
    private final AtomicInteger mThreadPoolNumber = new AtomicInteger(0);
    private final String mNamePrefix;
    private final int mMaxFactoryThreads;
    private Queue<Thread> threads;

    /**
     * Default constructor
     *
     * @param threadPoolName The name of the ThreadPool
     */
    NamedThreadFactory(String threadPoolName, int maxFactoryThreads) {
        SecurityManager s = System.getSecurityManager();
        mGroup = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        mNamePrefix = threadPoolName;
        mMaxFactoryThreads = maxFactoryThreads;
        threads = new ArrayBlockingQueue<>(maxFactoryThreads);
    }

    /**
     * Creates a new named thread
     *
     * @param r Runnable
     * @return Thread
     */
    public Thread newThread(Runnable r) {

        int threadNumber = mThreadPoolNumber.incrementAndGet();
        final String threadName = mNamePrefix + " [#" + threadNumber + "/" + mMaxFactoryThreads + "]";

        Thread t = new Thread(mGroup, r, threadName, 0);

        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }

        int threadTotal = ThreadPoolUtils.mThreadTotal.incrementAndGet();

        LOG.d(LOG_TAG, "Created one more thread: "
                + threadName
                + " | Total number of threads (currently): "
                + threadTotal);

        threads.add(t);

        return t;
    }

    Queue<Thread> getThreads() {
        return threads;
    }

    void clearThreads() {
        threads.clear();
    }
}
