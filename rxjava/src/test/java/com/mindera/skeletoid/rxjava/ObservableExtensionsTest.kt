package com.mindera.skeletoid.rxjava

import io.reactivex.Observable
import io.reactivex.functions.Predicate
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import kotlin.test.assertEquals

class ObservableExtensionsTest {

    // ATTENTION !!!!
    // This is hitting main in all threadpools since we are defaulting to TRAMPOLINE on QA builds.
    // It would be great to not do it, but that would break Apps using QA build to UI Tests

    private fun threadName(): String = Thread.currentThread().name

    private fun assertMainThread() {
        assertEquals("main", threadName())
    }

    @Test
    fun testObservableSubscribeOnIO() {
        Observable.fromCallable { threadName() }
            .subscribeOnIO()
            .test()
            .assertValueAt(0) { it == "main" }
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testObservableObserveOnIO() {
        Observable.fromCallable { }
            .observeOnIO()
            .doOnNext { assertMainThread() }
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testObservableSubscribeOnComputation() {
        Observable.fromCallable { threadName() }
            .subscribeOnComputation()
            .test()
            .assertValueAt(0) { it == "main" }
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testObservableObserveOnComputation() {
        Observable.fromCallable { }
            .observeOnComputation()
            .doOnNext { assertMainThread() }
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testObservableSubscribeOnMain() {
        Observable.fromCallable { threadName() }
            .subscribeOnMain()
            .test()
            .assertValueAt(0) { it == "main" }
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testObservableObserveOnMain() {
        Observable.fromCallable { }
            .observeOnMain()
            .doOnNext { assertMainThread() }
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun testFilterOrElseWithPredicate() {
        val list = mutableListOf<Int>()
        val subject = PublishSubject.create<Int>()

        subject
            .doOnNext { list.add(it) }
            .test()

        Observable.just(1, 2, 3, 4, 5, 6)
            .filterOrElse(Predicate { it % 2 == 0 }) { subject.onNext(it) }
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValueCount(3)
            .assertValues(2, 4, 6)

        assertEquals(listOf(1, 3, 5), list)
    }

    @Test
    fun testFilterOrElseWithConditionFalse() {
        val list = mutableListOf<Int>()
        val subject = PublishSubject.create<Int>()

        subject
            .doOnNext { list.add(it) }
            .test()

        Observable.just(1, 2, 3, 4, 5, 6)
            .filterOrElse(false) { subject.onNext(it) }
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertNoValues()

        assertEquals(listOf(1, 2, 3, 4, 5, 6), list)
    }

    @Test
    fun testFilterOrElseWithConditionTrue() {
        val list = mutableListOf<Int>()
        val subject = PublishSubject.create<Int>()

        subject
            .doOnNext { list.add(it) }
            .test()

        Observable.just(1, 2, 3, 4, 5, 6)
            .filterOrElse(true) { subject.onNext(it) }
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValues(1, 2, 3, 4, 5, 6)

        assertEquals(emptyList<Int>(), list)
    }

    @Test
    fun testSkipWhileAndDoWithPredicate() {
        val list = mutableListOf<Int>()
        val subject = PublishSubject.create<Int>()

        subject
            .doOnNext { list.add(it) }
            .test()

        Observable.just(1, 2, 3, 4, 5, 6)
            .skipWhileAndDo(Predicate { it < 4 }) { subject.onNext(it) }
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValues(4, 5, 6)

        assertEquals(listOf(1, 2, 3), list)
    }

    @Test
    fun testSkipWhileAndDoWithConditionTrue() {
        val list = mutableListOf<Int>()
        val subject = PublishSubject.create<Int>()

        subject
            .doOnNext { list.add(it) }
            .test()

        Observable.just(1, 2, 3, 4, 5, 6)
            .skipWhileAndDo(true) { subject.onNext(it) }
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertNoValues()

        assertEquals(listOf(1, 2, 3, 4, 5, 6), list)
    }

    @Test
    fun testSkipWhileAndDoWithConditionFalse() {
        val list = mutableListOf<Int>()
        val subject = PublishSubject.create<Int>()

        subject
            .doOnNext { list.add(it) }
            .test()

        Observable.just(1, 2, 3, 4, 5, 6)
            .skipWhileAndDo(false) { subject.onNext(it) }
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValues(1, 2, 3, 4, 5, 6)

        assertEquals(emptyList<Int>(), list)
    }
}