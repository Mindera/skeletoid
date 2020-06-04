package com.mindera.skeletoid.generic

import com.mindera.skeletoid.generic.StringUtils.ordinalIndexOf
import junit.framework.Assert
import org.junit.Test

class StringUtilsUnitTests {

    @Test
    fun testsOrdinalIndexNullString() {
        Assert.assertEquals(
            -1,
            ordinalIndexOf(null, "abc", 1)
        )
    }

    @Test
    fun testsOrdinalIndexOfNullSubstring() {
        Assert.assertEquals(
            -1,
            ordinalIndexOf("abc", null, 1)
        )
    }

    @Test
    fun testsOrdinalIndexOfNullStrings() {
        Assert.assertEquals(
            -1,
            ordinalIndexOf(null, null, 1)
        )
    }

    @Test
    fun testsOrdinalIndexOfEmptyStrings() {
        Assert.assertEquals(
            -1,
            ordinalIndexOf("", "", 5)
        )
    }

    @Test
    fun testsOrdinalIndexOfInvalidIndex() {
        Assert.assertEquals(
            -1,
            ordinalIndexOf("abcabc", "c", -2)
        )
    }

    @Test
    fun testsOrdinalIndexOf() {
        Assert.assertEquals(
            5,
            ordinalIndexOf("abcabc", "c", 2)
        )
    }
}