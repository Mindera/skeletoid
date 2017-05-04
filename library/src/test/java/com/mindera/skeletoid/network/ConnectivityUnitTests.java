package com.mindera.skeletoid.network;


import org.junit.Test;

public class ConnectivityUnitTests {

    @Test(expected = IllegalArgumentException.class)
    public void testIsConnectedAndWithInternetAvailableWithNullArguments() {
        Connectivity.isConnectedAndWithInternetAvailable(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsConnectedWithNullArguments() {
        Connectivity.isConnected(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsConnectedToWIFIWithNullArguments() {
        Connectivity.isConnectedToWIFI(null);

    }
}
