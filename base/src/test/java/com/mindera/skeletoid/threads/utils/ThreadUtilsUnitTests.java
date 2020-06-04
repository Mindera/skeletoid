package com.mindera.skeletoid.threads.utils;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class ThreadUtilsUnitTests {

    @Test
    public void testThreadName() {
        //TODO This would be better with a regex that validates against [T# .+]
        assertTrue(ThreadUtils.getCurrentThreadName().startsWith("[T# ") && ThreadUtils.getCurrentThreadName().endsWith("] "));
    }
}
