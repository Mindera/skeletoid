package com.mindera.skeletoid.rxjava

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CompletableExtensionsTest {

    @Test
    fun testCompletableSubscribeOnIO() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }

        var thread: String? = null
        Completable.fromAction { thread = Thread.currentThread().name }
            .subscribeOnIO()
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testCompletableObserveOnIO() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }

        var thread: String? = null
        Completable.fromAction { }
            .observeOnIO()
            .doOnComplete { thread = Thread.currentThread().name }
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testCompletableSubscribeOnComputation() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        var thread: String? = null
        Completable.fromAction { thread = Thread.currentThread().name }
            .subscribeOnComputation()
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testCompletableObserveOnComputation() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        var thread: String? = null
        Completable.fromAction { }
            .observeOnComputation()
            .doOnComplete { thread = Thread.currentThread().name }
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testCompletableSubscribeOnMain() {
        var thread: String? = null
        Completable.fromAction { thread = Thread.currentThread().name }
            .subscribeOnMain()
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testCompletableObserveOnMain() {
        var thread: String? = null
        Completable.fromAction { }
            .observeOnMain()
            .doOnComplete { thread = Thread.currentThread().name }
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testCompletableDelayAtLeast() {
        val timeToWait = 2000L
        val time = System.currentTimeMillis()
        Completable.fromAction { }
            .delayAtLeast(timeToWait)
            .blockingGet() // A bit of 🔨 but..

        assertTrue (System.currentTimeMillis() - time >= timeToWait)
    }
}