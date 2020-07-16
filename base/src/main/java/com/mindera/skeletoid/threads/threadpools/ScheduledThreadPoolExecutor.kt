package com.mindera.skeletoid.threads.threadpools

import com.mindera.skeletoid.logs.LOG
import java.util.concurrent.CancellationException
import java.util.concurrent.Future
import java.util.concurrent.ScheduledThreadPoolExecutor

/**
 * This is an extension of a ScheduledThreadPoolExecutor that handles uncaught exceptions
 */
class ScheduledThreadPoolExecutor(
    corePoolSize: Int,
    threadFactory: NamedThreadFactory?
) : ScheduledThreadPoolExecutor(corePoolSize, threadFactory) {

    companion object {
        private const val LOG_TAG = "ScheduledThreadPoolExecutor"
    }

    override fun afterExecute(r: Runnable, t: Throwable) {
        var throwable: Throwable? = t
        super.afterExecute(r, throwable)
        if (throwable == null && r is Future<*>) {
            try {
                val future = r as Future<*>
                if (future.isDone) future.get()
            } catch (ce: CancellationException) {
                LOG.w(LOG_TAG, "Task was cancelled: $r")
            } catch (ie: InterruptedException) {
                LOG.w(LOG_TAG, "Task was interrupted: $r")
                Thread.currentThread().interrupt()
            } catch (e: Exception) {
                throwable = e.cause
            }
        }
        if (throwable != null){
            LOG.e(LOG_TAG, throwable, "Uncaught exception on ThreadPool")
        }
    }

    override fun shutdown() {
        val factory = threadFactory as? NamedThreadFactory
        factory?.changeThreadsNameAfterShutdown()
        super.shutdown()
    }

    override fun shutdownNow(): List<Runnable> {
        val factory = threadFactory as? NamedThreadFactory
        factory?.changeThreadsNameAfterShutdown()
        return super.shutdownNow()
    }

}