package com.mindera.skeletoid.kt.extensions

import org.powermock.api.mockito.PowerMockito

inline fun <reified T : Any> mockStatic() = PowerMockito.mock(T::class.java)
