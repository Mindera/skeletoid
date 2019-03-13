package com.mindera.skeletoid.rxjava.schedulers

import io.reactivex.Scheduler

/**
 * Change the schedulers to trampoline to make the unit tests run in a sequentially predictable order,
 * before the assertions code run.
 */
object Schedulers {
    fun computation(): Scheduler = io.reactivex.schedulers.Schedulers.trampoline()
    fun io(): Scheduler = io.reactivex.schedulers.Schedulers.trampoline()
}