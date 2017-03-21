
package com.mindera.skeletoid.threadpools;


import com.mindera.skeletoid.logs.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This is an extension of a ThreadPoolExecutor that implements priorities for runnables submitted.
 * It also handles uncaught exceptions
 */
public class ThreadPoolExecutor extends java.util.concurrent.ThreadPoolExecutor {

    private static final String LOG_TAG = "ThreadPoolExecutor";

    /**
     * Prefix of threads of a shutdown threadpool
     */
    private final String SHUTDOWN_THREAD = "SHUTDOWN";

    /**
     * Runnable priority types
     */
    public static final int HIGH_PRIORITY = 1;
    public static final int STANDARD_PRIORITY = 0;


    /**
     * ThreadPoolExecutor constructor
     *
     * @param corePoolSize  Pool size
     * @param maxPoolSize   Max threads
     * @param keepAlive     Keep alive time
     * @param timeUnit      Time unit
     * @param threadFactory Thread Factory
     */
    public ThreadPoolExecutor(int corePoolSize, int maxPoolSize, long keepAlive, TimeUnit timeUnit,
                              final NamedThreadFactory threadFactory) {
        super(corePoolSize, maxPoolSize, keepAlive, timeUnit, new PriorityBlockingQueue<Runnable>(11,
                new PriorityTaskComparator()), threadFactory);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
        if (callable instanceof Important)
            return new PriorityTask<T>(((Important) callable).getPriority(), callable);
        else
            return new PriorityTask<T>(0, callable);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
        if (runnable instanceof Important)
            return new PriorityTask<T>(((Important) runnable).getPriority(), runnable, value);
        else
            return new PriorityTask<T>(0, runnable, value);
    }

    @Override
    public void execute(Runnable task) {
        if (task == null) {
            Logger.e(LOG_TAG, "Executing null runnable... ignoring");
            return;
        }

        if (task instanceof PriorityTask) {
            super.execute(task);
        } else {
            final RunnableFuture<Object> futureTask = newTaskFor(task, null);
            super.execute(futureTask);
        }
    }

    @Override
    public Future<?> submit(final Runnable task) {
        if (task == null) {
            Logger.e(LOG_TAG, "Submitting null runnable... ignoring");
            return null;
        }
        final RunnableFuture<Object> futureTask = newTaskFor(task, null);
        execute(futureTask);
        return futureTask;
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

    /**
     * Interface for Priority Tasks to Implement
     */
    public interface Important {
        int getPriority();
    }

    private static final class PriorityTask<T> extends FutureTask<T> implements Comparable<PriorityTask<T>> {
        private final int priority;

        /**
         * PriorityTask constructor
         *
         * @param priority  Task priority
         * @param tCallable callable
         */
        public PriorityTask(final int priority, final Callable<T> tCallable) {
            super(tCallable);

            this.priority = priority;
        }

        /**
         * PriorityTask constructor
         *
         * @param priority Task priority
         * @param runnable Runnable
         * @param result   Task result
         */
        public PriorityTask(final int priority, final Runnable runnable, final T result) {
            super(runnable, result);

            this.priority = priority;
        }

        /**
         * Compare priorities to sort ThreadPool queue
         */
        @Override
        public int compareTo(final PriorityTask<T> o) {
            final long diff = o.priority - priority;
            return 0 == diff ? 0 : 0 > diff ? -1 : 1;
        }
    }

    /**
     * Compare priorities to sort ThreadPool queue
     */
    private static class PriorityTaskComparator<T extends PriorityTask> implements Comparator<T> {
        @Override
        public int compare(final T left, final T right) {
            return left.compareTo(right);
        }
    }
}
