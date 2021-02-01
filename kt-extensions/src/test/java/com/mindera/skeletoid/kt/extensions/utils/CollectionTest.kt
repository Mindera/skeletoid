package com.mindera.skeletoid.kt.extensions.utils

import org.junit.Assert
import org.junit.Test

class CollectionTest {

    @Test
    fun `test nullIfEmpty with non empty collection`() {
        val collection = setOf("One", "Two", "Three")
        Assert.assertNotNull(collection.nullIfEmpty())
    }

    @Test
    fun `test nullIfEmpty with empty collection`() {
        val collection = setOf<String>()
        Assert.assertNull(collection.nullIfEmpty())
    }
}