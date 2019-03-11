package com.mindera.skeletoid.threads.threadpools


import com.mindera.skeletoid.logs.LOG
import java.util.concurrent.CancellationException
import java.util.concurrent.Future

/**
 * This is an extension of a ScheduledThreadPoolExecutor that handles uncaught exceptions
 */
class ScheduledThreadPoolExecutor(corePoolSize: Int, threadFactory: NamedThreadFactory) :
    java.util.concurrent.ScheduledThreadPoolExecutor(corePoolSize, threadFactory) {

    companion object {
        private const val LOG_TAG = "ScheduledThreadPoolExecutor"
        private const val SHUTDOWN_THREAD = "SHUTDOWN"

    }

    override fun afterExecute(r: Runnable, t: Throwable?) {
        var throwable = t
        super.afterExecute(r, throwable)
        if (throwable == null && r is Future<*>) {
            try {
                val future = r as Future<*>
                if (future.isDone)
                    future.get()
            } catch (ce: CancellationException) {
                LOG.e(LOG_TAG, "Task was cancelled: $r")
            } catch (ie: InterruptedException) {
                LOG.e(LOG_TAG, "Task was interrupted: $r")
                Thread.currentThread().interrupt() // ignore/reset
            } catch (e: Exception) {
                throwable = e.cause
            }

        }

        if (throwable != null)
            LOG.e(LOG_TAG, throwable, "Uncaught exception on ThreadPool")
    }


    override fun shutdown() {
        changeThreadsNameAfterShutdown()
        super.shutdown()
    }

    override fun shutdownNow(): List<Runnable> {
        changeThreadsNameAfterShutdown()
        return super.shutdownNow()
    }

    /**
     * Mark threads name after shutdown to provide accurate logs
     */
    private fun changeThreadsNameAfterShutdown() {
        if (threadFactory is NamedThreadFactory) {
            val factory = threadFactory as NamedThreadFactory
            val threads = factory.threads
            if (threads != null) {
                for (t in threads) {
                    val threadName = t.name
                    if (threadName != null && !threadName.startsWith(SHUTDOWN_THREAD)) {
                        t.name = SHUTDOWN_THREAD + " " + t.name
                    }
                }
                factory.clearThreads()
            }
        }
    }
}
