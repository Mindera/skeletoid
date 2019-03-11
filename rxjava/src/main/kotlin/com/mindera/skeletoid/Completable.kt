@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid

import com.mindera.skeletoid.schedulers.Schedulers
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers

fun Completable.subscribeOnIO(): Completable = subscribeOn(Schedulers.io())

fun Completable.subscribeOnComputation(): Completable = subscribeOn(Schedulers.computation())

fun Completable.subscribeOnMain(): Completable = subscribeOn(AndroidSchedulers.mainThread())

fun Completable.observeOnIO(): Completable = observeOn(Schedulers.io())

fun Completable.observeOnComputation(): Completable = observeOn(Schedulers.computation())

fun Completable.observeOnMain(): Completable = observeOn(AndroidSchedulers.mainThread())
