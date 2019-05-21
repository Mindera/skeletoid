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
        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
        }
    }

    RxJavaPlugins.setInitIoSchedulerHandler { immediate }
    RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
    RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
    RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }

}
