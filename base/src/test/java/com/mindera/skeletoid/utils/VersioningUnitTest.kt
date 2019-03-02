package com.mindera.skeletoid.utils

import com.mindera.skeletoid.utils.versioning.Versioning
import org.junit.Assert.assertTrue
import org.junit.Test

class VersioningUnitTest {

    @Test
    fun testVersioningEqual() {
        val equals = Versioning.compareVersions("1.0.0", "1.0.0")
        assertTrue(equals == 0)
    }

    @Test
    fun testVersioningBiggerBuild() {
        val equals = Versioning.compareVersions("1.0.0", "1.0.1")
        assertTrue(equals < 0)
    }

    @Test
    fun testVersioningBiggerMinor() {
        val equals = Versioning.compareVersions("1.0.0", "1.1.0")
        assertTrue(equals < 0)
    }

    @Test
    fun testVersioningBiggerMajor() {
        val equals = Versioning.compareVersions("1.0.0", "2.0.0")
        assertTrue(equals < 0)
    }

    @Test
    fun testVersioningSmallerBuild() {
        val equals = Versioning.compareVersions("1.0.1", "1.0.0")
        assertTrue(equals > 0)
    }

    @Test
    fun testVersioningSmallerMinor() {
        val equals = Versioning.compareVersions("1.1.0","1.0.0")
        assertTrue(equals > 0)
    }

    @Test
    fun testVersioningSmallerMajor() {
        val equals = Versioning.compareVersions("2.0.0", "1.0.0")
        assertTrue(equals > 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testVersioningWrongInput() {
        Versioning.compareVersions("", "")
    }

    @Test(expected = NumberFormatException::class)
    fun testVersioningWrongInputNonNumeric() {
        Versioning.compareVersions("a", "b")
    }
}