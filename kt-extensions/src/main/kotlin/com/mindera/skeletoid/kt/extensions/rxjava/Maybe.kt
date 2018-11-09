package com.mindera.skeletoid.kt.extensions.rxjava

import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

inline fun <T : Any> Maybe<T>.subscribeIO(): Maybe<T>
        = subscribeOn(Schedulers.io())

inline fun <T : Any> Maybe<T>.subscribeMain(): Maybe<T>
        = subscribeOn(AndroidSchedulers.mainThread())


inline fun <T : Any> Maybe<T>.observeIO(): Maybe<T>
        = observeOn(Schedulers.io())

inline fun <T : Any> Maybe<T>.observeMain(): Maybe<T>
        = observeOn(AndroidSchedulers.mainThread())