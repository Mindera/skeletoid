package com.mindera.skeletoid.performance

import com.mindera.skeletoid.performance.rxoperators.PerformanceSingleOperator
import com.mindera.skeletoid.rx.BaseRxTest
import com.mindera.skeletoid.utils.extensions.mock
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import org.mockito.Mockito.verify
import java.util.concurrent.TimeUnit

class PerformanceSingleOperatorTest : BaseRxTest() {

    lateinit var operator: PerformanceSingleOperator<String>
    private val performanceTracker = mock<PerformanceTracker>()

    override fun setUp() {
        super.setUp()
        operator = PerformanceSingleOperator(performanceTracker)
    }

    @Test
    fun shouldStartStopPerformanceTracking() {
        Single.just("hello")
            .lift(operator)
            .test()

        verify(performanceTracker).startTracking()
        verify(performanceTracker).stopTracking()
    }

    @Test
    fun shouldStartStopPerformanceTrackingDelayedSingle() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        Single.just("hi")
            .lift(operator)
            .delay(1, TimeUnit.SECONDS, testScheduler)
            .test()

        verify(performanceTracker).startTracking()

        testScheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        verify(performanceTracker).stopTracking()
    }

    @Test
    fun shouldStartStopPerformanceTrackingLongOperationSingle() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        Single.create<String> { emitter ->
            run {
                Thread.sleep(800)
                emitter.onSuccess("hi")
            }
        }
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .lift(operator)
            .test()

        verify(performanceTracker).startTracking()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        verify(performanceTracker).stopTracking()
    }

    @Test
    fun shouldStartStopPerformanceTrackingOnError() {
        val testObserver: TestObserver<String> =
            Single.create<String> { emitter -> emitter.onError(Throwable("error")) }
                .lift(operator)
                .test()

        verify(performanceTracker).startTracking()
        testObserver.assertErrorMessage("error")
        verify(performanceTracker).stopTracking()
    }

    @Test
    fun shouldPerformanceTrackingOperatorReturnEmittedValue() {
        val testObserver = Single.just("hello")
            .lift(operator)
            .test()

        testObserver.assertValue("hello")
    }
}
