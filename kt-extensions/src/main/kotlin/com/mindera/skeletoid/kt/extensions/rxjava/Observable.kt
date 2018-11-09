@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.rxjava

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T : Any> Observable<T>.subscribeOnIO(): Observable<T>
        = subscribeOn(Schedulers.io())

fun <T : Any> Observable<T>.subscribeOnComputation(): Observable<T>
        = subscribeOn(Schedulers.computation())

fun <T : Any> Observable<T>.subscribeOnMain(): Observable<T>
        = subscribeOn(AndroidSchedulers.mainThread())

fun <T : Any> Observable<T>.observeOnIO(): Observable<T>
        = observeOn(Schedulers.io())


fun <T : Any> Observable<T>.observeOnComputation(): Observable<T>
        = observeOn(Schedulers.computation())

fun <T : Any> Observable<T>.observeOnMain(): Observable<T>
        = observeOn(AndroidSchedulers.mainThread())
