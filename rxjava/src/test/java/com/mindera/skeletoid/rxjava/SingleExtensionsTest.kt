package com.mindera.skeletoid.rxjava

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import kotlin.test.assertEquals

class SingleExtensionsTest {

    // ATTENTION !!!!
    // This is hitting main in all threadpools since we are defaulting to TRAMPOLINE on QA builds.
    // It would be great to not do it, but that would break Apps using QA build to UI Tests

    @Test
    fun testSingleSubscribeOnIO() {
        var thread: String? = null
        Single.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnIO()
            .blockingGet()

        assertEquals(thread, "main") // To be fixed: read above
    }

    @Test
    fun testSingleObserveOnIO() {
        var thread: String? = null
        Single.fromCallable { }
            .observeOnIO()
            .doOnSuccess { thread = Thread.currentThread().name }
            .blockingGet()

        assertEquals(thread, "main")  // To be fixed: read above
    }

    @Test
    fun testSingleSubscribeOnComputation() {
        var thread: String? = null
        Single.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnComputation()
            .blockingGet()

        assertEquals(thread, "main") // To be fixed: read above
    }

    @Test
    fun testSingleObserveOnComputation() {
        var thread: String? = null
        Single.fromCallable { }
            .observeOnComputation()
            .doOnSuccess { thread = Thread.currentThread().name }
            .blockingGet()

        assertEquals(thread, "main") // To be fixed: read above
    }

    @Test
    fun testSingleSubscribeOnMain() {
        var thread: String? = null
        Single.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnMain()
            .blockingGet()

        assertEquals(thread, "main")
    }

    @Test
    fun testSingleObserveOnMain() {
        var thread: String? = null
        Single.fromCallable { }
            .observeOnMain()
            .doOnSuccess { thread = Thread.currentThread().name }
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

        Single.just(1)
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

        Single.just(1)
            .filterOrElse(true) {
                subject.onNext(1)
            }
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

        var result1: String = "1"
        var result2: String = "2"

        single.subscribe { result1 = it }

        single.subscribe { result2 = it }

        single.single("")

        assertEquals(result1, result2)
    }
}