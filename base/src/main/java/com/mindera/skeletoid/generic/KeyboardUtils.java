package com.mindera.skeletoid.generic;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    /**
     * Hide keyboard. This can only be called from the Activity.
     * Using for example getActivity() from a Fragment WON'T work.
     */
    public static void hideKeyboard(Activity activity) {
        if(activity == null){
            throw new IllegalArgumentException("Activity cannot be null");
        }

        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Hide keyboard. This can be called from everywhere (Fragments for example)
     *
     * @param context App context
     * @param view    A view
     */
    public static void hideKeyboardFrom(Context context, View view) {
        if(context == null){
            throw new IllegalArgumentException("Context cannot be null");
        }

        if(view == null){
            throw new IllegalArgumentException("View cannot be null");
        }

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    
    /**
     * Show keyboard. This can be called from everywhere (Fragments for example)
     *
     * @param context App context
     */
    public static void showKeyboard(Context context) {
        if(context == null){
            throw new IllegalArgumentException("Context cannot be null");
        }

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
