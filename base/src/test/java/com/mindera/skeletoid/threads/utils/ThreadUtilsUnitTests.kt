package com.mindera.skeletoid.threads.utils

import org.junit.Assert
import org.junit.Test

class ThreadUtilsUnitTests {

    companion object {
        private const val THREAD_NAME_REGEX = "^\\[T# .*] $"
    }

    @Test
    fun `test regex`() {
        val threadName = "[T# main] "
        val otherThreadName = "T# main"
        val yetAnotherThreadName = "[T# main]"
        Assert.assertTrue(threadName.contains(THREAD_NAME_REGEX.toRegex()))
        Assert.assertFalse(otherThreadName.contains(THREAD_NAME_REGEX.toRegex()))
        Assert.assertFalse(yetAnotherThreadName.contains(THREAD_NAME_REGEX.toRegex()))
    }

    @Test
    fun `test thread name`() {
        Assert.assertTrue(ThreadUtils.currentThreadName.contains(THREAD_NAME_REGEX.toRegex()))
    }
}
