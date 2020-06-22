package com.mindera.skeletoid.logs.utils

import com.mindera.skeletoid.generic.AndroidUtils
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getLogString
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getMethodName
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getTag
import org.junit.Assert
import org.junit.Test

class LogAppenderUtilsUnitTests {
    companion object{
        private const val packageName = "my.package.name"
    }

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
                LogAppenderUtilsUnitTests::class.java, false, packageName, false
            )
        )
    }

    @Test
    fun testGetTagWithPackageName() {
        Assert.assertEquals(
            packageName + "/" + LogAppenderUtilsUnitTests::class.java.canonicalName,
            getTag(
                LogAppenderUtilsUnitTests::class.java, true, packageName, false
            )
        )
    }

    @Test
    fun testGetTagWithPackageNameAndMethodName() {
        Assert.assertEquals(
            packageName + "/" + LogAppenderUtilsUnitTests::class.java.canonicalName + ".testGetTagWithPackageNameAndMethodName",
            getTag(LogAppenderUtilsUnitTests::class.java, true, packageName, true)
        )
    }

    @Test
    fun testGetTagWithMethodName() {
        Assert.assertEquals(
            LogAppenderUtilsUnitTests::class.java.canonicalName + ".testGetTagWithMethodName",
            getTag(
                LogAppenderUtilsUnitTests::class.java,
                false,
                packageName,
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