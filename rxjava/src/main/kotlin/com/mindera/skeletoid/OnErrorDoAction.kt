package com.mindera.skeletoid

import io.reactivex.Observable
import io.reactivex.Single

private const val LOG_TAG = "OnErrorDoRequest"

class RequestWrapper<T>(val result: T? = null, val throwable: Throwable? = null)

class ActionOnErrorException(val error: Throwable) : Exception()

fun <R> Single<R>.onErrorDoActionBeforeFailing(doAction: (Throwable) -> Single<R>): Single<R> =
        map { com.mindera.skeletoid.RequestWrapper(it) }
                .onErrorReturn { e -> com.mindera.skeletoid.RequestWrapper(throwable = e) }
                .flatMap { wrapper ->
                    wrapper.throwable?.let { throwable ->
                        doAction(throwable).flatMap {
                            throw com.mindera.skeletoid.ActionOnErrorException(throwable)
                            @Suppress("UNREACHABLE_CODE")
                            Single.just(it) //Needed for compiler to know which type is this...
                        }
                    } ?: Single.just(wrapper.result)
                }


fun <R> Observable<R>.onErrorDoActionBeforeFailing(doAction: (Throwable) -> Observable<R>): Observable<R> =
        map { com.mindera.skeletoid.RequestWrapper(it) }
                .onErrorReturn { e -> com.mindera.skeletoid.RequestWrapper(throwable = e) }
                .flatMap { wrapper ->
                    wrapper.throwable?.let { throwable ->
                        doAction(throwable).flatMap {
                            throw com.mindera.skeletoid.ActionOnErrorException(throwable)
                            @Suppress("UNREACHABLE_CODE")
                            Observable.just(it) //Needed for compiler to know which type is this...
                        }
                    } ?: Observable.just(wrapper.result)
                }