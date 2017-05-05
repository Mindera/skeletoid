package com.mindera.skeletoid.generic;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StringUtilsUnitTests {

    @Test
    public void testsOrdinalIndexNullString() {
        assertEquals(-1, StringUtils.ordinalIndexOf(null, "abc", 1));
    }

    @Test
    public void testsOrdinalIndexOfNullSubstring() {
        assertEquals(-1, StringUtils.ordinalIndexOf("abc", null, 1));
    }

    @Test
    public void testsOrdinalIndexOfNullStrings() {
        assertEquals(-1, StringUtils.ordinalIndexOf(null, null, 1));
    }

    @Test
    public void testsOrdinalIndexOfEmptyStrings() {
        assertEquals(-1, StringUtils.ordinalIndexOf("", "", 5));
    }

    @Test
    public void testsOrdinalIndexOfInvalidIndex() {
        assertEquals(-1, StringUtils.ordinalIndexOf("abcabc", "c", -2));
    }

    @Test
    public void testsOrdinalIndexOf() {
        assertEquals(5, StringUtils.ordinalIndexOf("abcabc", "c", 2));
    }
}
