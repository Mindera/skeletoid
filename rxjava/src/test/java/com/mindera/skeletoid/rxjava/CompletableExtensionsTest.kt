package com.mindera.skeletoid.rxjava

import io.reactivex.Completable
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CompletableExtensionsTest {

    // ATTENTION !!!!
    // This is hitting main in all threadpools since we are defaulting to TRAMPOLINE on QA builds.
    // It would be great to not do it, but that would break Apps using QA build to UI Tests

    private fun threadName(): String = Thread.currentThread().name

    private fun assertMainThread() {
        assertEquals("main", threadName())
    }

    @Test
    fun testCompletableSubscribeOnIO() {
        Completable.fromCallable { assertMainThread() }
            .subscribeOnIO()
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testCompletableObserveOnIO() {
        Completable.fromAction { }
            .observeOnIO()
            .doOnComplete { assertMainThread() }
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testCompletableSubscribeOnComputation() {
        Completable.fromCallable { assertMainThread() }
            .subscribeOnComputation()
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testCompletableObserveOnComputation() {
        Completable.fromAction { }
            .observeOnComputation()
            .doOnComplete { assertMainThread() }
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testCompletableSubscribeOnMain() {
        Completable.fromCallable { assertMainThread() }
            .subscribeOnMain()
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testCompletableObserveOnMain() {
        Completable.fromAction { }
            .observeOnMain()
            .doOnComplete { assertMainThread() }
            .test()
            .assertNoErrors()
            .assertComplete()
    }

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