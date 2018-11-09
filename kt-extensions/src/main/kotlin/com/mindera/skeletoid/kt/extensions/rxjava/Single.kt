@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.rxjava

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T : Any> Single<T>.subscribeOnIO(): Single<T>
        = subscribeOn(Schedulers.io())

fun <T : Any> Single<T>.subscribeOnComputation(): Single<T>
        = subscribeOn(Schedulers.computation())

fun <T : Any> Single<T>.subscribeOnMain(): Single<T>
        = subscribeOn(AndroidSchedulers.mainThread())

fun <T : Any> Single<T>.observeOnIO(): Single<T>
        = observeOn(Schedulers.io())

fun <T : Any> Single<T>.observeOnComputation(): Single<T>
        = observeOn(Schedulers.computation())

fun <T : Any> Single<T>.observeOnMain(): Single<T>
        = observeOn(AndroidSchedulers.mainThread())
