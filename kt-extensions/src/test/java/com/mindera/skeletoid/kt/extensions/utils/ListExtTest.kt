package com.mindera.skeletoid.kt.extensions.utils

import org.junit.Assert
import org.junit.Test

class ListExtTest {

    @Test
    fun `test nullIfEmpty with non empty collection`() {
        val list = listOf("One", "Two", "Three")
        Assert.assertNotNull(list.nullIfEmpty())
    }

    @Test
    fun `test nullIfEmpty with empty collection`() {
        val list = listOf<String>()
        Assert.assertNull(list.nullIfEmpty())
    }
}