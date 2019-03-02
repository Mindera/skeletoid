package com.mindera.skeletoid.threads.threadpools;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThreadPoolExecutorUnitTest {

    private static final int CORE_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 15;
    private static final long KEEP_ALIVE = 5;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    private static final NamedThreadFactory THREAD_FACTORY = Mockito.mock(NamedThreadFactory.class);

    private ThreadPoolExecutor mThreadPoolExecutor;

    @Before
    public void setUp() {
        mThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TIME_UNIT, THREAD_FACTORY);
    }

    @Test
    public void testConstructor() {
        assertEquals(CORE_POOL_SIZE, mThreadPoolExecutor.getCorePoolSize());
        assertEquals(MAX_POOL_SIZE, mThreadPoolExecutor.getMaximumPoolSize());
        assertEquals(KEEP_ALIVE, mThreadPoolExecutor.getKeepAliveTime(TIME_UNIT));
        assertEquals(THREAD_FACTORY, mThreadPoolExecutor.getThreadFactory());
    }

    @Test
    public void testThreadPoolInitialization() {
        assertEquals(CORE_POOL_SIZE, mThreadPoolExecutor.getCorePoolSize());
    }

    @Test
    public void testShutdownThreadPool() {
        mThreadPoolExecutor.shutdown();
        assertTrue(mThreadPoolExecutor.isShutdown());
    }

    @Test
    public void testShutdownNowThreadPool() {
        mThreadPoolExecutor.shutdownNow();
        assertTrue(mThreadPoolExecutor.isShutdown());
    }
}
