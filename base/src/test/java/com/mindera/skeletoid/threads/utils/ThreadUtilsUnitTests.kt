package com.mindera.skeletoid.threads.utils

import com.mindera.skeletoid.threads.utils.ThreadUtils.currentThreadName
import org.junit.Assert
import org.junit.Test

class ThreadUtilsUnitTests {
    @Test
    fun testThreadName() {
        //TODO This would be better with a regex that validates against [T# .+]
        Assert.assertTrue(
            currentThreadName.startsWith("[T# ") && currentThreadName.endsWith("] ")
        )
    }
}
