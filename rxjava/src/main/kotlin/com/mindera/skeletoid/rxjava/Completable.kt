@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.rxjava

import com.mindera.skeletoid.rxjava.schedulers.Schedulers
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun Completable.subscribeOnIO(): Completable = subscribeOn(Schedulers.io())

fun Completable.observeOnIO(): Completable = observeOn(Schedulers.io())


fun Completable.subscribeOnComputation(): Completable = subscribeOn(Schedulers.computation())

fun Completable.observeOnComputation(): Completable = observeOn(Schedulers.computation())


fun Completable.subscribeOnMain(): Completable = subscribeOn(AndroidSchedulers.mainThread())

fun Completable.observeOnMain(): Completable = observeOn(AndroidSchedulers.mainThread())

fun Completable.delayAtLeast(timeToWait: Long = 1000,
                             timeUnit: TimeUnit = TimeUnit.MILLISECONDS
): Completable {
    return Completable.merge(listOf(this, Completable.timer(timeToWait, timeUnit)))
}
