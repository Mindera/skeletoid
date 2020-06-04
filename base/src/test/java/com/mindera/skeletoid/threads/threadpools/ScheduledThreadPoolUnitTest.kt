package com.mindera.skeletoid.threads.threadpools

import junit.framework.Assert
import org.junit.Test
import org.mockito.Mockito

class ScheduledThreadPoolUnitTest {
    @Test
    fun testThreadPoolInitialization() {
        val namedThreadFactory =
            Mockito.mock(
                NamedThreadFactory::class.java
            )
        val scheduledThreadPoolExecutor =
            ScheduledThreadPoolExecutor(
                1,
                namedThreadFactory
            )
        Assert.assertEquals(1, scheduledThreadPoolExecutor.corePoolSize)
    }

    @Test
    fun testShutdownThreadPool() {
        val namedThreadFactory =
            Mockito.mock(
                NamedThreadFactory::class.java
            )
        val scheduledThreadPoolExecutor =
            ScheduledThreadPoolExecutor(
                1,
                namedThreadFactory
            )
        scheduledThreadPoolExecutor.shutdown()
        Assert.assertTrue(scheduledThreadPoolExecutor.isShutdown)
    }

    @Test
    fun testShutdownNowThreadPool() {
        val namedThreadFactory =
            Mockito.mock(
                NamedThreadFactory::class.java
            )
        val scheduledThreadPoolExecutor =
            ScheduledThreadPoolExecutor(
                1,
                namedThreadFactory
            )
        scheduledThreadPoolExecutor.shutdownNow()
        Assert.assertTrue(scheduledThreadPoolExecutor.isShutdown)
    }
}