package com.mindera.skeletoid.kt.extensions.rxjava

import io.reactivex.Observable
import io.reactivex.Single

private const val LOG_TAG = "OnErrorDoRequest"

class RequestWrapper<T>(val result: T? = null, val throwable: Throwable? = null)

class ActionOnErrorException(val error: Throwable) : Exception()

fun <R> Single<R>.onErrorDoActionBeforeFailing(doAction: (Throwable) -> Single<R>): Single<R> =
        map { RequestWrapper(it) }
                .onErrorReturn { e -> RequestWrapper(throwable = e) }
                .flatMap { wrapper ->
                    wrapper.throwable?.let { throwable ->
                        doAction(throwable).flatMap {
                            throw ActionOnErrorException(throwable)
                            Single.just(it) //Needed for compiler to know which type is this...
                        }
                    } ?: Single.just(wrapper.result)
                }


fun <R> Observable<R>.onErrorDoActionBeforeFailing(doAction: (Throwable) -> Observable<R>): Observable<R> =
        map { RequestWrapper(it) }
                .onErrorReturn { e -> RequestWrapper(throwable = e) }
                .flatMap { wrapper ->
                    wrapper.throwable?.let { throwable ->
                        doAction(throwable).flatMap {
                            throw ActionOnErrorException(throwable)
                            Observable.just(it) //Needed for compiler to know which type is this...
                        }
                    } ?: Observable.just(wrapper.result)
                }