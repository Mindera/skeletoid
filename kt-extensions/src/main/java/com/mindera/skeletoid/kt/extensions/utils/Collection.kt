package com.mindera.skeletoid.kt.extensions.utils

fun <T: Any> Collection<T>.nullIfEmpty(): Collection<T>? {
    return if (isEmpty()) null else this
}

fun <T: Any> List<T>.nullIfEmpty(): List<T>? {
    return if (isEmpty()) null else this
}