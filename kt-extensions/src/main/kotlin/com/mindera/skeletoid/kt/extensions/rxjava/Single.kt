@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.rxjava

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

inline fun <T : Any> Single<T>.subscribeIO(): Single<T>
        = subscribeOn(Schedulers.io())

inline fun <T : Any> Single<T>.subscribeComputation(): Single<T>
        = subscribeOn(Schedulers.computation())

inline fun <T : Any> Single<T>.subscribeMain(): Single<T>
        = subscribeOn(AndroidSchedulers.mainThread())


inline fun <T : Any> Single<T>.observeIO(): Single<T>
        = observeOn(Schedulers.io())

inline fun <T : Any> Single<T>.observeComputation(): Single<T>
        = observeOn(Schedulers.computation())

inline fun <T : Any> Single<T>.observeMain(): Single<T>
        = observeOn(AndroidSchedulers.mainThread())
