package com.mindera.skeletoid.network;

import org.junit.Test;

import android.content.Context;
import android.content.Intent;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ConnectivityReceiverUnitTests {

    @Test
    public void testConstructor() {
        assertNotNull(new ConnectivityReceiver());
    }


    @Test
    public void testBroadcast() {
        Context context = mock(Context.class);
        Intent intent = mock(Intent.class);
        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        connectivityReceiver.onReceive(context, intent);
        ConnectivityReceiver.mConnectivityCallback = new ConnectivityReceiver.ConnectivityCallback() {
            @Override
            public void connectivityUpdate(boolean isConnectedToANetwork,
                    boolean networkHasInternetAccess,
                    boolean isNetworkWiFi) {
                assertFalse(isConnectedToANetwork);
                assertFalse(networkHasInternetAccess);
                assertFalse(isNetworkWiFi);
            }
        };

    }

    @Test
    public void testValidateInternetStatus() {
        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.mConnectivityCallback = new ConnectivityReceiver.ConnectivityCallback() {
            @Override
            public void connectivityUpdate(boolean isConnectedToANetwork,
                    boolean networkHasInternetAccess,
                    boolean isNetworkWiFi) {
                assertFalse(isConnectedToANetwork);
                assertFalse(networkHasInternetAccess);
                assertFalse(isNetworkWiFi);
            }
        };
        connectivityReceiver.validateInternetStatus(1);


    }
}
