package com.mindera.skeletoid.kt.extensions.rxjava

import io.reactivex.Observable
import io.reactivex.Single

fun <T : Any> T.toObservable(): Observable<T>
        = Observable.just(this)

fun <T : Any> T.toSingle(): Single<T>
        = Single.just(this)