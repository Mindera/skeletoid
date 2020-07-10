package com.mindera.skeletoid.threads.threadpools

import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class NamedThreadFactoryUnitTest {

    lateinit var namedThreadFactory : NamedThreadFactory

    @Before
    fun setUp() {
        namedThreadFactory = NamedThreadFactory("name", 10)
    }

    @Test
    fun testGetThreads() {
        Assert.assertNotNull(namedThreadFactory.threads)
        Assert.assertEquals(0, namedThreadFactory.threads.size)
    }

    @Test
    fun testNewThreads() {
        val runnable =
            Mockito.mock(Runnable::class.java)
        val thread = namedThreadFactory.newThread(runnable)
        Assert.assertNotNull(thread)
        Assert.assertEquals(1, namedThreadFactory.threads.size)
    }

    @Test
    fun testClearThreads() {
        namedThreadFactory.clearThreads()
        Assert.assertNotNull(namedThreadFactory.threads)
        Assert.assertEquals(0, namedThreadFactory.threads.size)
    }
}