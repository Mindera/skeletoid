package com.mindera.skeletoid.generic

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Environment
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.annotation.VisibleForTesting
import androidx.core.content.pm.PackageInfoCompat

/**
 * Utility class for Android related info
 */
object AndroidUtils {

    const val APP_NAME = "App"

    /**
     * Cached app version name
     */
    private var appVersionName: String? = null

    /**
     * Cached app version code
     */
    @JvmField
    var appVersionCode = -1L

    /**
     * Cached app package
     */
    @JvmField
    var appPackage: String? = null

    /**
     * Get Device resolution
     *
     * @param context Application context
     * @return Device resolution
     */
    @JvmStatic
    fun getDeviceResolution(context: Context): String? {
        val wm =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
                ?: return null

        val metrics = DisplayMetrics()
        wm.defaultDisplay?.getMetrics(metrics)
        val density =
            context.resources.displayMetrics.densityDpi.toFloat()
        val str = StringBuilder()
        str.append(
            "Width: " + metrics.widthPixels + " px (" + metrics.widthPixels / density
                    + "dp)"
        )
        str.append(
            "| Height: " + metrics.heightPixels + " px (" + metrics.heightPixels / density
                    + "dp)"
        )
        return str.toString()
    }

    /**
     * Method to obtain the versionName on the Manifest in runtime
     *
     * @param context Application context
     * @return The string with the application versionName or null if an exception occurs
     */
    @JvmStatic
    fun getApplicationVersionName(context: Context): String? {
        if (appVersionName == null) {
            val versionName: String? = try {
                getPackageInfo(context).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                //This has Log instead of LOG on purpose to avoid infinite loops in error cases of logger startup
                Log.e(AndroidUtils::class.java.simpleName, "getApplicationVersionName", e)
                null
            }
            appVersionName = versionName
        }
        return appVersionName
    }

    @JvmStatic
    private fun getPackageInfo(context: Context, targetPackage: String? = null): PackageInfo {
        return context.packageManager.getPackageInfo(
            targetPackage ?: context.packageName,
            PackageManager.GET_META_DATA
        )
    }

    /**
     * Method to obtain the versionCode on the Manifest in runtime
     *
     * @param context Application context
     * @return The int with the application versionCode or -1 if an exception occurs
     */
    @JvmStatic
    fun getApplicationVersionCode(context: Context): Long {
        if (appVersionCode < 0) {
            try {
                val info = context.packageManager
                    .getPackageInfo(
                        context.packageName,
                        PackageManager.GET_META_DATA
                    )
                appVersionCode = PackageInfoCompat.getLongVersionCode(info)
            } catch (e: Exception) {
                //This has Log instead of LOG in purpose to avoid infinite loops on error cases of logger startup
                Log.e(
                    AndroidUtils::class.java.simpleName,
                    "getApplicationVersionCode",
                    e
                )
            }
        }
        return appVersionCode
    }

    /**
     * Method to obtain the application package on the Manifest in runtime
     *
     * @param context Application context
     * @return The application package.
     */
    @JvmStatic
    fun getApplicationPackage(context: Context): String {
        if (appPackage == null) {
            appPackage = context.applicationContext.packageName
            if (appPackage != null) {
                try {
                    val info = context.packageManager
                        .getPackageInfo(
                            context.packageName,
                            PackageManager.GET_META_DATA
                        )
                    appPackage = info.packageName
                } catch (e: Exception) {
                    //This has Log instead of LOG in purpose to avoid infinite loops on error cases of logger startup
                    Log.e(
                        AndroidUtils::class.java.simpleName,
                        "getApplicationPackage",
                        e
                    )
                }
            }
        }
        return appPackage ?: "unknown.package"
    }

    /**
     * Get app name define in AndroidManifest as label. It will throw
     * android.content.res.Resources$NotFoundException if hardcoded.
     *
     * @param context The app context
     * @return Label of the app, or App.cd
     */
    @JvmStatic
    fun getApplicationName(context: Context): String {
        var label = APP_NAME
        try {
            val stringId = context.applicationInfo.labelRes
            label = context.getString(stringId)
        } catch (e: Exception) {
            //This has Log instead of LOG on purpose to avoid infinite loops in error cases of logger startup
            Log.e(
                AndroidUtils::class.java.simpleName,
                "getApplicationName",
                e
            )
        }
        return label
    }

    /**
     * Check if a specific package is installed.
     *
     * @param context       App context
     * @param targetPackage The package to check
     * @return true if it is, false if not
     */
    @JvmStatic
    fun checkIfPackageIsInstalled(
        context: Context,
        targetPackage: String
    ): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    @JvmStatic
    fun isPhoneAvailable(context: Context): Boolean {
        val telephonyManager = context
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        return telephonyManager?.phoneType != null && telephonyManager.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    /**
     * Get File directory path
     *
     * @param context Context
     * @param path    path inside directory path
     * @return Path needed
     */
    @JvmStatic
    fun getFileDirPath(context: Context, path: String): String {
        return context.filesDir.path + path
    }

    /**
     * Get Cache directory path
     *
     * @param context              Context
     * @param separatorAndFilename filename
     * @return Path needed
     */
    @JvmStatic
    fun getCacheDirPath(
        context: Context,
        separatorAndFilename: String
    ): String {
        return context.cacheDir.path + separatorAndFilename
    }

    /**
     * Get External Storage directory path
     *
     * @param separatorAndFilename filename
     * @return Path needed
     */
    @JvmStatic
    fun getExternalPublicDirectory(separatorAndFilename: String): String {
        /**TODO This is deprecated and needs to be updated.
        If using Android Q+ you'll need to add to the manifest:

        <application android:requestLegacyExternalStorage="true" ... >

        This attribute is "false" by default on apps targeting
        Android 10 or higher.

        **/
        return Environment.getExternalStorageDirectory()
            .path + separatorAndFilename
    }

    @JvmStatic
    @VisibleForTesting
    fun deinit() {
        appVersionName = null
        appVersionCode = -1
        appPackage = null
    }

}