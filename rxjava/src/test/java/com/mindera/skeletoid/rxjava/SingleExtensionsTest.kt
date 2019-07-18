package com.mindera.skeletoid.rxjava

import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import kotlin.test.assertEquals

class SingleExtensionsTest {

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
}