package com.mindera.skeletoid.generic

import android.content.Context
import android.content.res.Resources
import com.mindera.skeletoid.generic.UIUtils.getStatusBarHeighPixels
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class UIUtilsUnitTests {
    @Test
    fun testsGetStatusBarHeightInvalid() {
        val context =
            Mockito.mock(Context::class.java)
        val resources = Mockito.mock(
            Resources::class.java
        )
        val invalidResourceId = -1
        Mockito.`when`(context.resources).thenReturn(resources)
        Mockito.`when`(
            resources.getIdentifier(
                ArgumentMatchers.any(String::class.java),
                ArgumentMatchers.any(
                    String::class.java
                ),
                ArgumentMatchers.any(String::class.java)
            )
        ).thenReturn(invalidResourceId)
        Assert.assertEquals(0, getStatusBarHeighPixels(context))
    }

    @Test
    fun testsGetStatusBarHeight() {
        val context =
            Mockito.mock(Context::class.java)
        val resources = Mockito.mock(
            Resources::class.java
        )
        val resourceId = 22
        val statusBarHeight = 180
        Mockito.`when`(resources.getDimensionPixelSize(resourceId)).thenReturn(statusBarHeight)
        Mockito.`when`(context.resources).thenReturn(resources)
        Mockito.`when`(
            resources.getIdentifier(
                ArgumentMatchers.any(String::class.java),
                ArgumentMatchers.any(
                    String::class.java
                ),
                ArgumentMatchers.any(String::class.java)
            )
        ).thenReturn(resourceId)
        Assert.assertEquals(
            statusBarHeight,
            getStatusBarHeighPixels(context)
        )
    }
}