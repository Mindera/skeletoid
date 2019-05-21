package com.mindera.skeletoid.generic

import android.os.Build
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
@PrepareForTest(Build.VERSION::class, Build::class)
class DeviceUtilsUnitTests {

    companion object {

        private const val DEVICE_SPECIFICATIONS = "OS Release: release \n" +
                "Brand: Pixel \n" +
                "Manufacturer: Google \n" +
                "Name: Google Pixel 3 \n" +
                "SDK Version: 28 \n" +
                "Product: ? \n" +
                "Hardware: FRF50 \n" +
                "Device: GT-I9000 \n" +
                "OS Name: P"
    }

    @After
    fun cleanUp() {
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 0)
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK", "")
        Whitebox.setInternalState(Build::class.java, "HARDWARE", "")
        Whitebox.setInternalState(Build::class.java, "PRODUCT", "")
        Whitebox.setInternalState(Build::class.java, "DEVICE", "")
        Whitebox.setInternalState(Build::class.java, "BRAND", "")
        Whitebox.setInternalState(Build::class.java, "MANUFACTURER", "")
        Whitebox.setInternalState(Build::class.java, "MODEL", "")
        Whitebox.setInternalState(Build.VERSION::class.java, "RELEASE", "")
    }

    @Test
    fun testOSRelease() {
        Whitebox.setInternalState(Build.VERSION::class.java, "RELEASE", "release")

        assertEquals("release", DeviceUtils.osRelease)
    }

    @Test
    fun testBrand() {
        Whitebox.setInternalState(Build::class.java, "BRAND", "Pixel")

        assertEquals("Pixel", DeviceUtils.brand)
    }

    @Test
    fun testModel() {
        Whitebox.setInternalState(Build::class.java, "MODEL", "Pixel 3")

        assertEquals("Pixel 3", DeviceUtils.model)
    }

    @Test
    fun testManufacturer() {
        Whitebox.setInternalState(Build::class.java, "MANUFACTURER", "Google")

        assertEquals("Google", DeviceUtils.manufacturer)
    }

    @Test
    fun testNameContainingManufacturer() {
        Whitebox.setInternalState(Build::class.java, "MODEL", "Google Pixel 3")

        assertEquals("Google Pixel 3", DeviceUtils.name)
    }

    @Test
    fun testNameNotContainingManufacturer() {
        Whitebox.setInternalState(Build::class.java, "MANUFACTURER", "Google")
        Whitebox.setInternalState(Build::class.java, "MODEL", "Pixel 3")

        assertEquals("Google Pixel 3", DeviceUtils.name)
    }

    @Test
    fun testSdkVersion() {
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 28)

        assertEquals(28, DeviceUtils.sdkVersion)
    }

    @Test
    fun testProduct() {
        Whitebox.setInternalState(Build::class.java, "PRODUCT", "?")

        assertEquals("?", DeviceUtils.product)
    }

    @Test
    fun testHardware() {
        Whitebox.setInternalState(Build::class.java, "HARDWARE", "FRF50")

        assertEquals("FRF50", DeviceUtils.hardware)
    }

    @Test
    fun testDevice() {
        Whitebox.setInternalState(Build::class.java, "DEVICE", "GT-I9000")

        assertEquals("GT-I9000", DeviceUtils.device)
    }

    @Test
    fun testOSName() {
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 28)

        assertEquals("P", DeviceUtils.osName)
    }

    @Test
    fun testDeviceSpecifications() {
        Whitebox.setInternalState(Build::class.java, "DEVICE", "GT-I9000")
        Whitebox.setInternalState(Build::class.java, "HARDWARE", "FRF50")
        Whitebox.setInternalState(Build::class.java, "PRODUCT", "?")
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 28)
        Whitebox.setInternalState(Build::class.java, "MODEL", "Google Pixel 3")
        Whitebox.setInternalState(Build::class.java, "MANUFACTURER", "Google")
        Whitebox.setInternalState(Build::class.java, "MODEL", "Pixel 3")
        Whitebox.setInternalState(Build.VERSION::class.java, "RELEASE", "release")
        Whitebox.setInternalState(Build::class.java, "BRAND", "Pixel")

        assertEquals(DEVICE_SPECIFICATIONS, DeviceUtils.deviceSpecifications)
    }
}