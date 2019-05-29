package com.mindera.skeletoid.utils.extensions

import org.mockito.Mockito

inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)