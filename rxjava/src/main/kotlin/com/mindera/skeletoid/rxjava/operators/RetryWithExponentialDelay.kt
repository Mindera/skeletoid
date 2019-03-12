package com.mindera.skeletoid.rxjava.operators

import io.reactivex.Flowable
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit

class RetryWithExponentialDelay(private val maxRetries: Int, private val retryDelayMillis: Long) : Function<Flowable<out Throwable>, Flowable<*>> {
    private var retryCount: Int = 0

    override fun apply(attempts: Flowable<out Throwable>): Flowable<*> {
        return attempts.flatMap { t ->
            if (++retryCount <= maxRetries) {

                return@flatMap Flowable.timer(Math.pow(retryDelayMillis.toDouble(), retryCount.toDouble()).toLong(), TimeUnit.MILLISECONDS)
            }
            return@flatMap Flowable.error<Throwable>(t)
        }
    }
}