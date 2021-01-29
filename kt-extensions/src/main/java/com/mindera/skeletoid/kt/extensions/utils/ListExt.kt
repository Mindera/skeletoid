package com.mindera.skeletoid.kt.extensions.utils

fun <T> List<T>.nullIfEmpty() = if (isEmpty()) null else this