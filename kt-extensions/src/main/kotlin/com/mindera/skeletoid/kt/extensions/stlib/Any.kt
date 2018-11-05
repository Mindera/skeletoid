@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.kt.extensions.stlib


inline fun Any?.isNullOrTrue(): Boolean {
    return this != false
}

inline fun Any?.isNullOrFalse(): Boolean {
    return this != true
}

inline fun Any?.isNotNullAndTrue(): Boolean {
    return this == true
}

inline fun Any?.isNotNullAndFalse(): Boolean {
    return this == false
}