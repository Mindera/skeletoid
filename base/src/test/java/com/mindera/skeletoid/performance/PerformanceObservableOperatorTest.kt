package com.mindera.skeletoid.performance

import com.mindera.skeletoid.performance.rxoperators.PerformanceObservableOperator
import com.mindera.skeletoid.rx.BaseRxTest
import com.mindera.skeletoid.utils.extensions.mock
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.concurrent.TimeUnit

class PerformanceObservableOperatorTest : BaseRxTest() {

    private lateinit var observableOperator: PerformanceObservableOperator<String>
    private val performanceTracker = mock<PerformanceTracker>()

    override fun setUp() {
        super.setUp()
        observableOperator = PerformanceObservableOperator(performanceTracker)
    }

    @Test
    fun shouldStartStopPerformanceTracking() {
        Observable.just("hello")
            .lift(observableOperator)
            .test()

        verify(performanceTracker).startTracking()
        verify(performanceTracker).stopTracking()
    }

    @Test
    fun shouldStartStopPerformanceTrackingDelayedObservable() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        Observable.just("hi")
            .lift(observableOperator)
            .delay(1, TimeUnit.SECONDS, testScheduler)
            .test()

        verify(performanceTracker).startTracking()

        testScheduler.advanceTimeBy(2, TimeUnit.SECONDS)
        verify(performanceTracker).stopTracking()
    }

    @Test
    fun shouldStartStopPerformanceTrackingLongOperationObservable() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        Observable.create<String> { emitter ->
            run {
                Thread.sleep(800)
                emitter.onComplete()
            }
        }
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .lift(observableOperator)
            .test()

        verify(performanceTracker).startTracking()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        verify(performanceTracker).stopTracking()
    }

    @Test
    fun shouldStartStopPerformanceTrackingOnError() {
        val testObserver: TestObserver<String> =
            Observable.create<String> { emitter -> emitter.onError(Throwable("error")) }
                .lift(observableOperator)
                .test()

        verify(performanceTracker).startTracking()
        testObserver.assertErrorMessage("error")
        verify(performanceTracker).stopTracking()
    }

    @Test
    fun shouldNotStopPerformanceTrackingOnNext() {
        Observable.create<String> { emitter -> emitter.onNext("hello") }
            .lift(observableOperator)
            .test()

        verify(performanceTracker).startTracking()
        verify(performanceTracker, times(0)).stopTracking()
    }

    @Test
    fun shouldPerformanceTrackingOperatorReturnEmittedValue() {
        val testObserver = Observable.just("hello")
            .lift(observableOperator)
            .test()

        testObserver.assertValue("hello")
    }
}
