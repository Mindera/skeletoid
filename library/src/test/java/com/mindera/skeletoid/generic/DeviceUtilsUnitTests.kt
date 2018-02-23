package com.mindera.skeletoid.generic

import junit.framework.Assert.assertEquals
import org.junit.Test

class DeviceUtilsUnitTests {

    companion object {

        private const val DEVICE_SPECIFICATIONS = "OS Release:  \n" +
                "Brand:  \n" +
                "Manufacturer:  \n" +
                "Name:  \n" +
                "SDK Version: 0 \n" +
                "Product:  \n" +
                "Hardware:  \n" +
                "Device:  \n" +
                "OS Name: BASE"
    }

    @Test
    fun testOSRelease() {
        assertEquals("", DeviceUtils.osRelease)
    }

    @Test
    fun testBrand() {
        assertEquals("", DeviceUtils.brand)
    }

    @Test
    fun testModel() {
        assertEquals("", DeviceUtils.model)
    }

    @Test
    fun testManufacturer() {
        assertEquals("", DeviceUtils.manufacturer)
    }

    @Test
    fun testName() {
        assertEquals("", DeviceUtils.name)
    }

    @Test
    fun testSdkVersion() {
        assertEquals(0, DeviceUtils.sdkVersion)
    }

    @Test
    fun testProduct() {
        assertEquals("", DeviceUtils.product)
    }

    @Test
    fun testHardware() {
        assertEquals("", DeviceUtils.hardware)
    }

    @Test
    fun testDevice() {
        assertEquals("", DeviceUtils.device)
    }

    @Test
    fun testOSName() {
        assertEquals("BASE", DeviceUtils.osName)
    }

    @Test
    fun testDeviceSpecifications() {
        assertEquals(DEVICE_SPECIFICATIONS, DeviceUtils.deviceSpecifications)
    }
}