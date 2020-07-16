package com.mindera.skeletoid.threads.threadpools

import com.mindera.skeletoid.logs.LOG.e
import com.mindera.skeletoid.logs.LOG.w
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
        var t: Throwable? = t
        super.afterExecute(r, t)
        if (t == null && r is Future<*>) {
            try {
                val future = r as Future<*>
                if (future.isDone) future.get()
            } catch (ce: CancellationException) {
                w(
                    LOG_TAG,
                    "Task was cancelled: $r"
                )
            } catch (ie: InterruptedException) {
                w(
                    LOG_TAG,
                    "Task was interrupted: $r"
                )
                Thread.currentThread().interrupt() // ignore/reset
            } catch (e: Exception) {
                t = e.cause
            }
        }
        if (t != null) e(
            LOG_TAG,
            t,
            "Uncaught exception on ThreadPool"
        )
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