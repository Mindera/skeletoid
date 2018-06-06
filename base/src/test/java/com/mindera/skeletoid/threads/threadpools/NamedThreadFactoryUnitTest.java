package com.mindera.skeletoid.threads.threadpools;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class NamedThreadFactoryUnitTest {

    @Test
    public void testGetThreads() {
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory("name", 10);

        assertNotNull(namedThreadFactory.getThreads());
        assertEquals(0, namedThreadFactory.getThreads().size());
    }

    @Test
    public void testNewThreads() {
        Runnable runnable = mock(Runnable.class);
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory("name", 10);
        Thread thread = namedThreadFactory.newThread(runnable);

        assertNotNull(thread);
        assertEquals(1, namedThreadFactory.getThreads().size());
    }

    @Test
    public void testClearThreads() {
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory("name", 10);

        namedThreadFactory.clearThreads();
        assertNotNull(namedThreadFactory.getThreads());
        assertEquals(0, namedThreadFactory.getThreads().size());
    }
}
