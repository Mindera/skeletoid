package com.mindera.skeletoid.generic;

import org.junit.Test;

public class DebugToolsUnitTests {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new DebugTools();
    }
}
