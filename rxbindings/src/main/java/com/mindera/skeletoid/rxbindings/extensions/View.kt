package com.mindera.skeletoid.rxbindings.extensions

import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import com.mindera.skeletoid.observeOnMain
import com.mindera.skeletoid.subscribeOnMain
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

var CLICK_THROTTLE = 2000L

fun View.bindThrottledTouch(action: () -> Unit): Disposable {
    return this.clicksThrottle()
            .subscribeOnMain()
            .observeOnMain()
            .subscribe { action() }
}

fun View.clicksThrottle(): Observable<Unit> =
        this.clicks().throttleFirst(CLICK_THROTTLE, TimeUnit.MILLISECONDS)