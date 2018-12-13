package com.mindera.skeletoid.routing;

import android.app.Activity;

/**
 * Closes the activity passed in the constructor
 */
public class CloseActivityCommand implements IRouteCommand {

    private final Activity mActivity;
    private int enterAnimation = 0;
    private int exitAnimation = 0;

    public CloseActivityCommand(Activity activity) {
        mActivity = activity;
    }

    public CloseActivityCommand(Activity activity, int enterAnimation, int exitAnimation) {
        this(activity);
        this.enterAnimation = enterAnimation;
        this.exitAnimation = exitAnimation;
    }

    @Override
    public void navigate() {
        mActivity.finish();
        if(enterAnimation != 0 || exitAnimation != 0) {
            mActivity.overridePendingTransition(enterAnimation, exitAnimation);
        }
    }
}
