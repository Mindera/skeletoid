package com.mindera.skeletoid.generic;

import com.mindera.skeletoid.logs.LOG;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LOG.class)
public class DebugToolsUnitTests {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new DebugTools();
    }

    @Test
    public void testPrintAllStackTraces() {
        mockStatic(LOG.class);

        DebugTools.printAllStackTraces(String.class);

        verifyStatic(LOG.class, VerificationModeFactory.times(1));
        LOG.d(eq(String.class.toString()), eq("DUMPING ALL STACK TRACES"));
        verifyStatic(LOG.class, VerificationModeFactory.atLeastOnce());
        LOG.d(eq(String.class.toString()), anyString());
    }
}
