package com.mindera.skeletoid.network;

import android.content.Context;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class ConnectivityUnitTests {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new Connectivity();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsConnectedWithNullArguments() {
        Connectivity.isConnected(null);

    }

    @Test
    public void testIsConnected() {
        Context context  = mock(Context.class);
        assertFalse(Connectivity.isConnected(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsConnectedToWIFIWithNullArguments() {
        Connectivity.isConnectedToWIFI(null);

    }

    @Test
    public void testIsConnectedToWIFI() {
        Context context  = mock(Context.class);
        assertFalse(Connectivity.isConnectedToWIFI(context));
    }
}
