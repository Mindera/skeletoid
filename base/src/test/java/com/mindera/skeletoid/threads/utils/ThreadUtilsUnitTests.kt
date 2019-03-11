package com.mindera.skeletoid.threads.utils

import junit.framework.Assert.assertTrue
import org.junit.Test

class ThreadUtilsUnitTests {

    @Test
    fun testThreadName() {
        //TODO This would be better with a regex that validates against [T# .+]
        assertTrue(
            ThreadUtils.getCurrentThreadName().startsWith("[T# ") && ThreadUtils.getCurrentThreadName().endsWith(
                "] "
            )
        )
    }
}
