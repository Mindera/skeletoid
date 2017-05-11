package com.mindera.skeletoid.logs.utils;

import com.mindera.skeletoid.generic.AndroidUtils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class LogAppenderUtilsUnitTests {

    private String mPackageName = "my.package.name";

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new LogAppenderUtils();
    }

    @Test
    public void testGetLogString() {
        assertEquals("A B C ", LogAppenderUtils.getLogString("A", "B", "C"));
    }

    @Test
    public void testGetLogStringNull() {
        assertEquals("", LogAppenderUtils.getLogString(new String[0]));
    }

    @Test
    public void testGetMethodName() {
        assertEquals("testGetMethodName", LogAppenderUtils.getMethodName(LogAppenderUtilsUnitTests.class));
    }

    @Test
    public void testGetMethodNameInvalid() {
        assertEquals("UnknownMethod", LogAppenderUtils.getMethodName(AndroidUtils.class));
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
    public void testGetTagWithPackageNameAndMethodName() {
        assertEquals(mPackageName + "/" + LogAppenderUtilsUnitTests.class.getCanonicalName() + ".testGetTagWithPackageNameAndMethodName",
                LogAppenderUtils.getTag(LogAppenderUtilsUnitTests.class, true, mPackageName, true));
    }

    @Test
    public void testGetTagWithMethodName() {
        assertEquals(LogAppenderUtilsUnitTests.class.getCanonicalName() + ".testGetTagWithMethodName",
                LogAppenderUtils.getTag(LogAppenderUtilsUnitTests.class, false, mPackageName, true));
    }

    @Test
    public void testGetObjectHash() {
        assertEquals("[OID#" + this.hashCode() + "] ", LogAppenderUtils.getObjectHash(this));
    }

    @Test
    public void testGetObjectHashNull() {
        assertEquals("[!!!NULL INSTANCE!!!] ", LogAppenderUtils.getObjectHash(null));
    }


}
