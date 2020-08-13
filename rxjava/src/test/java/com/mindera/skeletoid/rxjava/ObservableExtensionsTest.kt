package com.mindera.skeletoid.rxjava

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Predicate
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObservableExtensionsTest {

    // ATTENTION !!!!
    // This is hitting main in all threadpools since we are defaulting to TRAMPOLINE on QA builds.
    // It would be great to not do it, but that would break Apps using QA build to UI Tests

    @Test
    fun testObservableSubscribeOnIO() {
        var thread: String? = null
        Observable.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnIO()
            .single(Unit)
            .blockingGet()

        assertEquals(thread, "main") // To be fixed: read above
    }

    @Test
    fun testObservableObserveOnIO() {
        var thread: String? = null
        Observable.fromCallable { }
            .observeOnIO()
            .single(Unit)
            .doOnSuccess { thread = Thread.currentThread().name }
            .blockingGet()

        assertEquals(thread, "main")  // To be fixed: read above
    }

    @Test
    fun testObservableSubscribeOnComputation() {
        var thread: String? = null
        Observable.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnComputation()
            .single(Unit)
            .blockingGet()

        assertEquals(thread, "main") // To be fixed: read above
    }

    @Test
    fun testObservableObserveOnComputation() {
        var thread: String? = null
        Observable.fromCallable {  }
            .observeOnComputation()
            .single(Unit)
            .doOnSuccess { thread = Thread.currentThread().name }
            .blockingGet()

        assertEquals(thread, "main") // To be fixed: read above
    }

    @Test
    fun testObservableSubscribeOnMain() {
        var thread: String? = null
        Observable.fromCallable { thread = Thread.currentThread().name }
            .subscribeOnMain()
            .single(Unit)
            .blockingGet()

        assertEquals(thread, "main")
    }

    @Test
    fun testObservableObserveOnMain() {
        var thread: String? = null
        Observable.fromCallable { }
            .observeOnMain()
            .single(Unit)
            .doOnSuccess { thread = Thread.currentThread().name }
            .blockingGet()

        assertEquals(thread, "main")
    }

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

    @Test
    fun testSkipWhileAndDoWithPredicate() {
        val list = mutableListOf<Int>()
        val subject = PublishSubject.create<Int>()
        subject
            .doOnNext { list.add(it) }
            .test()

        Observable.just(1, 2, 3, 4, 5, 6)
            .skipWhileAndDo(Predicate { it < 4 }, {
                subject.onNext(it)
            })
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
            .skipWhileAndDo(Predicate { true }, {
                subject.onNext(it)
            })
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
            .skipWhileAndDo(Predicate { false }, {
                subject.onNext(it)
            })
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValues(1, 2, 3, 4, 5, 6)

        assertEquals(emptyList<Int>(), list)
    }

    //How to test this ?
//    @Test
//    fun testCreateUniqueConcurrentRequestCache() {
//        Observable.fromCallable {  }
//            .createUniqueConcurrentRequestCache()
//    }

}