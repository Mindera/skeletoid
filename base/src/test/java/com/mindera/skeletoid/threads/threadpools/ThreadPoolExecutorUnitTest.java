package com.mindera.skeletoid.threads.threadpools;

import com.mindera.skeletoid.logs.LOG;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LOG.class)
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

    @Test
    public void testExecuteTask() throws InterruptedException {
//        Object mock = mock(Object.class);
//
//        TestRunnable testRunnable = new TestRunnable(mock);
//
//        mThreadPoolExecutor.execute(testRunnable);
//        mThreadPoolExecutor.awaitTermination(1, TimeUnit.SECONDS);
//
//        verify(mock.toString());
    }

    @Test
    public void testExecuteNullTask() {
        mockStatic(LOG.class);

        mThreadPoolExecutor.execute(null);

        verifyStatic(LOG.class);
        LOG.e(eq("ThreadPoolExecutor"), eq("Executing null runnable... ignoring"));
    }

    @Test
    public void testSubmitNullTask() {
        mockStatic(LOG.class);

        mThreadPoolExecutor.submit((Runnable) null);

        verifyStatic(LOG.class);
        LOG.e(eq("ThreadPoolExecutor"), eq("Submitting null runnable... ignoring"));
    }

    private class TestRunnable implements Runnable {

        private Object object;

        public TestRunnable(final Object object) {
            this.object = object;
        }

        @Override
        public void run() {
            object.toString();
        }
    }
}

