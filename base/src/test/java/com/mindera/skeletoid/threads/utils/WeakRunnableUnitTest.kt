package com.mindera.skeletoid.threads.utils

import org.junit.Test
import org.mockito.Mockito

class WeakRunnableUnitTest {
    @Test
    fun testWeakRunnable() {
        val weakRunnable = WeakRunnable({})
        weakRunnable.run()
        Mockito.verify(weakRunnable, Mockito.times(1)).run()
    }
}