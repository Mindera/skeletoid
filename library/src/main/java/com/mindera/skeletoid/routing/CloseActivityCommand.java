package com.mindera.skeletoid.routing;

import android.app.Activity;

/**
 * Closes the activity passed in the constructor
 */
public class CloseActivityCommand implements IRouteCommand {

    private final Activity mActivity;

    public CloseActivityCommand(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void navigate() {
        mActivity.finish();
    }
}
