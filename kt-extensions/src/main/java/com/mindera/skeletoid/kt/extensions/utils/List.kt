package com.mindera.skeletoid.kt.extensions.utils

fun <T: Any> List<T>.nullIfEmpty(): List<T>? {
    return if (isEmpty()) null else this
}