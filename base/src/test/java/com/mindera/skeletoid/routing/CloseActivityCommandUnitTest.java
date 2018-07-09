package com.mindera.skeletoid.routing;

import org.junit.Test;

import android.app.Activity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CloseActivityCommandUnitTest {

    @Test
    public void navigate() {

        Activity activity = mock(Activity.class);
        CloseActivityCommand command = new CloseActivityCommand(activity);
        command.navigate();

        verify(activity, times(1)).finish();
    }
}
