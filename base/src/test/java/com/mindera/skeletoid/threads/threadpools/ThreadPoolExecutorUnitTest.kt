package com.mindera.skeletoid.threads.threadpools

import com.mindera.skeletoid.logs.LOG
import com.mindera.skeletoid.logs.LOG.e
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
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
        private val TIME_UNIT =
            TimeUnit.MILLISECONDS
        private val THREAD_FACTORY =
            Mockito.mock(
                NamedThreadFactory::class.java
            )
    }

    private var threadPoolExecutor: ThreadPoolExecutor? =
        null

    @Before
    fun setUp() {
        threadPoolExecutor = ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE,
            TIME_UNIT,
            THREAD_FACTORY
        )
    }

    @Test
    fun testConstructor() {
        Assert.assertEquals(
            CORE_POOL_SIZE,
            threadPoolExecutor!!.corePoolSize
        )
        Assert.assertEquals(
            MAX_POOL_SIZE,
            threadPoolExecutor!!.maximumPoolSize
        )
        Assert.assertEquals(
            KEEP_ALIVE,
            threadPoolExecutor!!.getKeepAliveTime(TIME_UNIT)
        )
        Assert.assertEquals(
            THREAD_FACTORY,
            threadPoolExecutor!!.threadFactory
        )
    }

    @Test
    fun testThreadPoolInitialization() {
        Assert.assertEquals(
            CORE_POOL_SIZE,
            threadPoolExecutor!!.corePoolSize
        )
    }

    @Test
    fun testShutdownThreadPool() {
        threadPoolExecutor!!.shutdown()
        Assert.assertTrue(threadPoolExecutor!!.isShutdown)
    }

    @Test
    fun testShutdownNowThreadPool() {
        threadPoolExecutor!!.shutdownNow()
        Assert.assertTrue(threadPoolExecutor!!.isShutdown)
    }

    @Test
    fun testExecuteNullTask() {
        PowerMockito.mockStatic(LOG::class.java)
        threadPoolExecutor!!.execute(null)
        PowerMockito.verifyStatic(LOG::class.java)
        e(
            ArgumentMatchers.eq("ThreadPoolExecutor"),
            ArgumentMatchers.eq("Executing null runnable... ignoring")
        )
    }
}