package com.mindera.skeletoid.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OptionalUnitTest {

    @Test
    fun testEmptyOptional() {

        // having
        val optional = Optional.empty<String>()

        // then
        assertTrue(!optional.isPresent)
    }

    @Test
    fun testOptionalOfValue() {

        // when
        val optional = Optional.of("str1")
        val observed = optional.get()

        // then
        assertTrue(optional.isPresent)
        assertEquals("str1", observed)
    }

    @Test
    fun testOptionalOfNullable() {

        // having
        val str: String? = "str1"

        // when
        val optional = Optional.ofNullable(str)
        val observed = optional.get()

        // then
        assertTrue(optional.isPresent)
        assertEquals(str, observed)
    }

    @Test
    fun testOptionalOfNullableNull() {

        // having
        val str: String? = null
        val empty = Optional.empty<String>()

        // when
        val optional = Optional.ofNullable(str)

        // then
        assertTrue(!optional.isPresent)
        assertEquals(empty, optional)
    }

    @Test
    fun testOptionalToStringEmpty() {

        // having
        val optional = Optional.empty<String>()

        // when
        val observed = optional.toString()

        // then
        assertEquals("Optional.empty", observed)
    }

    @Test
    fun testOptionalToString() {

        // having
        val optional = Optional.of("str1")

        // when
        val observed = optional.toString()

        // then
        assertEquals("Optional[str1]", observed)
    }

    @Test
    fun testOptionalEquals() {

        // having
        val optional = Optional.of("str1")
        val optional2 = Optional.of("str1")

        // when
        val observed = optional == optional2

        // then
        assertTrue(observed)
    }

    @Test
    fun testOptionalReferenceEquals() {

        // having
        val optional = Optional.of("str1")

        // when
        val observed = optional == optional

        // then
        assertTrue(observed)
    }

    @Test
    fun testOptionalNotEquals() {

        // having
        val optional = Optional.of("str1")
        val optional2 = Optional.of(1)

        // when
        val observed = optional == optional2

        // then
        assertFalse(observed)
    }

    @Test
    fun testOptionalHashcode() {

        // having
        val optional = Optional.of("str1")

        // when
        val observed = optional.hashCode()

        // then
        assertTrue(observed != 0)
    }

    @Test(expected = NoSuchElementException::class)
    fun testOptionalEmptyGet() {

        // having
        val empty = Optional.empty<String>()

        // when
        empty.get()
    }
}