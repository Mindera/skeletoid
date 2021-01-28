package com.mindera.skeletoid.rxjava

import io.reactivex.Completable
import org.junit.Assert.assertTrue
import org.junit.Test

class CompletableExtensionsTest {

    @Test
    fun testCompletableDelayAtLeast() {
        val timeToWait = 2000L
        val time = System.currentTimeMillis()

        Completable.fromAction { }
            .delayAtLeast(timeToWait)
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()

        assertTrue (System.currentTimeMillis() - time >= timeToWait)
    }
}