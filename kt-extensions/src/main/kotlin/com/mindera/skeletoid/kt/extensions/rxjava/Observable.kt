@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.rxjava

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ConcurrentHashMap

fun <T : Any> Observable<T>.subscribeOnIO(): Observable<T> = subscribeOn(Schedulers.io())

fun <T : Any> Observable<T>.subscribeOnComputation(): Observable<T> = subscribeOn(Schedulers.computation())

fun <T : Any> Observable<T>.subscribeOnMain(): Observable<T> = subscribeOn(AndroidSchedulers.mainThread())

fun <T : Any> Observable<T>.observeOnIO(): Observable<T> = observeOn(Schedulers.io())

fun <T : Any> Observable<T>.observeOnComputation(): Observable<T> = observeOn(Schedulers.computation())

fun <T : Any> Observable<T>.observeOnMain(): Observable<T> = observeOn(AndroidSchedulers.mainThread())

//Use this to maintain a list of shared Observables (use together with allowMultipleSubscribers)
fun <T : Any> Observable<T>.createUniqueConcurrentRequestCache(requestMap: ConcurrentHashMap<String, Observable<*>>, key: String): Observable<T> {
    requestMap[key] = this
    return this.doFinally { requestMap.remove(key) }
}

fun <T> Observable<T>.allowMultipleSubscribers(): Observable<T> =
        share()
                .replay(1)
                .autoConnect(1)





