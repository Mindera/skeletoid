@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.rxjava

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

inline fun <T : Any> Observable<T>.subscribeIO(): Observable<T>
        = subscribeOn(Schedulers.io())

inline fun <T : Any> Observable<T>.subscribeComputation(): Observable<T>
        = subscribeOn(Schedulers.computation())

inline fun <T : Any> Observable<T>.subscribeMain(): Observable<T>
        = subscribeOn(AndroidSchedulers.mainThread())


inline fun <T : Any> Observable<T>.observeIO(): Observable<T>
        = observeOn(Schedulers.io())


inline fun <T : Any> Observable<T>.observeComputation(): Observable<T>
        = observeOn(Schedulers.computation())

inline fun <T : Any> Observable<T>.observeMain(): Observable<T>
        = observeOn(AndroidSchedulers.mainThread())
