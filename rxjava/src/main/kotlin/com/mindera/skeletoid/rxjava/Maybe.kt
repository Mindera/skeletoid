package com.mindera.skeletoid.rxjava

import com.mindera.skeletoid.rxjava.schedulers.Schedulers
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers

fun <T : Any> Maybe<T>.subscribeOnIO(): Maybe<T>
        = subscribeOn(Schedulers.io())

fun <T : Any> Maybe<T>.observeOnIO(): Maybe<T>
        = observeOn(Schedulers.io())


fun <T : Any> Maybe<T>.subscribeOnComputation(): Maybe<T>
        = subscribeOn(Schedulers.computation())

fun <T : Any> Maybe<T>.observeOnComputation(): Maybe<T>
        = observeOn(Schedulers.computation())


fun <T : Any> Maybe<T>.subscribeOnMain(): Maybe<T>
        = subscribeOn(AndroidSchedulers.mainThread())

fun <T : Any> Maybe<T>.observeOnMain(): Maybe<T>
        = observeOn(AndroidSchedulers.mainThread())

fun <T> Maybe<T>.filterAndDo(condition: Boolean, functionToRunIfConditionIsMet: () -> Unit): Maybe<T> =
    doOnSubscribe {
        if (condition) {
            functionToRunIfConditionIsMet()
        }
    }
        .filter { condition }