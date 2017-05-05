package com.mindera.skeletoid.threads.utils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ThreadUtilsUnitTests {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new ThreadUtils();
    }

    @Test
    public void testThreadName() {
        assertEquals("[T# Test worker] ", ThreadUtils.getCurrentThreadName());
    }
}
