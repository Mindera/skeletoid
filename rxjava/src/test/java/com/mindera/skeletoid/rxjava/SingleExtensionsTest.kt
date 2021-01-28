package com.mindera.skeletoid.rxjava

import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import kotlin.test.assertEquals

class SingleExtensionsTest {

    // ATTENTION !!!!
    // This is hitting main in all threadpools since we are defaulting to TRAMPOLINE on QA builds.
    // It would be great to not do it, but that would break Apps using QA build to UI Tests

    private fun threadName(): String = Thread.currentThread().name

    private fun assertMainThread() {
        assertEquals("main", threadName())
    }

    @Test
    fun testSingleSubscribeOnIO() {
        Single.fromCallable { threadName() }
            .subscribeOnIO()
            .test()
            .assertValueAt(0) { it == "main" }
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testSingleObserveOnIO() {
        Single.fromCallable { }
            .observeOnIO()
            .doOnSuccess { assertMainThread() }
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testSingleSubscribeOnComputation() {
        Single.fromCallable { threadName() }
            .subscribeOnComputation()
            .test()
            .assertValueAt(0) { it == "main" }
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testSingleObserveOnComputation() {
        Single.fromCallable { }
            .observeOnComputation()
            .doOnSuccess { assertMainThread() }
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testSingleSubscribeOnMain() {
        Single.fromCallable { threadName() }
            .subscribeOnMain()
            .test()
            .assertValueAt(0) { it == "main" }
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testSingleObserveOnMain() {
        Single.fromCallable { }
            .observeOnMain()
            .doOnSuccess { assertMainThread() }
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

        Single.just(1)
            .filterOrElse(false) { subject.onNext(1) }
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

        Single.just(1)
            .filterOrElse(true) { subject.onNext(1) }
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValues(1)

        assertEquals(emptyList<Int>(), list)
    }

    @Test
    fun testSingleMultipleSubscribers() {
        val single = Single.fromCallable { "banana" }
            .subscribeOnMain()
            .allowMultipleSubscribers()

        var result1 = "1"
        var result2 = "2"

        single.subscribe { result1 = it }
        single.subscribe { result2 = it }
        single.single("")

        assertEquals(result1, result2)
    }
}