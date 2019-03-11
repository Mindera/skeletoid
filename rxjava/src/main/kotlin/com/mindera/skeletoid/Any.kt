package com.mindera.skeletoid

import io.reactivex.Observable
import io.reactivex.Single

fun <T : Any> T.justObservable(): Observable<T>
        = Observable.just(this)

fun <T : Any> T.justSingle(): Single<T>
        = Single.just(this)