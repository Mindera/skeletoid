package com.mindera.skeletoid.logs.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.test.mock.MockContentResolver;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileProvider.class, Intent.class})
public class ShareLogFilesUtilsUnitTests {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new ShareLogFilesUtils();
    }

    @Test
    public void testGetFileLogPath() {
        Context context = mock(Context.class);

        File file = mock(File.class);
        when(context.getFilesDir()).thenReturn(file);
        when(file.getPath()).thenReturn("/com/mindera/skeletoid");

        assertEquals("/com/mindera/skeletoid", ShareLogFilesUtils.getFileLogPath(context));
    }

    @Test
    public void testSendLogsSingle() {
        Activity activity = mock(Activity.class);
        Context context = mock(Context.class);
        File file = mock(File.class);
        MockContentResolver resolver = new MockContentResolver();

        when(file.getPath()).thenReturn("/com/mindera/skeletoid");

        when(activity.getFilesDir()).thenReturn(file);
        when(activity.getPackageName()).thenReturn("com.mindera.skeletoid");
        when(activity.getApplicationContext()).thenReturn(context);
        when(activity.getContentResolver()).thenReturn(resolver);
        when(context.getFilesDir()).thenReturn(file);

        Uri uri = mock(Uri.class);

        mockStatic(FileProvider.class);
        when(FileProvider.getUriForFile(any(Activity.class), any(String.class), any(File.class))).thenReturn(uri);

        mockStatic(Intent.class);
        ArgumentCaptor<Intent> intentArgument = ArgumentCaptor.forClass(Intent.class);
        ArgumentCaptor<String> titleArgument = ArgumentCaptor.forClass(String.class);

        ShareLogFilesUtils.sendLogs(activity, "intentChooserTitle", "subjectTitle", "bodyText", new String[0], file);

        verifyStatic();
        Intent.createChooser(intentArgument.capture(), titleArgument.capture());

        assertNotNull(intentArgument.getValue());
//        assertEquals(Intent.ACTION_SEND, intentArgument.getValue().getAction());

        assertNotNull(titleArgument.getValue());
//        assertEquals("intentChooserTitle", titleArgument.getValue());
    }
}
