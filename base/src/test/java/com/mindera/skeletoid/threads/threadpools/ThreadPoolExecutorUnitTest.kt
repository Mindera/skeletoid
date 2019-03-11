package com.mindera.skeletoid.threads.threadpools

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import java.util.concurrent.TimeUnit

class ThreadPoolExecutorUnitTest {

    private var mThreadPoolExecutor: ThreadPoolExecutor? = null

    @Before
    fun setUp() {
        mThreadPoolExecutor =
            ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TIME_UNIT, THREAD_FACTORY)
    }

    @Test
    fun testConstructor() {
        assertEquals(CORE_POOL_SIZE, mThreadPoolExecutor!!.corePoolSize)
        assertEquals(MAX_POOL_SIZE, mThreadPoolExecutor!!.maximumPoolSize)
        assertEquals(KEEP_ALIVE, mThreadPoolExecutor!!.getKeepAliveTime(TIME_UNIT))
        assertEquals(THREAD_FACTORY, mThreadPoolExecutor!!.threadFactory)
    }


    @Test
    fun testThreadPoolInitialization() {
        assertEquals(CORE_POOL_SIZE, mThreadPoolExecutor!!.corePoolSize)
    }


    @Test
    fun testShutdownThreadPool() {
        mThreadPoolExecutor!!.shutdown()
        assertTrue(mThreadPoolExecutor!!.isShutdown)
    }


    @Test
    fun testShutdownNowThreadPool() {
        mThreadPoolExecutor!!.shutdownNow()
        assertTrue(mThreadPoolExecutor!!.isShutdown)
    }

    companion object {

        private val CORE_POOL_SIZE = 10
        private val MAX_POOL_SIZE = 15
        private val KEEP_ALIVE: Long = 5
        private val TIME_UNIT = TimeUnit.MILLISECONDS
        private val THREAD_FACTORY = mock(NamedThreadFactory::class.java)
    }
}
