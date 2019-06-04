package com.mindera.skeletoid.utils

import com.mindera.skeletoid.utils.versioning.Versioning
import org.junit.Test


class VersioningUnitTest{

    @Test
    fun testVersioningEqual() {
        val equals = Versioning.compareVersions("1.0.0", "1.0.0")
        assert(equals == 0)
    }

    @Test
    fun testVersioningBiggerBuild() {
        val equals = Versioning.compareVersions("1.0.0", "1.0.1")
        assert(equals < 0)
    }

    @Test
    fun testVersioningBiggerMinor() {
        val equals = Versioning.compareVersions("1.0.0", "1.1.0")
        assert(equals < 0)
    }

    @Test
    fun testVersioningBiggerMajor() {
        val equals = Versioning.compareVersions("1.0.0", "2.0.0")
        assert(equals < 0)
    }

    @Test
    fun testVersioningSmallerBuild() {
        val equals = Versioning.compareVersions("1.0.1", "1.0.0")
        assert(equals > 0)
    }

    @Test
    fun testVersioningSmallerMinor() {
        val equals = Versioning.compareVersions("1.1.0","1.0.0")
        assert(equals > 0)
    }

    @Test
    fun testVersioningSmallerMajor() {
        val equals = Versioning.compareVersions("2.0.0", "1.0.0")
        assert(equals > 0)
    }

    @Test
    fun testVersioningBiggerDifferentLenghts() {
        val equals = Versioning.compareVersions("1.2.3.5", "1.2.3")
        assert(equals > 0)
    }

    @Test
    fun testVersioningSmallerDifferentLenghts() {
        val equals = Versioning.compareVersions("1.2.2.5", "1.2.3")
        assert(equals < 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testVersioningEmptyInput() {
        Versioning.compareVersions("", "")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testVersioningEmptyFirstInput() {
        Versioning.compareVersions("", "1.2.3")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testVersioningEmptySecondInput() {
        Versioning.compareVersions("1.2.3", "")
    }

    @Test(expected = NumberFormatException::class)
    fun testVersioningIncorrectInputNonNumeric() {
        Versioning.compareVersions("a", "b")
    }

    @Test(expected = NumberFormatException::class)
    fun testVersioningMalformedInput() {
        Versioning.compareVersions("1.2,4", "1.2.3")
    }

    @Test(expected = NumberFormatException::class)
    fun testVersioningMalformedEmptyInput() {
        Versioning.compareVersions("1..4", "1.2.3")
    }
}