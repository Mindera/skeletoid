package com.mindera.skeletoid.threads.utils

import com.mindera.skeletoid.threads.utils.ThreadUtils.currentThreadName
import org.junit.Assert
import org.junit.Test

class ThreadUtilsUnitTests {

    companion object {
        private const val REGEX = "^\\[T# .*] $"
    }

    @Test
    fun testRegex() {
        val threadName = "[T# main] "
        val otherThreadName = "T# main"
        val yetAnotherThreadName = "[T# main]"
        Assert.assertTrue(threadName.contains(REGEX.toRegex()))
        Assert.assertFalse(otherThreadName.contains(REGEX.toRegex()))
        Assert.assertFalse(yetAnotherThreadName.contains(REGEX.toRegex()))
    }

    @Test
    fun testThreadName() {
        Assert.assertTrue(currentThreadName.contains(REGEX.toRegex()))
    }
}
