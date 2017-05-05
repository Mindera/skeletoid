package com.mindera.skeletoid.logs.utils;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class LogAppenderUtilsUnitTests {

    private String mPackageName = "my.package.name";

    @Test
    public void testGetLogString() {
        assertEquals("A B C ", LogAppenderUtils.getLogString("A", "B", "C"));
    }

    @Test
    public void testGetMethodName() {
        assertEquals("testGetMethodName", LogAppenderUtils.getMethodName(LogAppenderUtilsUnitTests.class));
    }

    @Test
    public void testGetTag() {
        assertEquals(LogAppenderUtilsUnitTests.class.getCanonicalName(), LogAppenderUtils.getTag(LogAppenderUtilsUnitTests.class, false, mPackageName, false));
    }

    @Test
    public void testGetTagWithPackageName() {
        assertEquals(mPackageName + "/" + LogAppenderUtilsUnitTests.class.getCanonicalName(), LogAppenderUtils.getTag(LogAppenderUtilsUnitTests.class, true, mPackageName, false));
    }

    @Test
    public void testGetObjectHash() {
        assertEquals("[OID#" + this.hashCode() + "] ", LogAppenderUtils.getObjectHash(this));
    }

    @Test
    public void testGetObjectHashNull() {
        assertEquals("[!!!NULL INSTANCE!!!]", LogAppenderUtils.getObjectHash(this));
    }


}
