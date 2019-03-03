package com.mindera.skeletoid.kt.extensions.stlib

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class LetUnitTest {

    @Test
    fun testMultipleLetPair_isNull() {

        // having
        val str1: String? = "str1"
        val str2: String? = null

        // when
        val observed = multipleLet(str1, str2) { _, _ ->
            "result"
        }

        // then
        assertNull(observed)
    }

    @Test
    fun testMultipleLetPair_isNotNull() {

        // having
        val str1: String? = "str1"
        val str2: String? = "str2"

        // when
        val observed = multipleLet(str1, str2) { s1, s2 ->
            assertNotNull(s1)
            assertNotNull(s2)
            return@multipleLet "result"
        }

        // then
        assertNotNull(observed)
    }

    @Test
    fun testMultipleLetTriple_isNull() {

        // having
        val str1: String? = "str1"
        val str2: String? = "str2"
        val str3: String? = null

        // when
        val observed = multipleLet(str1, str2, str3) { _, _, _ ->
            "result"
        }

        // then
        assertNull(observed)
    }

    @Test
    fun testMultipleLetTriple_isNotNull() {

        // having
        val str1: String? = "str1"
        val str2: String? = "str2"
        val str3: String? = "str3"

        // when
        val observed = multipleLet(str1, str2, str3) { s1, s2, s3 ->
            assertNotNull(s1)
            assertNotNull(s2)
            assertNotNull(s3)
            return@multipleLet "result"
        }

        // then
        assertNotNull(observed)
    }

    @Test
    fun testMultipleLetQuadruple_isNull() {

        // having
        val str1: String? = "str1"
        val str2: String? = "str2"
        val str3: String? = "str3"
        val str4: String? = null

        // when
        val observed = multipleLet(str1, str2, str3, str4) { _, _, _, _ ->
            "result"
        }

        // then
        assertNull(observed)
    }

    @Test
    fun testMultipleLetQuadruple_isNotNull() {

        // having
        val str1: String? = "str1"
        val str2: String? = "str2"
        val str3: String? = "str3"
        val str4: String? = "str4"

        // when
        val observed = multipleLet(str1, str2, str3, str4) { s1, s2, s3, s4 ->
            assertNotNull(s1)
            assertNotNull(s2)
            assertNotNull(s3)
            assertNotNull(s4)
            return@multipleLet "result"
        }

        // then
        assertNotNull(observed)
    }

    @Test
    fun testMultipleLetQuintuple_isNull() {

        // having
        val str1: String? = "str1"
        val str2: String? = "str2"
        val str3: String? = "str3"
        val str4: String? = "str4"
        val str5: String? = null

        // when
        val observed = multipleLet(str1, str2, str3, str4, str5) { _, _, _, _, _ ->
            "result"
        }

        // then
        assertNull(observed)
    }

    @Test
    fun testMultipleLetQuintuple_isNotNull() {

        // having
        val str1: String? = "str1"
        val str2: String? = "str2"
        val str3: String? = "str3"
        val str4: String? = "str4"
        val str5: String? = "str5"

        // when
        val observed = multipleLet(str1, str2, str3, str4, str5) { s1, s2, s3, s4, s5 ->
            assertNotNull(s1)
            assertNotNull(s2)
            assertNotNull(s3)
            assertNotNull(s4)
            assertNotNull(s5)
            return@multipleLet "result"
        }

        // then
        assertNotNull(observed)
    }
}