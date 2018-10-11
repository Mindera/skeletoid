package com.mindera.skeletoid.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.util.Log;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class AndroidUtilsUnitTests {

    private final String packageName = "com.mindera.skeletoid";

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new AndroidUtils();
    }

    @Test
    public void testGetFileDirPath() {
        Context context = mock(Context.class);

        File file = mock(File.class);
        when(context.getFilesDir()).thenReturn(file);
        when(file.getPath()).thenReturn(packageName);

        assertEquals(packageName, AndroidUtils.getFileDirPath(context, ""));
    }

    @Test
    public void testGetFileDirPathWithPath() {
        Context context = mock(Context.class);

        File file = mock(File.class);
        when(context.getFilesDir()).thenReturn(file);
        when(file.getPath()).thenReturn("com.mindera.skeletoid");

        assertEquals("com.mindera.skeletoid/dir", AndroidUtils.getFileDirPath(context, "/dir"));
    }

    @Test
    public void testDeviceName() {
        assertEquals("", AndroidUtils.getDeviceName());
    }

    @Test
    public void testDeviceBrand() {
        assertEquals("", AndroidUtils.getDeviceBrand());
    }


    @Test
    public void testOSReleaseVersion() {
        assertEquals("", AndroidUtils.getOSReleaseVersion());
    }


    @Test
    public void testOSSDKVersion() {
        assertEquals(0, AndroidUtils.getOSSDKVersion());
    }

    @Test
    public void testGetDeviceResolution() {
        Context context = mock(Context.class);
        assertNull(AndroidUtils.getDeviceResolution(context));
    }

    @Test
    public void testGetApplicationVersionName() {
        Context context = mock(Context.class);
        assertEquals(AndroidUtils.APP_NAME, AndroidUtils.getApplicationName(context));
    }

    @Test
    public void testGetApplicationVersionNameWithoutContext() {
        assertEquals(AndroidUtils.APP_NAME, AndroidUtils.getApplicationName(null));
    }


    @Test
    public void testGetApplicationVersionCode() {
        Context context = mock(Context.class);
        PowerMockito.mockStatic(Log.class);
        assertEquals(AndroidUtils.mAppVersionCode, AndroidUtils.getApplicationVersionCode(context));
    }

    @Test
    public void testGetApplicationVersionCodeWithoutContext() {
        assertEquals(AndroidUtils.mAppVersionCode, AndroidUtils.getApplicationVersionCode(null));
    }


//    @Test
//    public void testGetApplicationPackage() {
//        Context context = mock(Context.class);
//        assertEquals(AndroidUtils.mAppPackage, AndroidUtils.getApplicationPackage(context));
//    }

    @Test
    public void testGetApplicationPackageWithoutContext() {
        assertEquals(AndroidUtils.mAppPackage, AndroidUtils.getApplicationPackage(null));
    }


    @Test
    public void testCheckIfPackageIsInstalled() {
        Context context = mock(Context.class);
        assertFalse(AndroidUtils.checkIfPackageIsInstalled(context, packageName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckIfPackageIsInstalledWithoutContext() {
        assertFalse(AndroidUtils.checkIfPackageIsInstalled(null, packageName));
    }

    @Test
    public void testIsPhoneAvailable() {
        Context context = mock(Context.class);
        assertFalse(AndroidUtils.isPhoneAvailable(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPhoneAvailableWithoutContext() {
        assertFalse(AndroidUtils.isPhoneAvailable(null));
    }

    @Test
    public void testIsServiceRunning() {
        Context context = mock(Context.class);
        assertFalse(AndroidUtils.isServiceRunning(context, AndroidUtils.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsServiceRunningWithoutContext() {
        assertFalse(AndroidUtils.isServiceRunning(null, AndroidUtils.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsServiceRunningWithoutContextOrClass() {
        assertFalse(AndroidUtils.isServiceRunning(null, null));
    }




}
