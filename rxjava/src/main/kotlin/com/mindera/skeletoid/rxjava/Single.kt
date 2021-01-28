package com.mindera.skeletoid.rxjava

import com.mindera.skeletoid.rxjava.schedulers.Schedulers
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

fun <T : Any> Single<T>.subscribeOnIO(): Single<T> = subscribeOn(Schedulers.io())

fun <T : Any> Single<T>.observeOnIO(): Single<T> = observeOn(Schedulers.io())

fun <T : Any> Single<T>.subscribeOnComputation(): Single<T> = subscribeOn(Schedulers.computation())

fun <T : Any> Single<T>.observeOnComputation(): Single<T> = observeOn(Schedulers.computation())

fun <T : Any> Single<T>.subscribeOnMain(): Single<T> = subscribeOn(Schedulers.main())

fun <T : Any> Single<T>.observeOnMain(): Single<T> = observeOn(Schedulers.main())

fun <T : Any> Single<T>.filterOrElse(condition: Boolean, action: () -> Unit): Maybe<T> {
    return doOnSubscribe { if (!condition) action() }
        .filter { condition }
}

fun <T : Any> Single<T>.allowMultipleSubscribers(): Observable<T> {
    return toObservable()
        .share()
        .replay(1)
        .autoConnect(1)
}