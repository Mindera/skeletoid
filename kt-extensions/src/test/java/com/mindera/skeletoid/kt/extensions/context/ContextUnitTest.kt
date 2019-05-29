package com.mindera.skeletoid.kt.extensions.context

import android.content.Context
import android.content.res.Resources
import android.os.Build
import com.mindera.skeletoid.kt.extensions.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
@PrepareForTest(Build.VERSION::class)
class ContextUnitTest {

    @Before
    fun setUp() {
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 0)
    }

    @Test
    fun testGetColorCompatExtensionEqualOrGreaterThan23() {
        val colourId = 1
        val expectedColour = 2
        val context = mock<Context>()
        val resources = mock<Resources>()
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 23)
        Mockito.`when`(context.getColor(colourId)).thenReturn(expectedColour)
        Mockito.`when`(context.resources).thenReturn(resources)

        val actualColour = context.getColorCompat(colourId)
        assertEquals(expectedColour, actualColour)
    }

    @Test
    fun testGetColorCompatExtensionLowerThan23() {
        val colourId = 1
        val expectedColour = 2
        val context = mock<Context>()
        val resources = mock<Resources>()
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 20)
        Mockito.`when`(resources.getColor(colourId)).thenReturn(expectedColour)
        Mockito.`when`(context.resources).thenReturn(resources)

        val actualColour = context.getColorCompat(colourId)
        assertEquals(expectedColour, actualColour)
    }

    @Test
    fun testGetIntegerExtension() {
        val integerId = 1
        val expectedInteger = 2
        val context = mock<Context>()
        val resources = mock<Resources>()
        Mockito.`when`(resources.getInteger(integerId)).thenReturn(expectedInteger)
        Mockito.`when`(context.resources).thenReturn(resources)

        val actualInteger = context.getInteger(integerId)
        assertEquals(expectedInteger, actualInteger)
    }

    @Test
    fun testGetStringArrayExtension() {
        val stringArrayId = 1
        val expectedStringArray = arrayOf("1", "2", "3")
        val context = mock<Context>()
        val resources = mock<Resources>()
        Mockito.`when`(resources.getStringArray(stringArrayId)).thenReturn(expectedStringArray)
        Mockito.`when`(context.resources).thenReturn(resources)

        val actualStringArray = context.getStringArray(stringArrayId)
        assertTrue(expectedStringArray.contentEquals(actualStringArray))
    }
}
