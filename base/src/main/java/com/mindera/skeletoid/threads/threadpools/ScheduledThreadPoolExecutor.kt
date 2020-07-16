package com.mindera.skeletoid.threads.threadpools

import androidx.annotation.VisibleForTesting
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

    @VisibleForTesting
    override fun afterExecute(runnable: Runnable?, throwable: Throwable?) {
        super.afterExecute(runnable, throwable)

        var localThrowable: Throwable? = throwable
        if (localThrowable == null && runnable is Future<*>) {
            try {
                val future = runnable as Future<*>
                if (future.isDone) future.get()
            } catch (ce: CancellationException) {
                LOG.w(LOG_TAG, "Task was cancelled: $runnable")
            } catch (ie: InterruptedException) {
                LOG.w(LOG_TAG, "Task was interrupted: $runnable")
                Thread.currentThread().interrupt()
            } catch (e: Exception) {
                localThrowable = e.cause
            }
        }
        if (localThrowable != null){
            LOG.e(LOG_TAG, localThrowable, "Uncaught exception on ThreadPool")
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