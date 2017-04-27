package com.mindera.skeletoid.threadpools;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class NamedThreadFactoryUnitTest {

    @Test
    public void testGetThreads() {
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory("name", 10);

        assertNotNull(namedThreadFactory.getThreads());
        assertEquals(0, namedThreadFactory.getThreads().size());
    }

    @Test
    public void testClearThreads() {
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory("name", 10);

        namedThreadFactory.clearThreads();
        assertNotNull(namedThreadFactory.getThreads());
        assertEquals(0, namedThreadFactory.getThreads().size());
    }
}
