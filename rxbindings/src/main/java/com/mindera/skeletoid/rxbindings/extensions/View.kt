package com.mindera.skeletoid.rxbindings.extensions

import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

var CLICK_THROTTLE = 2000L

fun View.bindThrottledTouch(action: () -> Unit): Disposable {
    return this.clicksThrottle()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { action() }
}

fun View.clicksThrottle(): Observable<Unit> =
        this.clicks().throttleFirst(CLICK_THROTTLE, TimeUnit.MILLISECONDS)