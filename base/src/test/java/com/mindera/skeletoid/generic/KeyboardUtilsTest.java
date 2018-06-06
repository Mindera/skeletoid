package com.mindera.skeletoid.generic;

import org.junit.Test;

import android.app.Activity;
import android.view.View;

import static org.mockito.Mockito.mock;

/**
 * Created by Pedro Vicente - pedro.vicente@mindera.com
 * File created on 14/08/2017.
 */

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
