package com.mindera.skeletoid

import com.mindera.skeletoid.BreadcrumbExtension.breadcrumbsEnabled
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.exceptions.CompositeException

object BreadcrumbExtension {
    var breadcrumbsEnabled = false
}

//More info: https://rongi.github.io/kotlin-blog/rxjava/2017/09/25/breadcrumbs-rxjava-error-handling.html
fun <T> Observable<T>.dropBreadcrumb(): Observable<T> {
    if (breadcrumbsEnabled) {
        val breadcrumb = com.mindera.skeletoid.BreadcrumbException()
        return this.onErrorResumeNext { error: Throwable ->
            throw CompositeException(error, breadcrumb)
        }
    }

    return this
}

fun <T> Single<T>.dropBreadcrumb(): Single<T> {
    if (breadcrumbsEnabled) {
        val breadcrumb = com.mindera.skeletoid.BreadcrumbException()
        return this.onErrorResumeNext { error: Throwable ->
            throw CompositeException(error, breadcrumb)
        }
    }

    return this
}

fun <T> Maybe<T>.dropBreadcrumb(): Maybe<T> {
    if (breadcrumbsEnabled) {
        val breadcrumb = com.mindera.skeletoid.BreadcrumbException()
        return this.onErrorResumeNext { error: Throwable ->
            throw CompositeException(error, breadcrumb)
        }
    }

    return this
}

class BreadcrumbException : Exception()
