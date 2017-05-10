package com.mindera.skeletoid.network;


import org.junit.Test;

public class ConnectivityReceiverUnitTests {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new ConnectivityReceiver();
    }
}
