package com.mindera.skeletoid.threads.threadpools

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ScheduledThreadPoolUnitTest {

    private lateinit var threadFactory: NamedThreadFactory
    private lateinit var threadPoolExecutor: ScheduledThreadPoolExecutor

    @Before
    fun setUp() {
        threadFactory = NamedThreadFactory(threadPoolName = "", maxFactoryThreads = 1)
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

    @Test
    fun testShutdownThreadPoolNullThreadFactory() {
        val threadFactoryAccess = threadPoolExecutor.javaClass.superclass?.superclass?.getDeclaredField("threadFactory")
        threadFactoryAccess?.isAccessible = true
        threadFactoryAccess?.set(threadPoolExecutor, null)
        threadPoolExecutor.shutdown()
        Assert.assertTrue(threadPoolExecutor.isShutdown)
    }

    @Test
    fun testShutdownNowThreadPoolNullThreadFactory() {
        val threadFactoryAccess = threadPoolExecutor.javaClass.superclass?.superclass?.getDeclaredField("threadFactory")
        threadFactoryAccess?.isAccessible = true
        threadFactoryAccess?.set(threadPoolExecutor, null)
        threadPoolExecutor.shutdownNow()
        Assert.assertTrue(threadPoolExecutor.isShutdown)
    }

    @Test
    fun testShutdownThreadPoolNameTest() {
        val threadFactory = NamedThreadFactory(threadPoolName = "SHUTDOWN", maxFactoryThreads = 1)
        val threadPoolExecutor = ScheduledThreadPoolExecutor(1, threadFactory)
        threadPoolExecutor.shutdown()
        Assert.assertTrue(threadPoolExecutor.isShutdown)
    }

    @Test
    fun testShutdownNowThreadPoolNameTest() {
        val threadFactory = NamedThreadFactory(threadPoolName = "SHUTDOWN", maxFactoryThreads = 1)
        val threadPoolExecutor = ScheduledThreadPoolExecutor(1, threadFactory)
        threadPoolExecutor.shutdownNow()
        Assert.assertTrue(threadPoolExecutor.isShutdown)
    }

    @Test
    fun afterExecuteNullArgs() {
        threadPoolExecutor.shutdownNow()
        val method = threadPoolExecutor.javaClass.getDeclaredMethod("afterExecute", Runnable::class.java, Throwable::class.java)
        method.isAccessible = true
        method.invoke(threadPoolExecutor, null, null)
        Assert.assertTrue(threadPoolExecutor.isShutdown)
    }

    @Test
    fun afterExecuteWithFuture() {
        val future = threadPoolExecutor.submit { }
        threadPoolExecutor.shutdownNow()
        val method = threadPoolExecutor.javaClass.getDeclaredMethod("afterExecute", Runnable::class.java, Throwable::class.java)
        method.isAccessible = true
        method.invoke(threadPoolExecutor, future, null)
        Assert.assertTrue(threadPoolExecutor.isShutdown)
    }

}
