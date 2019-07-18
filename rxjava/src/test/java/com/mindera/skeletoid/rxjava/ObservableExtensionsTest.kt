package com.mindera.skeletoid.rxjava

import io.reactivex.Observable
import io.reactivex.functions.Predicate
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObservableExtensionsTest {

    @Test
    fun testFilterOrElseWithPredicate() {
        val list = mutableListOf<Int>()

        val subject = PublishSubject.create<Int>()
        subject
            .doOnNext { list.add(it) }
            .test()

        Observable.just(1, 2, 3, 4, 5, 6)
            .filterOrElse(Predicate { it % 2 == 0 }, {
                subject.onNext(it)
            })
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
            .filterOrElse(Predicate { false }, {
                subject.onNext(it)
            })
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
            .filterOrElse(Predicate { true }, {
                subject.onNext(it)
            })
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValues(1, 2, 3, 4, 5, 6)

        assertEquals(emptyList<Int>(), list)
    }
}