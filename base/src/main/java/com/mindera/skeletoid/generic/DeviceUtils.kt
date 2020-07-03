package com.mindera.skeletoid.generic

import android.os.Build

object DeviceUtils {

    /**
     * Get installed OS release name
     *
     * @return String with the installed OS release version or empty if not found
     */
    val osRelease: String = Build.VERSION.RELEASE ?: ""

    /**
     * Get the device's brand name
     *
     * @return String with the device's brand name or empty String if not found
     */
    val brand: String = Build.BRAND ?: ""

    /**
     * Get the device's manufacturer name
     *
     * @return String with the device's manufacturer name or empty String if not found
     */
    val manufacturer: String = Build.MANUFACTURER ?: ""

    /**
     * Get the device's model name
     *
     * @return String with the device's model name or empty String if not found
     */
    val model: String = Build.MODEL ?: ""

    /**
     * Get the device's manufacturer and model name
     *
     * @return String with the device's manufacturer and model name
     */

    val name: String = if (model.startsWith(manufacturer)) model else "$manufacturer $model"


    /**
     * Get installed OS SDK version
     *
     * @return String with the installed OS SDK version or empty String if not found
     */
    val sdkVersion: Int = Build.VERSION.SDK_INT

    /**
     * Get the name of the overall product
     *
     * @return String with the name of the overall product or empty String if not found
     */
    val product: String = Build.PRODUCT ?: ""

    /**
     * The name of the hardware (from the kernel command line or /proc)
     *
     * @return String with the name of the hardware or empty String if not found
     */
    val hardware: String = Build.HARDWARE ?: ""

    /**
     * The name of the industrial design
     *
     * @return String with the name of the industrial design or empty String if not found
     */
    val device: String = Build.DEVICE ?: ""

    /**
     * The OS version code name
     *
     * @return String with the name of the OS version code or empty String if not found
     */
    val osName: String = Build.VERSION_CODES::class.java.fields[sdkVersion].name

    val deviceSpecifications: String
        get() = "OS Release: $osRelease \n" +
                "Brand: $brand \n" +
                "Manufacturer: $manufacturer \n" +
                "Name: $name \n" +
                "SDK Version: $sdkVersion \n" +
                "Product: $product \n" +
                "Hardware: $hardware \n" +
                "Device: $device \n" +
                "OS Name: $osName"

    /**
     * Check if device running is an emulator ( https://stackoverflow.com/a/55355049/327011 )
     * @return true if emulator
     */
    val isEmulator: Boolean
        get() = (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator"))

}
