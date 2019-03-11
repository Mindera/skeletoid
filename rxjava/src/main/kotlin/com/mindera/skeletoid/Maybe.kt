package com.mindera.skeletoid

import com.mindera.skeletoid.schedulers.Schedulers
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers

fun <T : Any> Maybe<T>.subscribeOnIO(): Maybe<T>
        = subscribeOn(Schedulers.io())

fun <T : Any> Maybe<T>.subscribeOnMain(): Maybe<T>
        = subscribeOn(AndroidSchedulers.mainThread())

fun <T : Any> Maybe<T>.observeOnIO(): Maybe<T>
        = observeOn(Schedulers.io())

fun <T : Any> Maybe<T>.observeOnMain(): Maybe<T>
        = observeOn(AndroidSchedulers.mainThread())