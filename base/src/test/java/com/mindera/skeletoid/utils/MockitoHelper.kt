package com.mindera.skeletoid.utils

import org.mockito.Mockito

object MockitoHelper {
    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }

   fun <T> eq(value: T): T {
        Mockito.eq(value)
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T =  null as T
}