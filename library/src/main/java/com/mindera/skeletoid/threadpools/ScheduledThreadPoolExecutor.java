
package com.mindera.skeletoid.threadpools;


import com.mindera.skeletoid.logs.Logger;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;

/**
 * This is an extension of a ScheduledThreadPoolExecutor that handles uncaught exceptions
 */
public class ScheduledThreadPoolExecutor extends java.util.concurrent.ScheduledThreadPoolExecutor {

    private static final String LOG_TAG = "ScheduledThreadPoolExecutor";


    /**
     * Prefix of threads of a shutdown threadpool
     */
    private final String SHUTDOWN_THREAD = "SHUTDOWN";

    public ScheduledThreadPoolExecutor(int corePoolSize, NamedThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }


    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone())
                    future.get();
            } catch (CancellationException ce) {
                Logger.e(LOG_TAG, "Task was cancelled: " + r.toString());
            } catch (InterruptedException ie) {
                Logger.e(LOG_TAG, "Task was interrupted: " + r.toString());
                Thread.currentThread().interrupt(); // ignore/reset
            } catch (Exception e) {
                t = e.getCause();
            }
        }
        if (t != null)
            Logger.e(LOG_TAG, "Uncaught exception on ThreadPool", t);
    }


    @Override
    public void shutdown() {
        changeThreadsNameAfterShutdown();
        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        changeThreadsNameAfterShutdown();
        return super.shutdownNow();
    }

    /**
     * Mark threads name after shutdown to provide accurate logs
     */
    public void changeThreadsNameAfterShutdown() {
        final NamedThreadFactory factory = (NamedThreadFactory) getThreadFactory();
        if (factory != null) {
            final Queue<Thread> threads = factory.getThreads();
            if (threads != null) {
                for (Thread t : threads) {
                    final String threadName = t.getName();
                    if (threadName != null && !threadName.startsWith(SHUTDOWN_THREAD)) {
                        t.setName(SHUTDOWN_THREAD + " " + t.getName());
                    }
                }
                factory.clearThreads();
            }
        }
    }
}
