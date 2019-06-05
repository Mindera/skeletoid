package com.mindera.skeletoid.threads.threadpools;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ScheduledThreadPoolUnitTest {

    @Test
    public void testThreadPoolInitialization() {
        NamedThreadFactory namedThreadFactory = mock(NamedThreadFactory.class);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1,namedThreadFactory);

        assertEquals(1, scheduledThreadPoolExecutor.getCorePoolSize());
    }


    @Test
    public void testShutdownThreadPool() {
        NamedThreadFactory namedThreadFactory = mock(NamedThreadFactory.class);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1,namedThreadFactory);

        scheduledThreadPoolExecutor.shutdown();
        assertTrue(scheduledThreadPoolExecutor.isShutdown());
    }


    @Test
    public void testShutdownNowThreadPool() {
        NamedThreadFactory namedThreadFactory = mock(NamedThreadFactory.class);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1,namedThreadFactory);

        scheduledThreadPoolExecutor.shutdownNow();
        assertTrue(scheduledThreadPoolExecutor.isShutdown());
    }

}
