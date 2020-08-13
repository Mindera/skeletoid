package com.mindera.skeletoid.rxjava

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CompletableExtensionsTest {

    // ATTENTION !!!!
    // This is hitting main in all threadpools since we are defaulting to TRAMPOLINE on QA builds.
    // It would be great to not do it, but that would break Apps using QA build to UI Tests

    @Test
    fun testCompletableSubscribeOnIO() {
        var thread: String? = null
        Completable.fromAction { thread = Thread.currentThread().name }
            .subscribeOnIO()
            .blockingGet()

        assertEquals(thread, "main") // To be fixed: read above
    }

    @Test
    fun testCompletableObserveOnIO() {
        var thread: String? = null
        Completable.fromAction { }
            .observeOnIO()
            .doOnComplete { thread = Thread.currentThread().name }
            .blockingGet()

        assertEquals(thread, "main")  // To be fixed: read above
    }

    @Test
    fun testCompletableSubscribeOnComputation() {
        var thread: String? = null
        Completable.fromAction { thread = Thread.currentThread().name }
            .subscribeOnComputation()
            .blockingGet()

        assertEquals(thread, "main") // To be fixed: read above
    }

    @Test
    fun testCompletableObserveOnComputation() {
        var thread: String? = null
        Completable.fromAction { }
            .observeOnComputation()
            .doOnComplete { thread = Thread.currentThread().name }
            .blockingGet()

        assertEquals(thread, "main") // To be fixed: read above
    }

    @Test
    fun testCompletableSubscribeOnMain() {
        var thread: String? = null
        Completable.fromAction { thread = Thread.currentThread().name }
            .subscribeOnMain()
            .blockingGet()

        assertEquals(thread, "main")
    }

    @Test
    fun testCompletableObserveOnMain() {
        var thread: String? = null
        Completable.fromAction { }
            .observeOnMain()
            .doOnComplete { thread = Thread.currentThread().name }
            .blockingGet()

        assertEquals(thread, "main")
    }

    @Test
    fun testCompletableDelayAtLeast() {
        val timeToWait = 2000L
        val time = System.currentTimeMillis()
        Completable.fromAction { }
            .delayAtLeast(timeToWait)
            .blockingGet()

        assertTrue (System.currentTimeMillis() - time >= timeToWait)
    }
}