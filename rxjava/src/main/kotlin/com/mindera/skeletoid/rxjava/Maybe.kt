package com.mindera.skeletoid.rxjava

import com.mindera.skeletoid.rxjava.schedulers.Schedulers
import io.reactivex.Maybe

fun <T : Any> Maybe<T>.subscribeOnIO(): Maybe<T> = subscribeOn(Schedulers.io())

fun <T : Any> Maybe<T>.observeOnIO(): Maybe<T> = observeOn(Schedulers.io())

fun <T : Any> Maybe<T>.subscribeOnComputation(): Maybe<T> = subscribeOn(Schedulers.computation())

fun <T : Any> Maybe<T>.observeOnComputation(): Maybe<T> = observeOn(Schedulers.computation())

fun <T : Any> Maybe<T>.subscribeOnMain(): Maybe<T> = subscribeOn(Schedulers.main())

fun <T : Any> Maybe<T>.observeOnMain(): Maybe<T> = observeOn(Schedulers.main())

fun <T : Any> Maybe<T>.filterOrElse(condition: Boolean, action: () -> Unit): Maybe<T> =
    doOnSubscribe { if (!condition) action() }
    .filter { condition }