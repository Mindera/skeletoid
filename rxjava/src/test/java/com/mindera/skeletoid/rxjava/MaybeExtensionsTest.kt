package com.mindera.skeletoid.rxjava

import io.reactivex.Maybe
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import kotlin.test.assertEquals

class MaybeExtensionsTest {

    // ATTENTION !!!!
    // This is hitting main in all threadpools since we are defaulting to TRAMPOLINE on QA builds.
    // It would be great to not do it, but that would break Apps using QA build to UI Tests

    private fun threadName(): String = Thread.currentThread().name

    private fun assertMainThread() {
        assertEquals( "main", threadName())
    }

    @Test
    fun testMaybeSubscribeOnIO() {
        Maybe.fromCallable { threadName() }
            .subscribeOnIO()
            .test()
            .assertValueAt(0) { it == "main" }
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testMaybeObserveOnIO() {
        Maybe.fromCallable { }
            .observeOnIO()
            .doOnComplete { assertMainThread() }
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testMaybeSubscribeOnComputation() {
        Maybe.fromCallable { threadName() }
            .subscribeOnComputation()
            .test()
            .assertValueAt(0) { it == "main" }
            .assertNoErrors()
            .assertComplete()

    }

    @Test
    fun testMaybeObserveOnComputation() {
        Maybe.fromCallable { }
            .observeOnComputation()
            .doOnComplete { assertMainThread() }
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testMaybeSubscribeOnMain() {
        Maybe.fromCallable { threadName() }
            .subscribeOnMain()
            .test()
            .assertValueAt(0) { it == "main" }
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testMaybeObserveOnMain() {
        Maybe.fromCallable { }
            .observeOnMain()
            .doOnComplete { assertMainThread() }
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testFilterOrElseWithConditionFalse() {
        val list = mutableListOf<Int>()
        val subject = PublishSubject.create<Int>()

        subject
            .doOnNext { list.add(it) }
            .test()

        Maybe.just(1)
            .filterOrElse(list.isNotEmpty()) { subject.onNext(1) }
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
            .filterOrElse(list.isEmpty()) { subject.onNext(1) }
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValues(1)

        assertEquals(emptyList<Int>(), list)
    }
}