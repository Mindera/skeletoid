package com.mindera.skeletoid.generic;

import org.junit.Test;

import android.content.Context;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AndroidUtilsUnitTests {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new AndroidUtils();
    }

    @Test
    public void testGetFileDirPath() {
        Context context = mock(Context.class);

        File file = mock(File.class);
        when(context.getFilesDir()).thenReturn(file);
        when(file.getPath()).thenReturn("com.mindera.skeletoid");

        assertEquals("com.mindera.skeletoid", AndroidUtils.getFileDirPath(context, ""));
    }

    @Test
    public void testGetFileDirPathWithPath() {
        Context context = mock(Context.class);

        File file = mock(File.class);
        when(context.getFilesDir()).thenReturn(file);
        when(file.getPath()).thenReturn("com.mindera.skeletoid");

        assertEquals("com.mindera.skeletoid/dir", AndroidUtils.getFileDirPath(context, "/dir"));
    }
}
