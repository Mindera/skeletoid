package com.mindera.skeletoid.threads.threadpools

import com.mindera.skeletoid.logs.LOG
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.concurrent.TimeUnit

@RunWith(PowerMockRunner::class)
@PrepareForTest(LOG::class)
class ThreadPoolExecutorUnitTest {

    companion object {
        private const val CORE_POOL_SIZE = 10
        private const val MAX_POOL_SIZE = 15
        private const val KEEP_ALIVE: Long = 5
        private val TIME_UNIT = TimeUnit.MILLISECONDS
    }

    private lateinit var threadFactory: NamedThreadFactory
    private lateinit var threadPoolExecutor: ThreadPoolExecutor

    @Before
    fun setUp() {
        threadFactory = NamedThreadFactory(threadPoolName = "", maxFactoryThreads = 1)
        threadPoolExecutor = ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TIME_UNIT, threadFactory)
    }

    @Test
    fun testConstructor() {
        Assert.assertEquals(CORE_POOL_SIZE, threadPoolExecutor.corePoolSize)
        Assert.assertEquals(MAX_POOL_SIZE, threadPoolExecutor.maximumPoolSize)
        Assert.assertEquals(KEEP_ALIVE, threadPoolExecutor.getKeepAliveTime(TIME_UNIT))
        Assert.assertEquals(threadFactory, threadPoolExecutor.threadFactory)
    }

    @Test
    fun testThreadPoolInitialization() {
        Assert.assertEquals(CORE_POOL_SIZE, threadPoolExecutor.corePoolSize)
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