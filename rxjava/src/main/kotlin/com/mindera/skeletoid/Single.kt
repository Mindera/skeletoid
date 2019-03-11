@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid

import com.mindera.skeletoid.schedulers.Schedulers
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

fun <T : Any> Single<T>.subscribeOnIO(): Single<T>
        = subscribeOn(Schedulers.io())

fun <T : Any> Single<T>.observeOnIO(): Single<T>
        = observeOn(Schedulers.io())


fun <T : Any> Single<T>.subscribeOnComputation(): Single<T>
        = subscribeOn(Schedulers.computation())

fun <T : Any> Single<T>.observeOnComputation(): Single<T>
        = observeOn(Schedulers.computation())


fun <T : Any> Single<T>.subscribeOnMain(): Single<T>
        = subscribeOn(AndroidSchedulers.mainThread())

fun <T : Any> Single<T>.observeOnMain(): Single<T>
        = observeOn(AndroidSchedulers.mainThread())


fun <T> Single<T>.allowMultipleSubscribers(): Observable<T> =
        toObservable()
                .share()
                .replay(1)
                .autoConnect(1)