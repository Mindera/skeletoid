package com.mindera.skeletoid.logs.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.mindera.skeletoid.generic.AndroidUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

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
        ContentResolver resolver = mock(ContentResolver.class);

        when(file.getPath()).thenReturn("/com/mindera/skeletoid");
        when(activity.getFilesDir()).thenReturn(file);
        when(activity.getPackageName()).thenReturn("com.mindera.skeletoid");
        when(activity.getApplicationContext()).thenReturn(context);
        when(resolver.getType(any(Uri.class))).thenReturn("type");
        when(activity.getContentResolver()).thenReturn(resolver);
        when(context.getFilesDir()).thenReturn(file);

        Uri uri = mock(Uri.class);

        mockStatic(FileProvider.class);
        when(FileProvider.getUriForFile(any(Context.class), any(String.class), any(File.class))).thenReturn(uri);

        Intent mockIntent = mock(Intent.class);
        mockStatic(Intent.class);
        when(Intent.createChooser(any(Intent.class), anyString())).thenReturn(mockIntent);

        ArgumentCaptor<String> titleArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Intent> intentArgument = ArgumentCaptor.forClass(Intent.class);

        ShareLogFilesUtils.sendLogs(activity, "intentChooserTitle", "subjectTitle", "bodyText", new String[]{"user@user.com"}, file);

        verifyStatic(Intent.class, times(1));
        Intent.createChooser(intentArgument.capture(), titleArgument.capture());

        assertEquals("intentChooserTitle", titleArgument.getValue());

        verify(activity).startActivity(mockIntent);
    }
}
