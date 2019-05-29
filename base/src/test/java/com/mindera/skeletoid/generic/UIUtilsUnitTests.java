package com.mindera.skeletoid.generic;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UIUtilsUnitTests {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new UIUtils();
    }

    @Test
    public void testsGetStatusBarHeightInvalid() {
        Context context = mock(Context.class);
        Resources resources = mock(Resources.class);
        int invalidResourceId = -1;
        when(context.getResources()).thenReturn(resources);
        when(resources.getIdentifier(any(String.class), any(String.class), any(String.class))).thenReturn(invalidResourceId);

        assertEquals(0, UIUtils.getStatusBarHeighPixels(context));
    }

    @Test
    public void testsGetStatusBarHeight() {
        Context context = mock(Context.class);
        Resources resources = mock(Resources.class);
        int resourceId = 22;
        int statusBarHeight = 180;
        when(resources.getDimensionPixelSize(resourceId)).thenReturn(statusBarHeight);
        when(context.getResources()).thenReturn(resources);
        when(resources.getIdentifier(any(String.class), any(String.class), any(String.class))).thenReturn(resourceId);

        assertEquals(statusBarHeight, UIUtils.getStatusBarHeighPixels(context));
    }
}
