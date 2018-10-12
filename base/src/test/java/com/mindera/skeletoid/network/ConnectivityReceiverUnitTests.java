package com.mindera.skeletoid.network;

import android.content.Context;
import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
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
        ConnectivityReceiver.mConnectivityCallback = mock(ConnectivityReceiver.ConnectivityCallback.class);
        connectivityReceiver.onReceive(context, intent);

        verify(ConnectivityReceiver.mConnectivityCallback).connectivityUpdate(false,false,false);

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
