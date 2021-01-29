package com.mindera.skeletoid.rxjava

import io.reactivex.Maybe
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Test

class MaybeExtensionsTest {

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