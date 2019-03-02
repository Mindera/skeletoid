package com.mindera.skeletoid.threads.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ThreadUtilsUnitTests {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new ThreadUtils();
    }

    @Test
    public void testThreadName() {
        //TODO This would be better with a regex that validates against [T# .+]
        assertTrue(ThreadUtils.getCurrentThreadName().startsWith("[T# ") && ThreadUtils.getCurrentThreadName().endsWith("] "));
    }
}
