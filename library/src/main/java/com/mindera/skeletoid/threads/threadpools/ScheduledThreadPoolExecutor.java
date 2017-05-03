
package com.mindera.skeletoid.threads.threadpools;


import com.mindera.skeletoid.logs.LOG;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;

/**
 * This is an extension of a ScheduledThreadPoolExecutor that handles uncaught exceptions
 */
public class ScheduledThreadPoolExecutor extends java.util.concurrent.ScheduledThreadPoolExecutor {

    private static final String LOG_TAG = "ScheduledThreadPoolExecutor";

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
                LOG.e(LOG_TAG, "Task was cancelled: " + r.toString());
            } catch (InterruptedException ie) {
                LOG.e(LOG_TAG, "Task was interrupted: " + r.toString());
                Thread.currentThread().interrupt(); // ignore/reset
            } catch (Exception e) {
                t = e.getCause();
            }
        }
        if (t != null)
            LOG.e(LOG_TAG, t, "Uncaught exception on ThreadPool");
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
    private void changeThreadsNameAfterShutdown() {
        final String SHUTDOWN_THREAD = "SHUTDOWN";

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
