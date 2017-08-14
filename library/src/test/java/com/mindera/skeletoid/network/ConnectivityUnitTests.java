package com.mindera.skeletoid.network;

import org.junit.Test;

import android.content.Context;
import android.net.NetworkInfo;

import java.net.URI;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class ConnectivityUnitTests {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new Connectivity();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsConnectedAndWithInternetAvailableWithNullArguments() {
        Connectivity.isConnectedAndWithInternetAvailable(null);

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

    @Test
    public void testConnectivityCallback() {
        ConnectivityReceiver.ConnectivityCallback callback = mock(ConnectivityReceiver.ConnectivityCallback.class);
        Connectivity.setConnectivityCallback(callback);

        assertEquals(callback, ConnectivityReceiver.mConnectivityCallback);

    }

    @Test
    public void testRemoveConnectivityCallback() {
        ConnectivityReceiver.ConnectivityCallback callback = mock(ConnectivityReceiver.ConnectivityCallback.class);
        Connectivity.setConnectivityCallback(callback);
        Connectivity.removeConnectivityCallback();
        assertEquals(null, ConnectivityReceiver.mConnectivityCallback);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateConnectivityValidationAddressNull() {
        Connectivity.updateConnectivityValidationAddress(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateConnectivityValidationAddressEmpty() {
        Connectivity.updateConnectivityValidationAddress(URI.create(""));
    }

    @Test
    public void testUpdateConnectivityValidationAddress() {
        Connectivity.updateConnectivityValidationAddress(URI.create("http://www.google.com"));
        assertEquals("http://www.google.com", ConnectivityReceiver.mInternetAddress);
        assertEquals("http://www.google", ConnectivityReceiver.mInternetHttpValidationAddress);
        assertEquals("https://www.google", ConnectivityReceiver.mInternetHttpsValidationAddress);
    }
}
