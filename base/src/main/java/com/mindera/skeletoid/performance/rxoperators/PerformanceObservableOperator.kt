package com.mindera.skeletoid.performance.rxoperators

import com.mindera.skeletoid.performance.PerformanceTracker
import io.reactivex.ObservableOperator
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 * @param T the value type of item the Observer expects to observe
 * @property performanceTracker the object that manages tracking initiation and completion
 *
 * @return ObservableOperator that tracks time between Observer's subscription and completion
 */
open class PerformanceObservableOperator<T>(
    private val performanceTracker: PerformanceTracker
) : ObservableOperator<T, T> {

    @Throws(Exception::class)
    override fun apply(observer: Observer<in T>): Observer<in T> {
        return object : Observer<T> {

            override fun onSubscribe(d: Disposable) {
                performanceTracker.startTracking()

                observer.onSubscribe(d)
            }

            override fun onNext(t: T) {
                observer.onNext(t)
            }

            override fun onComplete() {
                observer.onComplete()

                performanceTracker.stopTracking()
            }

            override fun onError(e: Throwable) {
                observer.onError(e)

                performanceTracker.stopTracking()
            }
        }
    }
}
