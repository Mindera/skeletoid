package com.mindera.skeletoid.kt.extensions.stlib

fun Any?.isTrueAndNotNull(): Boolean {
    return this == true
}

fun Any?.isFalseAndNotNull(): Boolean {
    return this == false
}

fun Any?.isTrueOrNull(): Boolean {
    return this != false
}

fun Any?.isFalseOrNull(): Boolean {
    return this != true
}