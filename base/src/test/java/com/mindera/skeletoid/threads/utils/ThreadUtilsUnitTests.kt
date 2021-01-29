package com.mindera.skeletoid.threads.utils

import org.junit.Assert
import org.junit.Test

class ThreadUtilsUnitTests {

    companion object {
        private const val REGEX = "^\\[T# .*] $"
    }

    @Test
    fun `test regex`() {
        val threadName = "[T# main] "
        val otherThreadName = "T# main"
        val yetAnotherThreadName = "[T# main]"
        Assert.assertTrue(threadName.contains(REGEX.toRegex()))
        Assert.assertFalse(otherThreadName.contains(REGEX.toRegex()))
        Assert.assertFalse(yetAnotherThreadName.contains(REGEX.toRegex()))
    }

    @Test
    fun `test thread name`() {
        Assert.assertTrue(ThreadUtils.currentThreadName.contains(REGEX.toRegex()))
    }
}
