package com.mindera.skeletoid.rxbindings.extensions.views

import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.longClicks
import com.mindera.skeletoid.rxjava.CLICK_LONG_THROTTLE
import com.mindera.skeletoid.rxjava.observeOnMain
import com.mindera.skeletoid.rxjava.subscribeOnMain
import com.mindera.skeletoid.rxjava.throttleUnit
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

fun View.bindThrottledTouch(action: (View) -> Unit): Disposable {
    return clicksThrottle()
            .subscribeOnMain()
            .observeOnMain()
            .subscribe { action(this) }
}

fun View.clicksThrottle(throttleTime: Long = CLICK_LONG_THROTTLE): Observable<Unit> =
        this.clicks().throttleUnit(throttleTime)

fun View.longClicksThrottle(): Observable<Unit> = this.longClicks().throttleUnit()
