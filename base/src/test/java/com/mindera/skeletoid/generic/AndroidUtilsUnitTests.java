package com.mindera.skeletoid.generic;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Environment.class)
public class AndroidUtilsUnitTests {

    private final String packageName = "com.mindera.skeletoid";

    @After
    public void cleanUp() {
        AndroidUtils.deinit();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new AndroidUtils();
    }

    @Test
    public void testGetCacheDirPath() {
        Context context = mock(Context.class);
        File file = mock(File.class);
        when(context.getCacheDir()).thenReturn(file);
        when(file.getPath()).thenReturn("/"+packageName);

        assertEquals("/"+packageName, AndroidUtils.getCacheDirPath(context, ""));
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
    public void testGetExternalPublicDirectory() {
        mockStatic(Environment.class);
        File file = mock(File.class);
        when(file.getPath()).thenReturn("com.mindera.skeletoid");
        when(Environment.getExternalStorageDirectory()).thenReturn(file);

        assertEquals("com.mindera.skeletoid/dir", AndroidUtils.getExternalPublicDirectory("/dir"));
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
        WindowManager windowManager = mock(WindowManager.class);
        Display display = mock(Display.class);
        Resources resources = mock(Resources.class);
        DisplayMetrics displayMetrics = spy(DisplayMetrics.class);
        displayMetrics.densityDpi = 100;
        when(windowManager.getDefaultDisplay()).thenReturn(display);
        when(context.getSystemService(Context.WINDOW_SERVICE)).thenReturn(windowManager);
        when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        when(context.getResources()).thenReturn(resources);

        assertEquals("Width: 0 px (0.0dp)| Height: 0 px (0.0dp)", AndroidUtils.getDeviceResolution(context));
    }

    @Test
    public void testGetDeviceResolutionNoWindowManager() {
        Context context = mock(Context.class);
        when(context.getSystemService(Context.WINDOW_SERVICE)).thenReturn(null);

        assertNull(AndroidUtils.getDeviceResolution(context));
    }

    @Test
    public void testGetApplicationName() {
        Context context = mock(Context.class);
        ApplicationInfo applicationInfo = mock(ApplicationInfo.class);
        applicationInfo.labelRes = 1;
        when(context.getString(1)).thenReturn("appName");
        when(context.getApplicationInfo()).thenReturn(applicationInfo);

        assertEquals("appName", AndroidUtils.getApplicationName(context));
    }

    @Test
    public void testGetApplicationNameWithoutContext() {
        assertEquals(AndroidUtils.APP_NAME, AndroidUtils.getApplicationName(null));
    }

    @Test
    public void testGetApplicationVersionName() throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        PackageInfo packageInfo = mock(PackageInfo.class);
        packageInfo.versionName = "1.1.1";
        PackageManager packageManager = mock(PackageManager.class);
        when(context.getPackageName()).thenReturn("packageName");
        when(packageManager.getPackageInfo("packageName", PackageManager.GET_META_DATA)).thenReturn(packageInfo);
        when(context.getPackageManager()).thenReturn(packageManager);

        assertEquals("1.1.1", AndroidUtils.getApplicationVersionName(context));
    }

    @Test
    public void testGetApplicationVersionNameNameNotFound() throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        PackageManager packageManager = mock(PackageManager.class);
        when(context.getPackageName()).thenReturn("packageName");
        when(packageManager.getPackageInfo("packageName", PackageManager.GET_META_DATA)).thenThrow(new PackageManager.NameNotFoundException());
        when(context.getPackageManager()).thenReturn(packageManager);

        assertEquals("", AndroidUtils.getApplicationVersionName(context));
    }

    @Test
    public void testGetApplicationVersionNameWithoutContext() {
        assertNull(AndroidUtils.getApplicationVersionName(null));
    }

    @Test
    public void testDefaultGetApplicationVersionCode() {
        Context context = mock(Context.class);
        assertEquals(AndroidUtils.mAppVersionCode, AndroidUtils.getApplicationVersionCode(context));
    }

    @Test
    public void testCacheApplicationVersionName() throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        PackageInfo packageInfo = mock(PackageInfo.class);
        packageInfo.versionName = "1.1.1";
        PackageManager packageManager = mock(PackageManager.class);
        when(context.getPackageName()).thenReturn("packageName");
        when(packageManager.getPackageInfo("packageName", PackageManager.GET_META_DATA)).thenReturn(packageInfo);
        when(context.getPackageManager()).thenReturn(packageManager);

        assertEquals("1.1.1", AndroidUtils.getApplicationVersionName(context));

        packageInfo.versionName = "1.1.2";
        assertEquals("1.1.1", AndroidUtils.getApplicationVersionName(context));
    }

    @Test
    public void testGetApplicationVersionCodeNameNotFound() throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        PackageManager packageManager = mock(PackageManager.class);
        when(context.getPackageName()).thenReturn("packageName");
        when(packageManager.getPackageInfo("packageName", PackageManager.GET_META_DATA)).thenThrow(new PackageManager.NameNotFoundException());
        when(context.getPackageManager()).thenReturn(packageManager);

        assertEquals(AndroidUtils.mAppVersionCode, AndroidUtils.getApplicationVersionCode(context));
    }

    @Test
    public void testGetApplicationVersionCodeWithoutContext() {
        assertEquals(AndroidUtils.mAppVersionCode, AndroidUtils.getApplicationVersionCode(null));
    }

    @Test
    public void testGetApplicationVersionCode() throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        PackageInfo packageInfo = mock(PackageInfo.class);
        packageInfo.versionCode = 1;
        PackageManager packageManager = mock(PackageManager.class);
        when(context.getPackageName()).thenReturn("packageName");
        when(packageManager.getPackageInfo("packageName", PackageManager.GET_META_DATA)).thenReturn(packageInfo);
        when(context.getPackageManager()).thenReturn(packageManager);


        assertEquals(1, AndroidUtils.getApplicationVersionCode(context));
    }

    @Test
    public void testCacheApplicationVersionCode() throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        PackageInfo packageInfo = mock(PackageInfo.class);
        packageInfo.versionCode = 1;
        PackageManager packageManager = mock(PackageManager.class);
        when(context.getPackageName()).thenReturn("packageName");
        when(packageManager.getPackageInfo("packageName", PackageManager.GET_META_DATA)).thenReturn(packageInfo);
        when(context.getPackageManager()).thenReturn(packageManager);

        assertEquals(1, AndroidUtils.getApplicationVersionCode(context));

        packageInfo.versionCode = 2;
        assertEquals(1, AndroidUtils.getApplicationVersionCode(context));
    }

    @Test
    public void testGetApplicationPackage() throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        Context appContext = mock(Context.class);
        PackageInfo packageInfo = mock(PackageInfo.class);
        packageInfo.packageName = "packageName";
        PackageManager packageManager = mock(PackageManager.class);
        when(packageManager.getPackageInfo("packageName", PackageManager.GET_META_DATA)).thenReturn(packageInfo);
        when(context.getPackageManager()).thenReturn(packageManager);
        when(appContext.getPackageName()).thenReturn("appPackageName");
        when(context.getPackageName()).thenReturn("packageName");
        when(context.getApplicationContext()).thenReturn(appContext);

        assertEquals("packageName", AndroidUtils.getApplicationPackage(context));
    }

    @Test
    public void testCacheApplicationPackage() throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        Context appContext = mock(Context.class);
        PackageInfo packageInfo = mock(PackageInfo.class);
        packageInfo.packageName = "packageName";
        PackageManager packageManager = mock(PackageManager.class);
        when(packageManager.getPackageInfo("packageName", PackageManager.GET_META_DATA)).thenReturn(packageInfo);
        when(context.getPackageManager()).thenReturn(packageManager);
        when(appContext.getPackageName()).thenReturn("appPackageName");
        when(context.getPackageName()).thenReturn("packageName");
        when(context.getApplicationContext()).thenReturn(appContext);

        assertEquals("packageName", AndroidUtils.getApplicationPackage(context));

        packageInfo.packageName = "anotherPackageName";
        assertEquals("packageName", AndroidUtils.getApplicationPackage(context));
    }

    @Test
    public void testGetApplicationPackageNameNotFound() throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        Context appContext = mock(Context.class);
        PackageManager packageManager = mock(PackageManager.class);
        when(packageManager.getPackageInfo("packageName", PackageManager.GET_META_DATA)).thenThrow(new PackageManager.NameNotFoundException());
        when(context.getPackageManager()).thenReturn(packageManager);
        when(appContext.getPackageName()).thenReturn("appPackageName");
        when(context.getPackageName()).thenReturn("packageName");
        when(context.getApplicationContext()).thenReturn(appContext);

        assertEquals("appPackageName", AndroidUtils.getApplicationPackage(context));
    }

    @Test
    public void testGetApplicationPackageWithoutContext() {
        assertEquals(AndroidUtils.mAppPackage, AndroidUtils.getApplicationPackage(null));
    }

    @Test
    public void testCheckIfPackageIsNotInstalledNameNotFound() throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        PackageManager packageManager = mock(PackageManager.class);
        when(packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)).thenThrow(new PackageManager.NameNotFoundException());
        when(context.getPackageManager()).thenReturn(packageManager);

        assertFalse(AndroidUtils.checkIfPackageIsInstalled(context, packageName));
    }

    @Test
    public void testCheckIfPackageIsNotInstalledNullPointer() {
        Context context = mock(Context.class);

        assertFalse(AndroidUtils.checkIfPackageIsInstalled(context, packageName));
    }

    @Test
    public void testCheckIfPackageIsInstalled() throws PackageManager.NameNotFoundException {
        Context context = mock(Context.class);
        PackageInfo packageInfo = mock(PackageInfo.class);
        packageInfo.packageName = "packageName";
        PackageManager packageManager = mock(PackageManager.class);
        when(packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)).thenReturn(packageInfo);
        when(context.getPackageManager()).thenReturn(packageManager);

        assertTrue(AndroidUtils.checkIfPackageIsInstalled(context, packageName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckIfPackageIsInstalledWithoutContext() {
        assertFalse(AndroidUtils.checkIfPackageIsInstalled(null, packageName));
    }

    @Test
    public void testIsPhoneAvailable() {
        Context context = mock(Context.class);
        TelephonyManager telephonyManager = mock(TelephonyManager.class);
        when(telephonyManager.getPhoneType()).thenReturn(TelephonyManager.PHONE_TYPE_GSM);
        when(context.getSystemService(Context.TELEPHONY_SERVICE)).thenReturn(telephonyManager);

        assertTrue(AndroidUtils.isPhoneAvailable(context));
    }

    @Test
    public void testIsPhoneNotAvailableNoTelephonyManager() {
        Context context = mock(Context.class);

        assertFalse(AndroidUtils.isPhoneAvailable(context));
    }

    @Test
    public void testIsPhoneNotAvailablePhoneNone() {
        Context context = mock(Context.class);
        TelephonyManager telephonyManager = mock(TelephonyManager.class);
        when(telephonyManager.getPhoneType()).thenReturn(TelephonyManager.PHONE_TYPE_NONE);
        when(context.getSystemService(Context.TELEPHONY_SERVICE)).thenReturn(telephonyManager);

        assertFalse(AndroidUtils.isPhoneAvailable(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPhoneAvailableWithoutContext() {
        assertFalse(AndroidUtils.isPhoneAvailable(null));
    }

    @Test
    public void testIsServiceRunning() {
        Context context = mock(Context.class);
        ActivityManager activityManager = mock(ActivityManager.class);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = new ArrayList<>();
        ActivityManager.RunningServiceInfo runningServiceInfoA = mockRunningServiceInfo("com.mindera.skeletoid.generic.AndroidUtils");
        ActivityManager.RunningServiceInfo runningServiceInfoB = mockRunningServiceInfo("B");
        runningServiceInfos.add(runningServiceInfoA);
        runningServiceInfos.add(runningServiceInfoB);
        when(activityManager.getRunningServices(Integer.MAX_VALUE)).thenReturn(runningServiceInfos);
        when(context.getSystemService(Context.ACTIVITY_SERVICE)).thenReturn(activityManager);

        assertTrue(AndroidUtils.isServiceRunning(context, AndroidUtils.class));
    }

    @Test
    public void testIsServiceNotRunning() {
        Context context = mock(Context.class);
        ActivityManager activityManager = mock(ActivityManager.class);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = new ArrayList<>();
        ActivityManager.RunningServiceInfo runningServiceInfoA = mockRunningServiceInfo("A");
        ActivityManager.RunningServiceInfo runningServiceInfoB = mockRunningServiceInfo("B");
        runningServiceInfos.add(runningServiceInfoA);
        runningServiceInfos.add(runningServiceInfoB);
        when(activityManager.getRunningServices(Integer.MAX_VALUE)).thenReturn(runningServiceInfos);
        when(context.getSystemService(Context.ACTIVITY_SERVICE)).thenReturn(activityManager);

        assertFalse(AndroidUtils.isServiceRunning(context, AndroidUtils.class));
    }

    @Test
    public void testIsServiceNotRunningNoServices() {
        Context context = mock(Context.class);
        ActivityManager activityManager = mock(ActivityManager.class);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = new ArrayList<>();
        when(activityManager.getRunningServices(Integer.MAX_VALUE)).thenReturn(runningServiceInfos);
        when(context.getSystemService(Context.ACTIVITY_SERVICE)).thenReturn(activityManager);

        assertFalse(AndroidUtils.isServiceRunning(context, AndroidUtils.class));
    }

    @Test
    public void testIsServiceNotRunningNoActivityManager() {
        Context context = mock(Context.class);
        assertFalse(AndroidUtils.isServiceRunning(context, AndroidUtils.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsServiceRunningWithoutContext() {
        assertFalse(AndroidUtils.isServiceRunning(null, AndroidUtils.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsServiceRunningWithoutClass() {
        Context context = mock(Context.class);
        assertFalse(AndroidUtils.isServiceRunning(context, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsServiceRunningWithoutContextAndClass() {
        assertFalse(AndroidUtils.isServiceRunning(null, null));
    }

    private ActivityManager.RunningServiceInfo mockRunningServiceInfo(final String name) {
        ActivityManager.RunningServiceInfo runningServiceInfo = mock(ActivityManager.RunningServiceInfo.class);
        ComponentName componentName = mock(ComponentName.class);
        when(componentName.getClassName()).thenReturn(name);
        runningServiceInfo.service = componentName;
        return runningServiceInfo;
    }
}
