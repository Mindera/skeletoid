package com.mindera.skeletoid.analytics.appenders;


import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.mindera.skeletoid.logs.LOG;

import java.util.Map;

public class GAAppender implements IAnalyticsAppender {

    private final String LOG_TAG = "GAAppender";

    public static final String CATEGORY = "CATEGORY";
    public static final String ACTION = "ACTION";
    public static final String LABEL = "LABEL";
    public static final String VALUE = "VALUE";
    public static final String PRODUCT = "PRODUCT";
    public static final String PRODUCT_ACTION = "PRODUCT_ACTION";
    private final int mConfigurationFileId;
    private Tracker mTracker;

    public GAAppender(int configurationFileId) {
        mConfigurationFileId = configurationFileId;
    }

    @Override
    public void enableAppender(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        analytics.setLogger(new Logger() {
            @Override
            public void verbose(String s) {
                
            }

            @Override
            public void info(String s) {

            }

            @Override
            public void warn(String s) {

            }

            @Override
            public void error(String s) {

            }

            @Override
            public void error(Exception e) {

            }

            @Override
            public void setLogLevel(int i) {

            }

            @Override
            public int getLogLevel() {
                return 0;
            }
        });

        //This can be configured via XML or code. Since we need to maintain this code on the lib, it must
        // be via xml (dummy file) that will ne replaced by the apps real file
        // examples of both configurations are available: https://developers.google.com/analytics/devguides/collection/android/v4/advanced
        mTracker = analytics.newTracker(mConfigurationFileId);
    }

    @Override
    public void disableAppender() {
        mTracker = null;
    }

    @Override
    public void trackEvent(String screenName, Map<String, Object> analyticsPayload) {
        if (mTracker == null) {
            LOG.e(LOG_TAG, "trackEvent failed: mTracker is null");
            return;
        }
        mTracker.setScreenName(screenName);
        mTracker.send(parsePayload(analyticsPayload));
    }


    @Override
    public void trackPageHit(String screenName, Map<String, Object> map) {
        if (mTracker == null) {
            LOG.e(LOG_TAG, "trackPageHit failed: mTracker is null");
            return;
        }
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    private Map parsePayload(Map<String, Object> analyticsPayload) {
        HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder();

        //TODO Should this be protected, or crash in purpose to avoid mistakes on dev side?
        if (analyticsPayload.containsKey(CATEGORY)) {
            eventBuilder.setCategory((String) analyticsPayload.get(CATEGORY));
        }

        if (analyticsPayload.containsKey(ACTION)) {
            eventBuilder.setCategory((String) analyticsPayload.get(ACTION));
        }
        if (analyticsPayload.containsKey(LABEL)) {
            eventBuilder.setLabel((String) analyticsPayload.get(LABEL));
        }

        if (analyticsPayload.containsKey(VALUE)) {
            eventBuilder.setValue((Long) analyticsPayload.get(VALUE));
        }

        if (analyticsPayload.containsKey(PRODUCT)) {
            eventBuilder.addProduct((Product) analyticsPayload.get(PRODUCT));
        }

        if (analyticsPayload.containsKey(PRODUCT_ACTION)) {
            eventBuilder.setProductAction((ProductAction) analyticsPayload.get(PRODUCT_ACTION));
        }

        return eventBuilder.build();
    }

    @Override
    public String getAnalyticsId() {
        return "GoogleAnalytics";
    }
}

