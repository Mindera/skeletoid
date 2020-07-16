package com.mindera.skeletoid.threads.threadpools

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Future
import java.util.concurrent.RunnableScheduledFuture

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

    @Test
    fun testShutdownThreadPoolNameTest() {
        val threadFactory = NamedThreadFactory("SHUTDOWN", 1)
        val threadPoolExecutor = ScheduledThreadPoolExecutor(1, threadFactory)
        threadPoolExecutor.shutdown()
        Assert.assertTrue(threadPoolExecutor.isShutdown)
    }

    @Test
    fun testShutdownNowThreadPoolNameTest() {
        val threadFactory = NamedThreadFactory("SHUTDOWN", 1)
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
        val future = threadPoolExecutor.submit(Runnable {  })
        threadPoolExecutor.shutdownNow()
        val method = threadPoolExecutor.javaClass.getDeclaredMethod("afterExecute", Runnable::class.java, Throwable::class.java)
        method.isAccessible = true
        method.invoke(threadPoolExecutor, future, null)
        Assert.assertTrue(threadPoolExecutor.isShutdown)
    }

}
