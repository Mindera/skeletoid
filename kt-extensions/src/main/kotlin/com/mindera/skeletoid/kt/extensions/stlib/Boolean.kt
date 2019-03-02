package com.mindera.skeletoid.kt.extensions.stlib

fun Boolean?.isTrueAndNotNull(): Boolean {
    return this == true
}

fun Boolean?.isFalseAndNotNull(): Boolean {
    return this == false
}

fun Boolean?.isTrueOrNull(): Boolean {
    return this != false
}

fun Boolean?.isFalseOrNull(): Boolean {
    return this != true
}