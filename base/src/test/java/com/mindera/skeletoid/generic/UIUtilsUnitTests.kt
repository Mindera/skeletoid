package com.mindera.skeletoid.generic

import android.content.Context
import android.content.res.Resources
import com.mindera.skeletoid.generic.UIUtils.getStatusBarHeightPixels
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UIUtilsUnitTests {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var resources: Resources

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testsGetStatusBarHeightInvalid() {
        val invalidResourceId = -1

        Mockito.`when`(context.resources).thenReturn(resources)
        Mockito.`when`(
            resources.getIdentifier(
                ArgumentMatchers.any(String::class.java),
                ArgumentMatchers.any(String::class.java),
                ArgumentMatchers.any(String::class.java)
            )
        ).thenReturn(invalidResourceId)

        Assert.assertEquals(0, getStatusBarHeightPixels(context))
    }

    @Test
    fun testsGetStatusBarHeight() {
        val resourceId = 22
        val statusBarHeight = 180

        Mockito.`when`(resources.getDimensionPixelSize(resourceId)).thenReturn(statusBarHeight)
        Mockito.`when`(context.resources).thenReturn(resources)
        Mockito.`when`(
            resources.getIdentifier(
                ArgumentMatchers.any(String::class.java),
                ArgumentMatchers.any(String::class.java),
                ArgumentMatchers.any(String::class.java)
            )
        ).thenReturn(resourceId)

        Assert.assertEquals(statusBarHeight, getStatusBarHeightPixels(context))

    }
}