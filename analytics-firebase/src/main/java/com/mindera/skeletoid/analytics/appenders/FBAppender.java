package com.mindera.skeletoid.analytics.appenders;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mindera.skeletoid.logs.LOG;

import java.util.Map;

public class FBAppender implements IAnalyticsAppender {

    private static final String LOG_TAG = "FBAppender";

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void enableAppender(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void disableAppender() {
        mFirebaseAnalytics = null;
    }

    @Override
    public void trackEvent(String eventName, Map<String, Object> map) {
        if (mFirebaseAnalytics == null) {
            LOG.e(LOG_TAG, "trackEvent failed: mFirebaseAnalytics is null");
            return;
        }

        mFirebaseAnalytics.logEvent(eventName, mapToBundle(map));
    }

    @Override
    public void trackEvent(String eventName, Bundle bundle) {
        if (mFirebaseAnalytics == null) {
            LOG.e(LOG_TAG, "trackEvent failed: mFirebaseAnalytics is null");
            return;
        }

        mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    @Override
    public void trackPageHit(Activity activity, String screenName, String screenClassOverload) {
        if (mFirebaseAnalytics == null) {
            LOG.e(LOG_TAG, "trackPageHit failed: mFirebaseAnalytics is null");
            return;
        }

        mFirebaseAnalytics.setCurrentScreen(activity, screenName, screenClassOverload);
    }

    @Override
    public void setUserID(String userID) {
        if (mFirebaseAnalytics == null) {
            LOG.e(LOG_TAG, "setUserID failed: mFirebaseAnalytics is null");
            return;
        }

        mFirebaseAnalytics.setUserId(userID);
    }

    @Override
    public void setUserProperty(String name, String value) {
        if (mFirebaseAnalytics == null) {
            LOG.e(LOG_TAG, "setUserProperty failed: mFirebaseAnalytics is null");
            return;
        }

        mFirebaseAnalytics.setUserProperty(name, value);
    }

    @Override
    public String getAnalyticsId() {
        return "FirebaseAnalytics";
    }

    private Bundle mapToBundle(Map<String, Object> map) {

        Bundle bundle = new Bundle();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue().toString());
        }

        return bundle;
    }
}
