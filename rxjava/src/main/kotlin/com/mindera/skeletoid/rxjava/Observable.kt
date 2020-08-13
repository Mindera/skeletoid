@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.rxjava

import com.mindera.skeletoid.rxjava.schedulers.Schedulers
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Predicate
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

fun <T : Any> Observable<T>.subscribeOnIO(): Observable<T> = subscribeOn(Schedulers.io())

fun <T : Any> Observable<T>.observeOnIO(): Observable<T> = observeOn(Schedulers.io())


fun <T : Any> Observable<T>.subscribeOnComputation(): Observable<T> =
    subscribeOn(Schedulers.computation())

fun <T : Any> Observable<T>.observeOnComputation(): Observable<T> =
    observeOn(Schedulers.computation())


fun <T : Any> Observable<T>.subscribeOnMain(): Observable<T> =
    subscribeOn(Schedulers.main())

fun <T : Any> Observable<T>.observeOnMain(): Observable<T> =
    observeOn(Schedulers.main())


//Use this to maintain a list of shared Observables (use together with allowMultipleSubscribers)
@Suppress("UNCHECKED_CAST")
fun <T : Any> Observable<T>.createUniqueConcurrentRequestCache(
    requestMap: ConcurrentHashMap<String, Observable<*>>,
    key: String
): Observable<T> {

    if (requestMap[key] is Observable) {
        return requestMap[key] as Observable<T>
    }

    val obs = this.doAfterNext{
        requestMap.remove(key)
    }
    .doFinally {
        //safe case for when there is an error or dispose..
        //https://stackoverflow.com/questions/47306699/difference-between-doafterterminate-and-dofinally
        requestMap.remove(key)
    }

    requestMap[key] = obs
    return obs
}

/**
 * Holds an item and an exception. Used in [delayAtLeast]
 */
internal data class DataHolder<T>(val something: T? = null, val throwable: Throwable? = null)

/**
 * Extension that always waits for at least timeToWait in the provided timeUnit before emitting.
 * This will happen even if an exception is thrown.
 * Used to prevent loadings to just blink when a connection is too fast.
 *
 * @param timeToWait - the time to wait
 * @param timeUnit - the unit of timeToWait
 */
fun <T : Any> Observable<T>.delayAtLeast(
    timeToWait: Long = 1000,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS
): Observable<T> {
    return Observable.zip<DataHolder<T>, Long, DataHolder<T>>(
        this.map { DataHolder(something = it) }.onErrorReturn { DataHolder(throwable = it) },
        Observable.timer(timeToWait, timeUnit),
        BiFunction { t, _ -> t }
    )
    .map { it.something ?: throw it.throwable ?: Exception() }
}

fun <T : Any> Observable<T>.allowMultipleSubscribers(): Observable<T> =
    share()
        .replay(1)
        .autoConnect(1)

fun <T : Any> Observable<T>.skipWhileAndDo(predicate: Predicate<T>, action: (value: T) -> Unit): Observable<T> =
    doOnNext {
        if (predicate.test(it)) {
            action(it)
        }
    }
    .skipWhile(predicate)

fun <T : Any> Observable<T>.filterOrElse(predicate: Predicate<T>, action: (value: T) -> Unit): Observable<T> =
    doOnNext {
        if (!predicate.test(it)) {
            action(it)
        }
    }
    .filter(predicate)

var CLICK_SHORT_THROTTLE = 1000L
var CLICK_LONG_THROTTLE = 2000L

fun <T> Observable<T>.throttle(throttleTime: Long = CLICK_LONG_THROTTLE): Observable<T> = throttleFirst(throttleTime, TimeUnit.MILLISECONDS)

fun <T> Observable<T>.throttleUnit(throttleTime: Long = CLICK_LONG_THROTTLE): Observable<Unit> = throttle(throttleTime).map { Unit }