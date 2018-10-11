package com.mindera.skeletoid.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.app.Activity;
import android.view.View;

import com.mindera.skeletoid.BuildConfig;

import static org.mockito.Mockito.mock;

/**
 * Created by Pedro Vicente - pedro.vicente@mindera.com
 * File created on 14/08/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class KeyboardUtilsTest {

    @Test(expected = NullPointerException.class)
    public void testHideKeyboard() {
        Activity activity = mock(Activity.class);
        KeyboardUtils.hideKeyboard(activity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHideKeyboardWithoutContext() {
        KeyboardUtils.hideKeyboard(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHideKeyboardFromWithoutContext() {
        View view = mock(View.class);
        KeyboardUtils.hideKeyboardFrom(null, view);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHideKeyboardFromWithoutView() {
        KeyboardUtils.hideKeyboardFrom(null, null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testHideKeyboardFromWithoutContextOrView() {
        KeyboardUtils.hideKeyboardFrom(null, null);
    }
}
