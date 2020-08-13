package com.mindera.skeletoid.rxjava

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.functions.Predicate
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MaybeExtensionsTest {

    @Test
    fun testMaybeSubscribeOnIO() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }

        var thread: String? = null
        Maybe.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnIO()
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testMaybeObserveOnIO() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }

        var thread: String? = null
        Maybe.fromCallable { }
            .observeOnIO()
            .doOnComplete { thread = Thread.currentThread().name }
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testMaybeSubscribeOnComputation() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        var thread: String? = null
        Maybe.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnComputation()
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testMaybeObserveOnComputation() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        var thread: String? = null
        Maybe.fromCallable { }
            .observeOnComputation()
            .doOnComplete { thread = Thread.currentThread().name }
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testMaybeSubscribeOnMain() {
        var thread: String? = null
        Maybe.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnMain()
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testMaybeObserveOnMain() {
        var thread: String? = null
        Maybe.fromCallable { }
            .observeOnMain()
            .doOnComplete { thread = Thread.currentThread().name }
            .test()

        assertEquals(thread, "main")
    }

    @Test
    fun testFilterOrElseWithConditionFalse() {
        val list = mutableListOf<Int>()
        val subject = PublishSubject.create<Int>()
        subject
            .doOnNext { list.add(it) }
            .test()

        Maybe.just(1)
            .filterOrElse(false) {
                subject.onNext(1)
            }
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertNoValues()

        assertEquals(listOf(1), list)
    }

    @Test
    fun testFilterOrElseWithConditionTrue() {
        val list = mutableListOf<Int>()
        val subject = PublishSubject.create<Int>()
        subject
            .doOnNext { list.add(it) }
            .test()

        Maybe.just(1)
            .filterOrElse(true) {
                subject.onNext(1)
            }
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValues(1)

        assertEquals(emptyList<Int>(), list)
    }
}