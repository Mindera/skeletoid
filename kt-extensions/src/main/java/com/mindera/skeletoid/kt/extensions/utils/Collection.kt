package com.mindera.skeletoid.kt.extensions.utils

fun Collection<*>.nullIfEmpty(): Collection<*>? {
    return if (isEmpty()) null else this
}