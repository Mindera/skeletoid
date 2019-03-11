package com.mindera.skeletoid.threads.threadpools

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Created by Pedro Vicente - pedro.vicente@mindera.com
 * File created on 14/08/2017.
 */

class ScheduledThreadPoolUnitTest {

    @Test
    fun testThreadPoolInitialization() {
        val namedThreadFactory = mock(NamedThreadFactory::class.java)
        val scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1, namedThreadFactory)

        assertEquals(1, scheduledThreadPoolExecutor.corePoolSize)
    }


    @Test
    fun testShutdownThreadPool() {
        val namedThreadFactory = mock(NamedThreadFactory::class.java)
        val scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1, namedThreadFactory)

        scheduledThreadPoolExecutor.shutdown()
        assertTrue(scheduledThreadPoolExecutor.isShutdown)
    }


    @Test
    fun testShutdownNowThreadPool() {
        val namedThreadFactory = mock(NamedThreadFactory::class.java)
        val scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1, namedThreadFactory)

        scheduledThreadPoolExecutor.shutdownNow()
        assertTrue(scheduledThreadPoolExecutor.isShutdown)
    }

}
