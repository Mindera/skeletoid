package com.mindera.skeletoid.rxjava.schedulers

import com.mindera.skeletoid.threads.threadpools.ThreadPoolUtils
import io.reactivex.Scheduler

object Schedulers {

    private val computationThreadPool by lazy {
        val maxComputationThreads = Runtime.getRuntime().availableProcessors()
        ThreadPoolUtils.getScheduledThreadPool("RxComputationTP", maxComputationThreads)
    }

    private val ioThreadPool by lazy {
        val maxIoThreads = Runtime.getRuntime().availableProcessors()
        ThreadPoolUtils.getScheduledThreadPool("RxIoTP", maxIoThreads + 2)
    }

    fun computation(): Scheduler = io.reactivex.schedulers.Schedulers.from(computationThreadPool)
    fun io(): Scheduler = io.reactivex.schedulers.Schedulers.from(ioThreadPool)
    fun single(): Scheduler = io.reactivex.schedulers.Schedulers.single()
    fun main(): Scheduler = io.reactivex.android.schedulers.AndroidSchedulers.mainThread()
}
