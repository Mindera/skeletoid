package com.mindera.skeletoid.kt.extensions.stlib

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BooleanUnitTest {

    @Test
    fun testIsTrueAndNotNull_isTrue() {

        // having
        val someBool: Boolean? = true

        // when
        val observed = someBool.isTrueAndNotNull()

        // then
        assertTrue(observed)
    }

    @Test
    fun testIsTrueAndNotNull_isFalse() {

        // having
        val someBool: Boolean? = false
        val otherBool: Boolean? = null

        // when
        val observed = someBool.isTrueAndNotNull()
        val observedOther = otherBool.isTrueAndNotNull()

        // then
        assertFalse(observed)
        assertFalse(observedOther)
    }

    @Test
    fun testIsFalseAndNotNull_isTrue() {

        // having
        val someBool: Boolean? = false

        // when
        val observed = someBool.isFalseAndNotNull()

        // then
        assertTrue(observed)
    }

    @Test
    fun testIsFalseAndNotNull_isFalse() {

        // having
        val someBool: Boolean? = true
        val otherBool: Boolean? = null

        // when
        val observed = someBool.isFalseAndNotNull()
        val observedOther = otherBool.isFalseAndNotNull()

        // then
        assertFalse(observed)
        assertFalse(observedOther)
    }

    @Test
    fun testIsTrueOrNull_isTrue() {

        // having
        val someBool: Boolean? = true
        val otherBool: Boolean? = null

        // when
        val observed = someBool.isTrueOrNull()
        val observedOther = otherBool.isTrueOrNull()

        // then
        assertTrue(observed)
        assertTrue(observedOther)
    }

    @Test
    fun testIsTrueOrNull_isFalse() {

        // having
        val someBool: Boolean? = false

        // when
        val observed = someBool.isTrueOrNull()

        // then
        assertFalse(observed)
    }

    @Test
    fun testIsFalseOrNull_isTrue() {

        // having
        val someBool: Boolean? = false
        val otherBool: Boolean? = null

        // when
        val observed = someBool.isFalseOrNull()
        val observedOther = otherBool.isFalseOrNull()

        // then
        assertTrue(observed)
        assertTrue(observedOther)
    }

    @Test
    fun testIsFalseOrNull_isFalse() {

        // having
        val someBool: Boolean? = true

        // when
        val observed = someBool.isFalseOrNull()

        // then
        assertFalse(observed)
    }
}
