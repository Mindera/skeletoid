package com.mindera.skeletoid.threads.threadpools

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Test
import org.mockito.Mockito.mock

class NamedThreadFactoryUnitTest {

    @Test
    fun testGetThreads() {
        val namedThreadFactory = NamedThreadFactory("name", 10)

        assertNotNull(namedThreadFactory.threads)
        assertEquals(0, namedThreadFactory.threads.size)
    }

    @Test
    fun testNewThreads() {
        val runnable = mock(Runnable::class.java)
        val namedThreadFactory = NamedThreadFactory("name", 10)
        val thread = namedThreadFactory.newThread(runnable)

        assertNotNull(thread)
        assertEquals(1, namedThreadFactory.threads.size)
    }

    @Test
    fun testClearThreads() {
        val namedThreadFactory = NamedThreadFactory("name", 10)

        namedThreadFactory.clearThreads()
        assertNotNull(namedThreadFactory.threads)
        assertEquals(0, namedThreadFactory.threads.size)
    }
}
