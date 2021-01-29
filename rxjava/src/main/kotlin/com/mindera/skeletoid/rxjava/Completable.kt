package com.mindera.skeletoid.rxjava

import com.mindera.skeletoid.rxjava.schedulers.Schedulers
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

fun Completable.subscribeOnIO(): Completable = subscribeOn(Schedulers.io())

fun Completable.observeOnIO(): Completable = observeOn(Schedulers.io())

fun Completable.subscribeOnComputation(): Completable = subscribeOn(Schedulers.computation())

fun Completable.observeOnComputation(): Completable = observeOn(Schedulers.computation())

fun Completable.subscribeOnMain(): Completable = subscribeOn(Schedulers.main())

fun Completable.observeOnMain(): Completable = observeOn(Schedulers.main())

fun Completable.delayAtLeast(timeToWait: Long = 1000, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Completable {
    return Completable.merge(listOf(this, Completable.timer(timeToWait, timeUnit)))
}
