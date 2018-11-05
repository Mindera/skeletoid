@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.stlib

inline fun Any?.isTrueAndNotNull(): Boolean {
    return this == true
}

inline fun Any?.isFalseAndNotNull(): Boolean {
    return this == false
}

inline fun Any?.isTrueOrNull(): Boolean {
    return this != false
}

inline fun Any?.isFalseOrNull(): Boolean {
    return this != true
}