package com.mindera.skeletoid.rxjava.schedulers

import com.mindera.skeletoid.logs.LOG
import com.mindera.skeletoid.threads.threadpools.ThreadPoolUtils
import io.reactivex.Scheduler

object Schedulers {

    private val LOG_TAG = "Schedulers"

    private val computationThreadPool by lazy {
        val coreThreads = Runtime.getRuntime().availableProcessors()

        ThreadPoolUtils.getFixedThreadPool("RxComputationTP", coreThreads, coreThreads)
    }

    private val ioThreadPool by lazy {
        val coreThreads = Runtime.getRuntime().availableProcessors()
        val maxIoThreads = coreThreads + 2

        LOG.v(LOG_TAG, "Starting IO Thread Pool with max number of $maxIoThreads threads")

        ThreadPoolUtils.getFixedThreadPool("RxIoTP", maxIoThreads, coreThreads)
    }

    fun computation(): Scheduler = io.reactivex.schedulers.Schedulers.from(computationThreadPool)

    fun io(): Scheduler = io.reactivex.schedulers.Schedulers.from(ioThreadPool)

}