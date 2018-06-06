package com.mindera.skeletoid.generic;

import org.junit.Test;

import android.content.Context;
import android.content.res.Resources;

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

        when(context.getResources()).thenReturn(resources);

        when(resources.getIdentifier(any(String.class), any(String.class), any(String.class)))
                .thenReturn(-1);

        assertEquals(0, UIUtils.getStatusBarHeighPixels(context));
    }
}
