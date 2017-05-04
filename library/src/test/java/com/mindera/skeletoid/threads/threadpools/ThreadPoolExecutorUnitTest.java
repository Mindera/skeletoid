package com.mindera.skeletoid.threads.threadpools;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ThreadPoolExecutorUnitTest {

    private static final int CORE_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 15;
    private static final long KEEP_ALIVE = 5;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    private static final NamedThreadFactory THREAD_FACTORY = mock(NamedThreadFactory.class);

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
}
