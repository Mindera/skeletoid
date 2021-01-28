package com.mindera.skeletoid.rxjava

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Test

class ObservableExtensionsTest {

    @Test
    fun testFilterOrElseWithPredicate() {
        val list = mutableListOf<Int>()
        val subject = PublishSubject.create<Int>()

        subject
            .doOnNext { list.add(it) }
            .test()

        Observable.just(1, 2, 3, 4, 5, 6)
            .filterOrElse({ it % 2 == 0 }) { subject.onNext(it) }
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
            .skipWhileAndDo({ it < 4 }) { subject.onNext(it) }
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