@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.rxjava

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

inline fun Completable.subscribeIO(): Completable = subscribeOn(Schedulers.io())

inline fun Completable.subscribeComputation(): Completable = subscribeOn(Schedulers.computation())

inline fun Completable.subscribeMain(): Completable = subscribeOn(AndroidSchedulers.mainThread())


inline fun Completable.observeIO(): Completable = observeOn(Schedulers.io())

inline fun Completable.observeComputation(): Completable = observeOn(Schedulers.computation())

inline fun Completable.observeMain(): Completable = observeOn(AndroidSchedulers.mainThread())
