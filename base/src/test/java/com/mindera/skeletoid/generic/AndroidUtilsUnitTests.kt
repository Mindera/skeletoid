package com.mindera.skeletoid.generic

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Environment
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import com.mindera.skeletoid.generic.AndroidUtils.checkIfPackageIsInstalled
import com.mindera.skeletoid.generic.AndroidUtils.deinit
import com.mindera.skeletoid.generic.AndroidUtils.getApplicationName
import com.mindera.skeletoid.generic.AndroidUtils.getApplicationPackage
import com.mindera.skeletoid.generic.AndroidUtils.getApplicationVersionCode
import com.mindera.skeletoid.generic.AndroidUtils.getApplicationVersionName
import com.mindera.skeletoid.generic.AndroidUtils.getCacheDirPath
import com.mindera.skeletoid.generic.AndroidUtils.getDeviceResolution
import com.mindera.skeletoid.generic.AndroidUtils.getExternalPublicDirectory
import com.mindera.skeletoid.generic.AndroidUtils.getFileDirPath
import com.mindera.skeletoid.generic.AndroidUtils.isPhoneAvailable
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.File

@RunWith(PowerMockRunner::class)
@PrepareForTest(Environment::class)
class AndroidUtilsUnitTests {
    private val packageName = "com.mindera.skeletoid"

    @After
    fun cleanUp() {
        deinit()
    }

    @Test
    fun testGetCacheDirPath() {
        val context =
            Mockito.mock(Context::class.java)
        val file = Mockito.mock(File::class.java)
        Mockito.`when`(context.cacheDir).thenReturn(file)
        Mockito.`when`(file.path).thenReturn("/$packageName")
        Assert.assertEquals(
            "/$packageName",
            getCacheDirPath(context, "")
        )
    }

    @Test
    fun testGetFileDirPath() {
        val context =
            Mockito.mock(Context::class.java)
        val file = Mockito.mock(File::class.java)
        Mockito.`when`(context.filesDir).thenReturn(file)
        Mockito.`when`(file.path).thenReturn(packageName)
        Assert.assertEquals(packageName, getFileDirPath(context, ""))
    }

    @Test
    fun testGetFileDirPathWithPath() {
        val context =
            Mockito.mock(Context::class.java)
        val file = Mockito.mock(File::class.java)
        Mockito.`when`(context.filesDir).thenReturn(file)
        Mockito.`when`(file.path).thenReturn("com.mindera.skeletoid")
        Assert.assertEquals(
            "com.mindera.skeletoid/dir",
            getFileDirPath(context, "/dir")
        )
    }

    @Test
    fun testGetExternalPublicDirectory() {
        PowerMockito.mockStatic(Environment::class.java)
        val file = Mockito.mock(File::class.java)
        Mockito.`when`(file.path).thenReturn("com.mindera.skeletoid")
        Mockito.`when`(Environment.getExternalStorageDirectory())
            .thenReturn(file)
        Assert.assertEquals(
            "com.mindera.skeletoid/dir",
            getExternalPublicDirectory("/dir")
        )
    }

    @Test
    fun testGetDeviceResolution() {
        val context =
            Mockito.mock(Context::class.java)
        val windowManager = Mockito.mock(WindowManager::class.java)
        val display = Mockito.mock(Display::class.java)
        val resources = Mockito.mock(
            Resources::class.java
        )
        val displayMetrics = Mockito.spy(DisplayMetrics::class.java)
        displayMetrics.densityDpi = 100
        Mockito.`when`(windowManager.defaultDisplay).thenReturn(display)
        Mockito.`when`(context.getSystemService(Context.WINDOW_SERVICE))
            .thenReturn(windowManager)
        Mockito.`when`(resources.displayMetrics).thenReturn(displayMetrics)
        Mockito.`when`(context.resources).thenReturn(resources)
        Assert.assertEquals(
            "Width: 0 px (0.0dp)| Height: 0 px (0.0dp)",
            getDeviceResolution(context)
        )
    }

    @Test
    fun testGetDeviceResolutionNoWindowManager() {
        val context =
            Mockito.mock(Context::class.java)
        Mockito.`when`(context.getSystemService(Context.WINDOW_SERVICE))
            .thenReturn(null)
        Assert.assertNull(getDeviceResolution(context))
    }

    @Test
    fun testGetApplicationName() {
        val context =
            Mockito.mock(Context::class.java)
        val applicationInfo =
            Mockito.mock(ApplicationInfo::class.java)
        applicationInfo.labelRes = 1
        Mockito.`when`(context.getString(1)).thenReturn("appName")
        Mockito.`when`(context.applicationInfo).thenReturn(applicationInfo)
        Assert.assertEquals("appName", getApplicationName(context))
    }

    @Test
    @Throws(PackageManager.NameNotFoundException::class)
    fun testGetApplicationVersionName() {
        val context =
            Mockito.mock(Context::class.java)
        val packageInfo =
            Mockito.mock(
                PackageInfo::class.java
            )
        packageInfo.versionName = "1.1.1"
        val packageManager =
            Mockito.mock(PackageManager::class.java)
        Mockito.`when`(context.packageName).thenReturn("packageName")
        Mockito.`when`(
            packageManager.getPackageInfo(
                "packageName",
                PackageManager.GET_META_DATA
            )
        ).thenReturn(packageInfo)
        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Assert.assertEquals(
            "1.1.1",
            getApplicationVersionName(context)
        )
    }

    @Test
    @Throws(PackageManager.NameNotFoundException::class)
    fun testGetApplicationVersionNameNameNotFound() {
        val context =
            Mockito.mock(Context::class.java)
        val packageManager =
            Mockito.mock(PackageManager::class.java)
        Mockito.`when`(context.packageName).thenReturn("packageName")
        Mockito.`when`(
            packageManager.getPackageInfo(
                "packageName",
                PackageManager.GET_META_DATA
            )
        ).thenThrow(PackageManager.NameNotFoundException())
        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Assert.assertEquals(null, getApplicationVersionName(context))
    }

    @Test
    fun testDefaultGetApplicationVersionCode() {
        val context =
            Mockito.mock(Context::class.java)
        Assert.assertEquals(
            AndroidUtils.appVersionCode,
            getApplicationVersionCode(context)
        )
    }

    @Test
    @Throws(PackageManager.NameNotFoundException::class)
    fun testCacheApplicationVersionName() {
        val context =
            Mockito.mock(Context::class.java)
        val packageInfo =
            Mockito.mock(
                PackageInfo::class.java
            )
        packageInfo.versionName = "1.1.1"
        val packageManager =
            Mockito.mock(PackageManager::class.java)
        Mockito.`when`(context.packageName).thenReturn("packageName")
        Mockito.`when`(
            packageManager.getPackageInfo(
                "packageName",
                PackageManager.GET_META_DATA
            )
        ).thenReturn(packageInfo)
        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Assert.assertEquals(
            "1.1.1",
            getApplicationVersionName(context)
        )
        packageInfo.versionName = "1.1.2"
        Assert.assertEquals(
            "1.1.1",
            getApplicationVersionName(context)
        )
    }

    @Test
    @Throws(PackageManager.NameNotFoundException::class)
    fun testGetApplicationVersionCodeNameNotFound() {
        val context =
            Mockito.mock(Context::class.java)
        val packageManager =
            Mockito.mock(PackageManager::class.java)
        Mockito.`when`(context.packageName).thenReturn("packageName")
        Mockito.`when`(
            packageManager.getPackageInfo(
                "packageName",
                PackageManager.GET_META_DATA
            )
        ).thenThrow(PackageManager.NameNotFoundException())
        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Assert.assertEquals(
            AndroidUtils.appVersionCode,
            getApplicationVersionCode(context)
        )
    }

    @Test
    @Throws(PackageManager.NameNotFoundException::class)
    fun testGetApplicationVersionCode() {
        val context =
            Mockito.mock(Context::class.java)
        val packageInfo =
            Mockito.mock(
                PackageInfo::class.java
            )
        packageInfo.versionCode = 1
        val packageManager =
            Mockito.mock(PackageManager::class.java)
        Mockito.`when`(context.packageName).thenReturn("packageName")
        Mockito.`when`(
            packageManager.getPackageInfo(
                "packageName",
                PackageManager.GET_META_DATA
            )
        ).thenReturn(packageInfo)
        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Assert.assertEquals(1, getApplicationVersionCode(context))
    }

    @Test
    @Throws(PackageManager.NameNotFoundException::class)
    fun testCacheApplicationVersionCode() {
        val context =
            Mockito.mock(Context::class.java)
        val packageInfo =
            Mockito.mock(
                PackageInfo::class.java
            )
        packageInfo.versionCode = 1
        val packageManager =
            Mockito.mock(PackageManager::class.java)
        Mockito.`when`(context.packageName).thenReturn("packageName")
        Mockito.`when`(
            packageManager.getPackageInfo(
                "packageName",
                PackageManager.GET_META_DATA
            )
        ).thenReturn(packageInfo)
        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Assert.assertEquals(1, getApplicationVersionCode(context))
        packageInfo.versionCode = 2
        Assert.assertEquals(1, getApplicationVersionCode(context))
    }

    @Test
    @Throws(PackageManager.NameNotFoundException::class)
    fun testGetApplicationPackage() {
        val context =
            Mockito.mock(Context::class.java)
        val appContext =
            Mockito.mock(Context::class.java)
        val packageInfo =
            Mockito.mock(
                PackageInfo::class.java
            )
        packageInfo.packageName = "packageName"
        val packageManager =
            Mockito.mock(PackageManager::class.java)
        Mockito.`when`(
            packageManager.getPackageInfo(
                "packageName",
                PackageManager.GET_META_DATA
            )
        ).thenReturn(packageInfo)
        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Mockito.`when`(appContext.packageName).thenReturn("appPackageName")
        Mockito.`when`(context.packageName).thenReturn("packageName")
        Mockito.`when`(context.applicationContext)
            .thenReturn(appContext)
        Assert.assertEquals(
            "packageName",
            getApplicationPackage(context)
        )
    }

    @Test
    @Throws(PackageManager.NameNotFoundException::class)
    fun testCacheApplicationPackage() {
        val context =
            Mockito.mock(Context::class.java)
        val appContext =
            Mockito.mock(Context::class.java)
        val packageInfo =
            Mockito.mock(
                PackageInfo::class.java
            )
        packageInfo.packageName = "packageName"
        val packageManager =
            Mockito.mock(PackageManager::class.java)
        Mockito.`when`(
            packageManager.getPackageInfo(
                "packageName",
                PackageManager.GET_META_DATA
            )
        ).thenReturn(packageInfo)
        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Mockito.`when`(appContext.packageName).thenReturn("appPackageName")
        Mockito.`when`(context.packageName).thenReturn("packageName")
        Mockito.`when`(context.applicationContext)
            .thenReturn(appContext)
        Assert.assertEquals(
            "packageName",
            getApplicationPackage(context)
        )
        packageInfo.packageName = "anotherPackageName"
        Assert.assertEquals(
            "packageName",
            getApplicationPackage(context)
        )
    }

    @Test
    @Throws(PackageManager.NameNotFoundException::class)
    fun testGetApplicationPackageNameNotFound() {
        val context =
            Mockito.mock(Context::class.java)
        val appContext =
            Mockito.mock(Context::class.java)
        val packageManager =
            Mockito.mock(PackageManager::class.java)
        Mockito.`when`(
            packageManager.getPackageInfo(
                "packageName",
                PackageManager.GET_META_DATA
            )
        ).thenThrow(PackageManager.NameNotFoundException())
        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Mockito.`when`(appContext.packageName).thenReturn("appPackageName")
        Mockito.`when`(context.packageName).thenReturn("packageName")
        Mockito.`when`(context.applicationContext)
            .thenReturn(appContext)
        Assert.assertEquals(
            "appPackageName",
            getApplicationPackage(context)
        )
    }

    @Test
    @Throws(PackageManager.NameNotFoundException::class)
    fun testCheckIfPackageIsNotInstalledNameNotFound() {
        val context =
            Mockito.mock(Context::class.java)
        val packageManager =
            Mockito.mock(PackageManager::class.java)
        Mockito.`when`(
            packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
        ).thenThrow(PackageManager.NameNotFoundException())
        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Assert.assertFalse(
            checkIfPackageIsInstalled(
                context,
                packageName
            )
        )
    }

    @Test
    fun testCheckIfPackageIsNotInstalledNullPointer() {
        val context =
            Mockito.mock(Context::class.java)
        Assert.assertFalse(
            checkIfPackageIsInstalled(
                context,
                packageName
            )
        )
    }

    @Test
    @Throws(PackageManager.NameNotFoundException::class)
    fun testCheckIfPackageIsInstalled() {
        val context =
            Mockito.mock(Context::class.java)
        val packageInfo =
            Mockito.mock(
                PackageInfo::class.java
            )
        packageInfo.packageName = "packageName"
        val packageManager =
            Mockito.mock(PackageManager::class.java)
        Mockito.`when`(
            packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
        ).thenReturn(packageInfo)
        Mockito.`when`(context.packageManager).thenReturn(packageManager)
        Assert.assertTrue(
            checkIfPackageIsInstalled(
                context,
                packageName
            )
        )
    }

    @Test
    fun testIsPhoneAvailable() {
        val context =
            Mockito.mock(Context::class.java)
        val telephonyManager =
            Mockito.mock(TelephonyManager::class.java)
        Mockito.`when`(telephonyManager.phoneType)
            .thenReturn(TelephonyManager.PHONE_TYPE_GSM)
        Mockito.`when`(context.getSystemService(Context.TELEPHONY_SERVICE))
            .thenReturn(telephonyManager)
        Assert.assertTrue(isPhoneAvailable(context))
    }

    @Test
    fun testIsPhoneNotAvailableNoTelephonyManager() {
        val context =
            Mockito.mock(Context::class.java)
        Assert.assertFalse(isPhoneAvailable(context))
    }

    @Test
    fun testIsPhoneNotAvailablePhoneNone() {
        val context =
            Mockito.mock(Context::class.java)
        val telephonyManager =
            Mockito.mock(TelephonyManager::class.java)
        Mockito.`when`(telephonyManager.phoneType)
            .thenReturn(TelephonyManager.PHONE_TYPE_NONE)
        Mockito.`when`(context.getSystemService(Context.TELEPHONY_SERVICE))
            .thenReturn(telephonyManager)
        Assert.assertFalse(isPhoneAvailable(context))
    }
}