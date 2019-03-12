package com.mindera.skeletoid.rx

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.After
import org.junit.Before
import java.util.concurrent.Executor

abstract class BaseRxTest {

    @Before
    open fun setUp() {
        setupRxSchedulers()
    }

    @After
    open fun tearDownTest() {
        RxJavaPlugins.reset()
    }
}

private fun setupRxSchedulers() {
    RxJavaPlugins.reset()

    val immediate = object : Scheduler() {
        override fun createWorker(): Scheduler.Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
        }
    }

    RxJavaPlugins.setInitIoSchedulerHandler { _ -> immediate }
    RxJavaPlugins.setInitComputationSchedulerHandler { _ -> immediate }
    RxJavaPlugins.setInitNewThreadSchedulerHandler { _ -> immediate }
    RxJavaPlugins.setInitSingleSchedulerHandler { _ -> immediate }
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> immediate }

}
