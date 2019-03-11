package com.mindera.skeletoid.threads.threadpools

import com.mindera.skeletoid.threads.threadpools.ThreadPoolUtils.totalThreads
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ThreadPoolUtilsUnitTest {

    @Before
    fun beforeTest() {
        ThreadPoolUtils.totalThreads.set(0)
    }

    @Test
    fun testGetFixedThreadPool() {
        val maxThreads = 10
        val threadPoolName = "testPool"

        val threadPool = ThreadPoolUtils.getFixedThreadPool(threadPoolName, maxThreads)

        assertEquals(maxThreads, threadPool.corePoolSize)
        assertEquals(maxThreads, threadPool.maximumPoolSize)
        assertEquals(0, threadPool.getKeepAliveTime(TimeUnit.MILLISECONDS))

        val threadFactory = threadPool.threadFactory
        assertNotNull(threadFactory)
        assertTrue(threadFactory is NamedThreadFactory)

        // Would be nice to check the name and maxFactoryThreads of threadFactory
    }

    @Test
    fun testGetScheduledThreadPool() {
        val maxThreads = 10
        val threadPoolName = "testPool"

        val scheduledThreadPool = ThreadPoolUtils.getScheduledThreadPool(threadPoolName, maxThreads)

        assertEquals(maxThreads, scheduledThreadPool.corePoolSize)

        val threadFactory = scheduledThreadPool.threadFactory
        assertNotNull(threadFactory)
        assertTrue(threadFactory is NamedThreadFactory)

        // Would be nice to check the name and maxFactoryThreads of threadFactory
    }

    @Test
    fun testShutdown() {

        val threadPoolExecutor = mock(ThreadPoolExecutor::class.java)
        `when`(threadPoolExecutor.corePoolSize).thenReturn(5)

        ThreadPoolUtils.shutdown(threadPoolExecutor)
        assertEquals(-5, totalThreads.get())

        verify(threadPoolExecutor, times(1)).shutdown()
    }

    @Test
    fun testShutdownNow() {

        val threadPoolExecutor = mock(ThreadPoolExecutor::class.java)
        `when`(threadPoolExecutor.corePoolSize).thenReturn(5)

        ThreadPoolUtils.shutdownNow(threadPoolExecutor)
        assertEquals(-5, totalThreads.get())

        verify(threadPoolExecutor, times(1)).shutdownNow()
    }
}
