package com.mindera.skeletoid.threads.threadpools;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.mindera.skeletoid.threads.threadpools.ThreadPoolUtils.mThreadTotal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ThreadPoolUtilsUnitTest {

    @Before
    public void beforeTest() {
        ThreadPoolUtils.mThreadTotal.set(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new ThreadPoolUtils();
    }

    @Test
    public void testGetFixedThreadPool() {
        int maxThreads = 10;
        String threadPoolName = "testPool";

        ThreadPoolExecutor threadPool = ThreadPoolUtils.getFixedThreadPool(threadPoolName, maxThreads);

        assertEquals(maxThreads, threadPool.getCorePoolSize());
        assertEquals(maxThreads, threadPool.getMaximumPoolSize());
        assertEquals(0, threadPool.getKeepAliveTime(TimeUnit.MILLISECONDS));

        ThreadFactory threadFactory = threadPool.getThreadFactory();
        assertNotNull(threadFactory);
        assertTrue(threadFactory instanceof NamedThreadFactory);

        // Would be nice to check the name and maxFactoryThreads of threadFactory
    }

    @Test
    public void testGetScheduledThreadPool() {
        int maxThreads = 10;
        String threadPoolName = "testPool";

        ScheduledThreadPoolExecutor scheduledThreadPool = ThreadPoolUtils.getScheduledThreadPool(threadPoolName, maxThreads);

        assertEquals(maxThreads, scheduledThreadPool.getCorePoolSize());

        ThreadFactory threadFactory = scheduledThreadPool.getThreadFactory();
        assertNotNull(threadFactory);
        assertTrue(threadFactory instanceof NamedThreadFactory);

        // Would be nice to check the name and maxFactoryThreads of threadFactory
    }

    @Test
    public void testShutdownNull() {
        // Kind of a dummy test
        ThreadPoolUtils.shutdown(null);
    }

    @Test
    public void testShutdown() {

        ThreadPoolExecutor threadPoolExecutor = mock(ThreadPoolExecutor.class);
        when(threadPoolExecutor.getCorePoolSize()).thenReturn(5);

        ThreadPoolUtils.shutdown(threadPoolExecutor);
        assertEquals(-5, mThreadTotal.get());

        verify(threadPoolExecutor, times(1)).shutdown();
    }

    @Test
    public void testShutdownNowNull() {
        // Kind of a dummy test
        ThreadPoolUtils.shutdownNow(null);
    }

    @Test
    public void testShutdownNow() {

        ThreadPoolExecutor threadPoolExecutor = mock(ThreadPoolExecutor.class);
        when(threadPoolExecutor.getCorePoolSize()).thenReturn(5);

        ThreadPoolUtils.shutdownNow(threadPoolExecutor);
        assertEquals(-5, mThreadTotal.get());

        verify(threadPoolExecutor, times(1)).shutdownNow();
    }
}
