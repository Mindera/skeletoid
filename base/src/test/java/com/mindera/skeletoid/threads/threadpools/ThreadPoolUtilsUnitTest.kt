package com.mindera.skeletoid.threads.threadpools

import com.mindera.skeletoid.threads.threadpools.ThreadPoolUtils.getFixedThreadPool
import com.mindera.skeletoid.threads.threadpools.ThreadPoolUtils.getScheduledThreadPool
import com.mindera.skeletoid.threads.threadpools.ThreadPoolUtils.shutdown
import com.mindera.skeletoid.threads.threadpools.ThreadPoolUtils.shutdownNow
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ThreadPoolUtilsUnitTest {
    @Before
    fun beforeTest() {
        ThreadPoolUtils.threadTotal.set(0)
    }

    @Test
    fun testGetFixedThreadPool() {
        val maxThreads = 10
        val threadPoolName = "testPool"
        val threadPool =
            getFixedThreadPool(threadPoolName, maxThreads)
        Assert.assertEquals(maxThreads, threadPool.corePoolSize)
        Assert.assertEquals(maxThreads, threadPool.maximumPoolSize)
        Assert.assertEquals(
            0,
            threadPool.getKeepAliveTime(TimeUnit.MILLISECONDS)
        )
        val threadFactory = threadPool.threadFactory
        Assert.assertNotNull(threadFactory)
        Assert.assertTrue(threadFactory is NamedThreadFactory)

        // Would be nice to check the name and maxFactoryThreads of threadFactory
    }

    @Test
    fun testGetScheduledThreadPool() {
        val maxThreads = 10
        val threadPoolName = "testPool"
        val scheduledThreadPool =
            getScheduledThreadPool(threadPoolName, maxThreads)
        Assert.assertEquals(maxThreads, scheduledThreadPool.corePoolSize)
        val threadFactory =
            scheduledThreadPool.threadFactory
        Assert.assertNotNull(threadFactory)
        Assert.assertTrue(threadFactory is NamedThreadFactory)

        // Would be nice to check the name and maxFactoryThreads of threadFactory
    }

    @Test
    fun testShutdownNull() {
        // Kind of a dummy test
        shutdown(Mockito.mock(ThreadPoolExecutor::class.java))
    }

    @Test
    fun testShutdown() {
        val threadPoolExecutor =
            Mockito.mock(
                ThreadPoolExecutor::class.java
            )
        Mockito.`when`(threadPoolExecutor.corePoolSize).thenReturn(5)
        shutdown(threadPoolExecutor)
        Assert.assertEquals(-5, ThreadPoolUtils.threadTotal.get())
        Mockito.verify(
            threadPoolExecutor,
            Mockito.times(1)
        ).shutdown()
    }

    @Test
    fun testShutdownNowNull() {
        // Kind of a dummy test
        shutdownNow(Mockito.mock(ThreadPoolExecutor::class.java))
    }

    @Test
    fun testShutdownNow() {
        val threadPoolExecutor =
            Mockito.mock(
                ThreadPoolExecutor::class.java
            )
        Mockito.`when`(threadPoolExecutor.corePoolSize).thenReturn(5)
        shutdownNow(threadPoolExecutor)
        Assert.assertEquals(-5, ThreadPoolUtils.threadTotal.get())
        Mockito.verify(
            threadPoolExecutor,
            Mockito.times(1)
        ).shutdownNow()
    }
}