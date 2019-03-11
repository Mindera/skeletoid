package com.mindera.skeletoid

import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit

fun <T> debounceBuffer(time: Long, unit: TimeUnit, scheduler: Scheduler): ObservableTransformer<T, List<T>> = ObservableTransformer {
    it.publish {
        it.buffer(it.debounce(time, unit, scheduler))
    }
}