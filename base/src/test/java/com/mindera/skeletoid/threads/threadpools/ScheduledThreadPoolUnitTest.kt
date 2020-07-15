package com.mindera.skeletoid.threads.threadpools

import com.mindera.skeletoid.utils.extensions.mock
import org.junit.Assert
import org.junit.Test

class ScheduledThreadPoolUnitTest {

    @Test
    fun testThreadPoolInitialization() {
        val namedThreadFactory : NamedThreadFactory = mock()
        val scheduledThreadPoolExecutor =
            ScheduledThreadPoolExecutor(1, namedThreadFactory)
        Assert.assertEquals(1, scheduledThreadPoolExecutor.corePoolSize)
    }

    @Test
    fun testShutdownThreadPool() {
        val namedThreadFactory : NamedThreadFactory = NamedThreadFactory("", 1)
        val scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1, namedThreadFactory)
        scheduledThreadPoolExecutor.shutdown()
        Assert.assertTrue(scheduledThreadPoolExecutor.isShutdown)
    }

    @Test
    fun testShutdownNowThreadPool() {
        val namedThreadFactory : NamedThreadFactory = NamedThreadFactory("", 1)
        val scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1, namedThreadFactory)
        scheduledThreadPoolExecutor.shutdownNow()
        Assert.assertTrue(scheduledThreadPoolExecutor.isShutdown)
    }
}
