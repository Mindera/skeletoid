package com.mindera.skeletoid.generic;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class DateUtilsUnitTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new DateUtils();
    }

    @Test
    public void testSameDay() {
        long after = 1481429921000L;
        long original = 1481429921000L;

        assertTrue(DateUtils.isSameDayOrAfter(after, original));
    }

    @Test
    public void testAfterYear() {
        long after = 1481429921000L;
        long original = 1381429921000L;

        assertTrue(DateUtils.isSameDayOrAfter(after, original));
    }

    @Test
    public void testBeforeYear() {
        long after = 1381429921000L;
        long original = 1481429921000L;

        assertFalse(DateUtils.isSameDayOrAfter(after, original));
    }

    @Test
    public void testBeforeDay() {
        long after = 1480429921000L;
        long original = 1481429921000L;

        assertFalse(DateUtils.isSameDayOrAfter(after, original));
    }
}
