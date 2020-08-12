package com.mindera.skeletoid.performance.rxoperators

import com.mindera.skeletoid.performance.interfaces.IPerformanceTracker
import io.reactivex.SingleObserver
import io.reactivex.SingleOperator
import io.reactivex.disposables.Disposable

/**
 *
 * @param T the value type of item the SingleObserver expects to observe
 * @property performanceTracker the object that manages tracking initiation and completion
 *
 * @return SingleOperator that tracks time between SingleObserver's subscription and completion
 */
open class PerformanceSingleOperator<T>(private val performanceTracker: IPerformanceTracker) :
    SingleOperator<T, T> {

    @Throws(Exception::class)
    override fun apply(observer: SingleObserver<in T>): SingleObserver<in T> {
        return object : SingleObserver<T> {

            override fun onSubscribe(d: Disposable) {
                performanceTracker.startTracking()
                observer.onSubscribe(d)
            }

            override fun onSuccess(t: T) {
                observer.onSuccess(t)

                performanceTracker.stopTracking()
            }

            override fun onError(e: Throwable) {
                observer.onError(e)

                performanceTracker.stopTracking()
            }
        }
    }
}
