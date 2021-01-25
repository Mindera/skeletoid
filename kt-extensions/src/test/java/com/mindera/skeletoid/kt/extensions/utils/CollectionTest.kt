package com.mindera.skeletoid.kt.extensions.utils

import org.junit.Assert
import org.junit.Test

class CollectionTest {

    @Test
    fun `test nullIfEmpty with non empty collection`() {
        val collection = listOf("One", "Two", "Three")
        Assert.assertNotNull(collection.nullIfEmpty())
    }

    @Test
    fun `test nullIfEmpty with empty collection`() {
        val collection = listOf<String>()
        Assert.assertNull(collection.nullIfEmpty())
    }
}