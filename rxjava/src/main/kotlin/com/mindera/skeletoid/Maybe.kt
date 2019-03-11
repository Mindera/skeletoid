package com.mindera.skeletoid

import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T : Any> Maybe<T>.subscribeOnIO(): Maybe<T>
        = subscribeOn(Schedulers.io())

fun <T : Any> Maybe<T>.subscribeOnMain(): Maybe<T>
        = subscribeOn(AndroidSchedulers.mainThread())

fun <T : Any> Maybe<T>.observeOnIO(): Maybe<T>
        = observeOn(Schedulers.io())

fun <T : Any> Maybe<T>.observeOnMain(): Maybe<T>
        = observeOn(AndroidSchedulers.mainThread())