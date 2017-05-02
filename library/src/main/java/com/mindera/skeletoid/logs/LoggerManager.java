package com.mindera.skeletoid.logs;

import android.content.Context;
import android.util.Log;

import com.mindera.skeletoid.analytics.appenders.IAnalyticsAppender;
import com.mindera.skeletoid.generic.AndroidUtils;
import com.mindera.skeletoid.logs.appenders.ILogAppender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getLogString;

/**
 * LOG main class. It contains all the logic and feeds the appenders
 */
class LoggerManager implements ILoggerManager {

    private static final String LOG_TAG = "LoggerManager";
    /**
     * Log format
     */
    private static final String LOG_FORMAT_4ARGS = "%s %s %s | %s";
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
    private boolean mAddMethodName = false;

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
     * Enables or disables logging to console/logcat.
     */
    public List<String> addAppenders(Context context, List<ILogAppender> logAppenders) {
        if (logAppenders == null || logAppenders.size() == 0) {
            return new ArrayList<>();
        }

        final List<String> appenderIds = new ArrayList<>();

        for (ILogAppender logAppender : logAppenders) {
            logAppender.enableAppender(context);

            final String loggerId = logAppender.getLoggerId();
            
            if (mLogAppenders.containsKey(loggerId)) {
                Log.e(LOG_TAG, "Appender ERROR: Adding appender with the same ID: " + loggerId);
                mLogAppenders.remove(loggerId).disableAppender();
            }
            appenderIds.add(loggerId);
            mLogAppenders.put(loggerId, logAppender);
        }
        return appenderIds;
    }


    /**
     * Enables or disables logging to console/logcat.
     */
    public void disableAppenders(Context context, List<String> loggerIds) {
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
    public void disableAllAppenders() {
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

    private void pushLogToAppenders(LOG.PRIORITY type, Throwable t, String... log) {
        for (Map.Entry<String, ILogAppender> entry : mLogAppenders.entrySet()) {
            entry.getValue().log(type, t, log);
        }
    }

    @Override
    public void log(String tag, LOG.PRIORITY type, String... text) {
        if (mLogAppenders.isEmpty()) {
            //nothing will be logged so no point in continuing
            return;
        }

        if (tag == null || type == null || text == null) {
            LOG.e(LOG_TAG, "Something is wrong, logger caught null -> " + tag + " - " + type + " - " + (text == null ? null : text.toString()));

            return;
        }

        final String log = String.format(LOG_FORMAT_4ARGS, tag, getObjectHash(tag), getCurrentThreadId(), getLogString(text));

        pushLogToAppenders(type, null, log);
    }

    @Override
    public void log(String tag, LOG.PRIORITY type, Throwable t, String... text) {
        if (mLogAppenders.isEmpty()) {
            //nothing will be logged so no point in continuing
            return;
        }

        String logString = getLogString(text);

        if (tag == null || type == null || text == null || t == null) {
            LOG.e(LOG_TAG, "Something is wrong, logger caught null -> " + logString);
            return;
        }

        final String log = String.format(LOG_FORMAT_4ARGS, tag, getObjectHash(tag), getCurrentThreadId(), logString);

        pushLogToAppenders(type, t, log);
    }

    public void log(Class<?> clazz, LOG.PRIORITY type, String... text) {
        if (mLogAppenders.isEmpty()) {
            //nothing will be logged so no point in continuing
            return;
        }

        final String logString = getLogString(text);

        if (clazz == null || type == null || text == null) {
            LOG.e(LOG_TAG, "Something is wrong, logger caught null -> " + logString);
            return;
        }

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(clazz), getCurrentThreadId(), logString);

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

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(clazz), getCurrentThreadId(), logString);
        pushLogToAppenders(type, t, log);
    }

    /**
     * Get class name with ClassName pre appended.
     *
     * @param clazz Class to get the tag from
     * @return Tag string
     */
    private String getTag(Class clazz) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (mAddPackageName) {
            stringBuilder.append(PACKAGE_NAME);
            stringBuilder.append("/");
        }

        stringBuilder.append(clazz.getCanonicalName());

        if (mAddMethodName) {
            stringBuilder.append("." + getMethodName(clazz));
        }

        return stringBuilder.toString();
    }

    /**
     * Get class method name. This will only work when proguard is not active
     *
     * @param clazz The class being logged
     */
    private String getMethodName(Class clazz) {
        int index = 0;
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            index++;
            if (ste.getClassName().equals(clazz.getName())) {
                break;
            }
        }

        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final String methodName;

        if (stackTrace.length > index) {
            methodName = stackTrace[index].getMethodName();
        } else {
            methodName = "UnknownMethod";
        }

        return methodName;
    }

    /**
     * Gets the hashcode of the object sent
     *
     * @return The hashcode of the object in a printable string
     */
    private String getObjectHash(Object object) {
        String hashCodeString;
        if (object == null) {
            hashCodeString = "[!!!NULL INSTANCE!!!] ";
        } else {
            hashCodeString = "[OID#" + object.hashCode() + "] ";
        }

        return hashCodeString;
    }

    /**
     * Gets the current thread ID
     *
     * @return The current thread id in a printable string
     */
    private String getCurrentThreadId() {
        return "[T# " + Thread.currentThread().getName() + "] ";
    }
}
