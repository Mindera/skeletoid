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

    // ATTENTION !!!!
    // This is hitting main in all threadpools since we are defaulting to TRAMPOLINE on QA builds.
    // It would be great to not do it, but that would break Apps using QA build to UI Tests

    @Test
    fun testMaybeSubscribeOnIO() {
        var thread: String? = null
        Maybe.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnIO()
            .blockingGet()

        assertEquals(thread, "main")
    }

    @Test
    fun testMaybeObserveOnIO() {
        var thread: String? = null
        Maybe.fromCallable { }
            .observeOnIO()
            .doOnComplete { thread = Thread.currentThread().name }
            .blockingGet()

        assertEquals(thread, "main")
    }

    @Test
    fun testMaybeSubscribeOnComputation() {
       var thread: String? = null
        Maybe.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnComputation()
            .blockingGet()

        assertEquals(thread, "main")
    }

    @Test
    fun testMaybeObserveOnComputation() {
        var thread: String? = null
        Maybe.fromCallable { }
            .observeOnComputation()
            .doOnComplete { thread = Thread.currentThread().name }
            .blockingGet()

        assertEquals(thread, "main")
    }

    @Test
    fun testMaybeSubscribeOnMain() {
        var thread: String? = null
        Maybe.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnMain()
            .blockingGet()

        assertEquals(thread, "main")
    }

    @Test
    fun testMaybeObserveOnMain() {
        var thread: String? = null
        Maybe.fromCallable { }
            .observeOnMain()
            .doOnComplete { thread = Thread.currentThread().name }
            .blockingGet()

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