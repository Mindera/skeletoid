@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.rxjava

import com.mindera.skeletoid.rxjava.schedulers.Schedulers
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

fun <T : Any> Observable<T>.subscribeOnIO(): Observable<T> = subscribeOn(Schedulers.io())

fun <T : Any> Observable<T>.observeOnIO(): Observable<T> = observeOn(Schedulers.io())


fun <T : Any> Observable<T>.subscribeOnComputation(): Observable<T> =
    subscribeOn(Schedulers.computation())

fun <T : Any> Observable<T>.observeOnComputation(): Observable<T> =
    observeOn(Schedulers.computation())


fun <T : Any> Observable<T>.subscribeOnMain(): Observable<T> =
    subscribeOn(AndroidSchedulers.mainThread())

fun <T : Any> Observable<T>.observeOnMain(): Observable<T> =
    observeOn(AndroidSchedulers.mainThread())


//Use this to maintain a list of shared Observables (use together with allowMultipleSubscribers)
fun <T : Any> Observable<T>.createUniqueConcurrentRequestCache(
    requestMap: ConcurrentHashMap<String, Observable<*>>,
    key: String
): Observable<T> {

    val obs = this.doFinally {
        requestMap.remove(key)
    }

    requestMap[key] = obs
    return obs
}

/**
 * Holds an item and an exception. Used in [delayAtLeast]
 */
internal data class DataHolder<T>(val something: T?= null, val throwable: Throwable? = null)

/**
 * Extension that always waits for at least timeToWait in the provided timeUnit before emitting. This will happen even if an exception is thrown. Used to prevent loadings to just blink when a connection is too fast.
 *
 * @param timeToWait - the time to wait
 * @param timeUnit - the unit of timeToWait
 */
fun <T> Observable<T>.delayAtLeast(
    timeToWait: Long = 1000,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS
): Observable<T> {
    var data : DataHolder<T>
    return Observable.zip<DataHolder<T>, Long, DataHolder<T>>(this.map {
        data = DataHolder(something = it)
        data
    }
        .onErrorReturn { DataHolder(throwable = it) },
        Observable.timer(timeToWait, timeUnit), BiFunction { t, _ -> t })
        .map {
            it.something ?: throw it.throwable ?: Exception()
        }
}

fun <T> Observable<T>.allowMultipleSubscribers(): Observable<T> =
    share()
        .replay(1)
        .autoConnect(1)

fun <T> Observable<T>.filterAndDo(condition: Boolean, elseFunction: () -> Unit): Observable<T> =
    doOnNext {
        if (condition) {
            elseFunction()
        }
    }
        .filter { condition }

fun <T> Observable<T>.skipWhileAndDo(condition: Boolean, doAction: () -> Unit): Observable<T> =
    doOnNext {
        if (condition) {
            doAction()
        }
    }
        .skipWhile { condition }



