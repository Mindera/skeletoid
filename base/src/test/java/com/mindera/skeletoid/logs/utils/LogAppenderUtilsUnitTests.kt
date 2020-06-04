package com.mindera.skeletoid.logs.utils

import com.mindera.skeletoid.generic.AndroidUtils
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getLogString
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getMethodName
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getTag
import junit.framework.Assert
import org.junit.Test

class LogAppenderUtilsUnitTests {
    private val mPackageName = "my.package.name"

    @Test
    fun testGetLogString() {
        Assert.assertEquals("A B C ", getLogString("A", "B", "C"))
    }

    @Test
    fun testGetLogStringEmptyString() {
        Assert.assertEquals("", getLogString())
    }

    @Test
    fun testGetMethodName() {
        Assert.assertEquals(
            "testGetMethodName", getMethodName(
                LogAppenderUtilsUnitTests::class.java
            )
        )
    }

    @Test
    fun testGetMethodNameInvalid() {
        Assert.assertEquals(
            "UnknownMethod", getMethodName(
                AndroidUtils::class.java
            )
        )
    }

    @Test
    fun testGetTag() {
        Assert.assertEquals(
            LogAppenderUtilsUnitTests::class.java.canonicalName, getTag(
                LogAppenderUtilsUnitTests::class.java, false, mPackageName, false
            )
        )
    }

    @Test
    fun testGetTagWithPackageName() {
        Assert.assertEquals(
            mPackageName + "/" + LogAppenderUtilsUnitTests::class.java.canonicalName,
            getTag(
                LogAppenderUtilsUnitTests::class.java, true, mPackageName, false
            )
        )
    }

    @Test
    fun testGetTagWithPackageNameAndMethodName() {
        Assert.assertEquals(
            mPackageName + "/" + LogAppenderUtilsUnitTests::class.java.canonicalName + ".testGetTagWithPackageNameAndMethodName",
            getTag(LogAppenderUtilsUnitTests::class.java, true, mPackageName, true)
        )
    }

    @Test
    fun testGetTagWithMethodName() {
        Assert.assertEquals(
            LogAppenderUtilsUnitTests::class.java.canonicalName + ".testGetTagWithMethodName",
            getTag(
                LogAppenderUtilsUnitTests::class.java,
                false,
                mPackageName,
                true
            )
        )
    }

    @Test
    fun testGetObjectHash() {
        Assert.assertEquals(
            "[LogAppenderUtilsUnitTests#" + this.hashCode() + "] ",
            getObjectHash(this)
        )
    }
}