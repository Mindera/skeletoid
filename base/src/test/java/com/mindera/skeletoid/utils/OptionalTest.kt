package com.mindera.skeletoid.utils

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class OptionalTest {

    @Test
    fun testIsPresent() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.of(testString)

        assertTrue(optionalString.isPresent)
    }

    @Test
    fun testIsEmptyNotPresent() {
        val optionalString: Optional<String> = Optional.empty()

        assertFalse(optionalString.isPresent)
    }

    @Test
    fun testIsNullablePresent() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.ofNullable(testString)

        assertTrue(optionalString.isPresent)
    }

    @Test
    fun testIsNullableNotPresent() {
        val testString = null
        val optionalString: Optional<String> = Optional.ofNullable(testString)

        assertFalse(optionalString.isPresent)
    }

    @Test
    fun testGet() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.of(testString)

        assertEquals(testString, optionalString.get())
    }

    @Test
    fun testNullableGet() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.ofNullable(testString)

        assertEquals(testString, optionalString.get())
    }

    @Test(expected = NoSuchElementException::class)
    fun testNullableGetThrows() {
        val testString = null
        val optionalString: Optional<String> = Optional.ofNullable(testString)
        optionalString.get()
    }

    @Test(expected = NoSuchElementException::class)
    fun testEmptyGetThrows() {
        val optionalString: Optional<String> = Optional.empty()
        optionalString.get()
    }

    @Test
    fun testEquals() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.of(testString)
        val anotherOptionalString: Optional<String> = Optional.of(testString)

        assertTrue(optionalString.equals(anotherOptionalString))
    }

    @Test
    fun testNotEquals() {
        val testString = "testString"
        val anotherTestString = "anotherTestString"
        val optionalString: Optional<String> = Optional.of(testString)
        val anotherOptionalString: Optional<String> = Optional.of(anotherTestString)

        assertFalse(optionalString.equals(anotherOptionalString))
    }

    @Test
    fun testOptionalNotEqualsEmpty() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.of(testString)
        val anotherOptionalString: Optional<String> = Optional.empty()

        assertFalse(optionalString.equals(anotherOptionalString))
    }

    @Test
    fun testEmptyNotEqualsOptional() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.empty()
        val anotherOptionalString: Optional<String> = Optional.of(testString)

        assertFalse(optionalString.equals(anotherOptionalString))
    }

    @Test
    fun testNotEqualsNull() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.of(testString)

        assertFalse(optionalString.equals(null))
    }

    @Test
    fun testNotEqualsAnotherClass() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.of(testString)
        val testInt = 3
        val optionalInt : Optional<Int> = Optional.of(testInt)

        assertFalse(optionalString.equals(optionalInt))
    }

    @Test
    fun testEmptyNotEqualsNullable() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.empty()
        val anotherOptionalString: Optional<String> = Optional.ofNullable(testString)

        assertFalse(optionalString.equals(anotherOptionalString))
    }

    @Test
    fun testEmptyEqualsNullNullable() {
        val testString = null
        val optionalString: Optional<String> = Optional.empty()
        val anotherOptionalString: Optional<String> = Optional.ofNullable(testString)

        assertTrue(optionalString.equals(anotherOptionalString))
    }

    @Test
    fun testNullNullableEqualsEmpty() {
        val testString = null
        val optionalString: Optional<String> = Optional.ofNullable(testString)
        val anotherOptionalString: Optional<String> = Optional.empty()

        assertTrue(optionalString.equals(anotherOptionalString))
    }

    @Test
    fun testNullableEqualsNullable() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.ofNullable(testString)
        val anotherOptionalString: Optional<String> = Optional.ofNullable(testString)

        assertTrue(optionalString.equals(anotherOptionalString))
    }

    @Test
    fun testNullableEqualsOptional() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.ofNullable(testString)
        val anotherOptionalString: Optional<String> = Optional.of(testString)

        assertTrue(optionalString.equals(anotherOptionalString))
    }

    @Test
    fun testNullNullableNotEqualsNullable() {
        val testString = null
        val anotherTestString = "testString"
        val optionalString: Optional<String> = Optional.ofNullable(testString)
        val anotherOptionalString: Optional<String> = Optional.of(anotherTestString)

        assertFalse(optionalString.equals(anotherOptionalString))
    }

    @Test
    fun testNullNullableEqualsNullNullable() {
        val testString = null
        val optionalString: Optional<String> = Optional.ofNullable(testString)
        val anotherOptionalString: Optional<String> = Optional.ofNullable(testString)

        assertTrue(optionalString.equals(anotherOptionalString))
    }

    @Test
    fun testHashCode() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.of(testString)

        assertNotEquals(0, optionalString.hashCode())
    }

    @Test
    fun testHashCodeEmpty() {
        val optionalString: Optional<String> = Optional.empty()

        assertEquals(0, optionalString.hashCode())
    }

    @Test
    fun testHashCodeNullable() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.ofNullable(testString)

        assertNotEquals(0, optionalString.hashCode())
    }

    @Test
    fun testHashCodeNullNullable() {
        val testString = null
        val optionalString: Optional<String> = Optional.ofNullable(testString)

        assertEquals(0, optionalString.hashCode())
    }

    @Test
    fun testToString() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.of(testString)

        assertEquals("Optional[$testString]", optionalString.toString())
    }

    @Test
    fun testToStringEmpty() {
        val optionalString: Optional<String> = Optional.empty()

        assertEquals("Optional.empty", optionalString.toString())
    }

    @Test
    fun testToStringNullable() {
        val testString = "testString"
        val optionalString: Optional<String> = Optional.ofNullable(testString)

        assertEquals("Optional[$testString]", optionalString.toString())
    }

    @Test
    fun testToStringNullNullable() {
        val testString = null
        val optionalString: Optional<String> = Optional.ofNullable(testString)

        assertEquals("Optional.empty", optionalString.toString())
    }
}