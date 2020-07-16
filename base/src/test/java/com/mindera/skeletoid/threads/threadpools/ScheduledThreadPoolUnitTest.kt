package com.mindera.skeletoid.threads.threadpools

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ScheduledThreadPoolUnitTest {

    private lateinit var threadFactory: NamedThreadFactory

    private lateinit var threadPoolExecutor: ScheduledThreadPoolExecutor


    @Before
    fun setUp() {
        threadFactory = NamedThreadFactory("", 1)
        threadPoolExecutor = ScheduledThreadPoolExecutor(1, threadFactory)
    }

    @Test
    fun testThreadPoolInitialization() {
        Assert.assertEquals(1, threadPoolExecutor.corePoolSize)
    }

    @Test
    fun testShutdownThreadPool() {
        threadPoolExecutor.shutdown()
        Assert.assertTrue(threadPoolExecutor.isShutdown)
    }

    @Test
    fun testShutdownNowThreadPool() {
        threadPoolExecutor.shutdownNow()
        Assert.assertTrue(threadPoolExecutor.isShutdown)
    }
}
