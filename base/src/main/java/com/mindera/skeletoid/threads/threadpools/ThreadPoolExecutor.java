
package com.mindera.skeletoid.threads.threadpools;


import com.mindera.skeletoid.logs.LOG;

import java.util.Comparator;
import java.util.List;
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
     * Runnable priority types (these are just helpers, another int value can be sent)
     */
    public static final int HIGH_PRIORITY = 3;
    public static final int AVERAGE_PRIORITY = 2;
    public static final int STANDARD_PRIORITY = 1;
    public static final int LOW_PRIORITY = 0;


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
            return new PriorityTask<>(((Important) callable).getPriority(), callable);
        else
            return new PriorityTask<>(0, callable);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
        if (runnable instanceof Important)
            return new PriorityTask<>(((Important) runnable).getPriority(), runnable, value);
        else
            return new PriorityTask<>(0, runnable, value);
    }

    @Override
    public void execute(Runnable task) {
        if (task == null) {
            LOG.e(LOG_TAG, "Executing null runnable... ignoring");
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
            LOG.e(LOG_TAG, "Submitting null runnable... ignoring");
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
                LOG.w(LOG_TAG, "Task was cancelled: " + r.toString());
            } catch (InterruptedException ie) {
                LOG.w(LOG_TAG, "Task was interrupted: " + r.toString());
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
        final NamedThreadFactory factory = (NamedThreadFactory) getThreadFactory();
        if(factory != null){
            factory.changeThreadsNameAfterShutdown();
        }
        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        final NamedThreadFactory factory = (NamedThreadFactory) getThreadFactory();
        if(factory != null){
            factory.changeThreadsNameAfterShutdown();
        }
        return super.shutdownNow();
    }

    /**
     * Interface for Priority Tasks to Implement
     */
    private interface Important {
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
        private PriorityTask(final int priority, final Callable<T> tCallable) {
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
        private PriorityTask(final int priority, final Runnable runnable, final T result) {
            super(runnable, result);

            this.priority = priority;
        }

        /**
         * Compare priorities to sort ThreadPool queue
         */
        @Override
        public int compareTo(final PriorityTask<T> o) {
            final int diff = o.priority - priority;
            return Integer.compare(diff, 0);
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
