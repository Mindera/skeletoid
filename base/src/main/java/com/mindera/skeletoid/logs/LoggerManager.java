package com.mindera.skeletoid.logs;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.mindera.skeletoid.generic.AndroidUtils;
import com.mindera.skeletoid.logs.appenders.ILogAppender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getLogString;
import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getObjectHash;
import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getTag;
import static com.mindera.skeletoid.threads.utils.ThreadUtils.getCurrentThreadName;

/**
 * LOG main class. It contains all the logic and feeds the appenders
 */
class LoggerManager implements ILoggerManager {

    private static final String LOG_TAG = "LoggerManager";
    /**
     * Log format
     */
    static final String LOG_FORMAT_4ARGS = "%s %s %s | %s";
    /**
     * Log format
     */
    private static final String LOG_FORMAT_3ARGS = "%s %s | %s";
    /**
     * Application TAG for logs
     */
    private final String PACKAGE_NAME;
    /**
     * Define if the method name invoking the log should be printed or not (via exception stack)
     */
    @VisibleForTesting
    public boolean mAddMethodName = false;

    /**
     * Define if the method name invoking the log should be printed or not (via exception stack)
     */
    private boolean mAddPackageName = false;

    /**
     * List of appenders (it can be improved to an ArrayMap if we want to add the support lib as dependency
     */
    private Map<String, ILogAppender> mLogAppenders = new HashMap<>();

    /**
     * The logger itself
     */

    LoggerManager(Context context) {
        PACKAGE_NAME = AndroidUtils.getApplicationPackage(context);
    }

    /**
     * This is to be used on ONLY ON UNIT TESTS.
     */
    LoggerManager(String packageName) {
        PACKAGE_NAME = packageName;
    }

    /**
     * Enables or disables logging to console/logcat.
     */
    public Set<String> addAppenders(Context context, List<ILogAppender> logAppenders) {
        if (logAppenders == null || logAppenders.isEmpty()) {
            return new HashSet<>();
        }

        final Set<String> appenderIds = new HashSet<>();

        for (ILogAppender logAppender : logAppenders) {

            final String loggerId = logAppender.getLoggerId();

            if (mLogAppenders.containsKey(loggerId)) {
                log(LOG_TAG, LOG.PRIORITY.ERROR, "Replacing Log Appender with ID: " + loggerId);
                ILogAppender oldLogAppender = mLogAppenders.remove(loggerId);
                oldLogAppender.disableAppender();
            }

            logAppender.enableAppender(context);
            appenderIds.add(loggerId);
            mLogAppenders.put(loggerId, logAppender);
        }
        return appenderIds;
    }


    /**
     * Enables or disables logging to console/logcat.
     */
    public void removeAppenders(Context context, Set<String> loggerIds) {
        if (loggerIds == null || mLogAppenders.isEmpty()) {
            return;
        }

        for (String logId : loggerIds) {
            final ILogAppender logAppender = mLogAppenders.remove(logId);
            if (logAppender != null) {
                logAppender.disableAppender();
            }
        }
    }

    @Override
    public void removeAllAppenders() {
        List<String> appendersKeys = new ArrayList<>(mLogAppenders.keySet());
        for (String analyticsId : appendersKeys) {
            final ILogAppender analyticsAppender = mLogAppenders.remove(analyticsId);
            if (analyticsAppender != null) {
                analyticsAppender.disableAppender();
            }
        }
    }

    public void setMethodNameVisible(boolean visibility) {
        mAddMethodName = visibility;
    }

    private void pushLogToAppenders(LOG.PRIORITY type, Throwable t, String log) {
        for (Map.Entry<String, ILogAppender> entry : mLogAppenders.entrySet()) {
            entry.getValue().log(type, t, log);
        }
    }

    @Override
    public void log(String tag, LOG.PRIORITY priority, String... text) {
        if (mLogAppenders.isEmpty()) {
            //nothing will be logged so no point in continuing
            return;
        }

        if (tag == null || priority == null || text == null) {
            LOG.e(LOG_TAG, "Something is wrong, logger caught null -> " + tag + " - " + priority + " - " + (text == null ? null : Arrays.toString(text)));

            return;
        }

        final String log = String.format(LOG_FORMAT_4ARGS, tag, getObjectHash(tag), getCurrentThreadName(), getLogString(text));

        pushLogToAppenders(priority, null, log);
    }

    @Override
    public void log(String tag, LOG.PRIORITY priority, Throwable t, String... text) {
        if (mLogAppenders.isEmpty()) {
            //nothing will be logged so no point in continuing
            return;
        }

        String logString = getLogString(text);

        if (tag == null || priority == null || text == null || t == null) {
            LOG.e(LOG_TAG, "Something is wrong, logger caught null -> " + logString);
            return;
        }

        final String log = String.format(LOG_FORMAT_4ARGS, tag, getObjectHash(tag), getCurrentThreadName(), logString);

        pushLogToAppenders(priority, t, log);
    }

    public void log(Class<?> clazz, LOG.PRIORITY type, String text) {
        if (mLogAppenders.isEmpty()) {
            //nothing will be logged so no point in continuing
            return;
        }

        final String logString = getLogString(text);

        if (clazz == null || type == null || text == null) {
            LOG.e(LOG_TAG, "Something is wrong, logger caught null -> " + logString);
            return;
        }

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(clazz, mAddPackageName, PACKAGE_NAME, mAddMethodName), getCurrentThreadName(), logString);

        pushLogToAppenders(type, null, log);
    }

    public void log(Class<?> clazz, LOG.PRIORITY type, String text, Throwable t) {
        if (mLogAppenders.isEmpty()) {
            //nothing will be logged so no point in continuing
            return;
        }

        final String logString = getLogString(text);

        if (clazz == null || type == null || text == null || t == null) {
            LOG.e(LOG_TAG, "Something is wrong, logger caught null -> " + logString);
            return;
        }

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(clazz, mAddPackageName, PACKAGE_NAME, mAddMethodName), getCurrentThreadName(), logString);
        pushLogToAppenders(type, t, log);
    }
}
