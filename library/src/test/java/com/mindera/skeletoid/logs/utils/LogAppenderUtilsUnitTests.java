package com.mindera.skeletoid.logs.utils;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class LogAppenderUtilsUnitTests {

    @Test
    public void testGetLogString() {
        assertEquals(LogAppenderUtils.getLogString("A", "B", "C"), "A B C ");
    }

    @Test
    public void testGetMethodName() {
        assertEquals(LogAppenderUtils.getMethodName(LogAppenderUtilsUnitTests.class), "invoke0");
    }

    @Test
    public void testGetTag() {
        assertEquals(LogAppenderUtils.getTag(LogAppenderUtilsUnitTests.class, false, false), LogAppenderUtilsUnitTests.class.getCanonicalName());
    }

    @Test
    public void testGetObjectHash() {
        assertEquals(LogAppenderUtils.getObjectHash(this), "[OID#" + this.hashCode() + "] ");
    }
}
