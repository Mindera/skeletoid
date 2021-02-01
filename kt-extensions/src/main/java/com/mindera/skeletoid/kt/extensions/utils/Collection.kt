package com.mindera.skeletoid.kt.extensions.utils

fun <T> Collection<T>.nullIfEmpty() = if (isEmpty()) null else this