package com.mindera.skeletoid.generic

import android.os.Build

object DeviceUtils {

    /**
     * Get installed OS release name
     *
     * @return String with the installed OS release version or empty if not found
     */
    val osRelease: String
        get() = Build.VERSION.RELEASE ?: ""

    /**
     * Get the device's brand name
     *
     * @return String with the device's brand name or empty String if not found
     */
    val brand: String
        get() = Build.BRAND ?: ""

    /**
     * Get the device's manufacturer name
     *
     * @return String with the device's manufacturer name or empty String if not found
     */
    val manufacturer: String
        get() = Build.MANUFACTURER ?: ""

    /**
     * Get the device's model name
     *
     * @return String with the device's model name or empty String if not found
     */
    val model: String
        get() = Build.MODEL ?: ""

    /**
     * Get the device's manufacturer and model name
     *
     * @return String with the device's manufacturer and model name or empty String if not found
     */
    val name: String
        get() = if (model.startsWith(manufacturer)) model else "$manufacturer $model"

    /**
     * Get installed OS SDK version
     *
     * @return String with the installed OS SDK version or empty String if not found
     */
    val sdkVersion: Int
        get() = Build.VERSION.SDK_INT

    /**
     * Get the name of the overall product
     *
     * @return String with the name of the overall product or empty String if not found
     */
    val product: String
        get() = Build.PRODUCT ?: ""

    /**
     * The name of the hardware (from the kernel command line or /proc)
     *
     * @return String with the name of the hardware or empty String if not found
     */
    val hardware: String
        get() = Build.HARDWARE ?: ""

    /**
     * The name of the industrial design
     *
     * @return String with the name of the industrial design or empty String if not found
     */
    val device: String
        get() = Build.DEVICE ?: ""

    /**
     * The OS version code name
     *
     * @return String with the name of the OS version code or empty String if not found
     */
    val osName: String
        get() = Build.VERSION_CODES::class.java.fields[sdkVersion].name ?: ""

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
}
