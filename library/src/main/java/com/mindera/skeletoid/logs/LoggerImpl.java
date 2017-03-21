package com.mindera.skeletoid.logs;

import android.content.Context;

import com.mindera.skeletoid.generic.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Logger main class. It contains all the logic and feeds the appenders
 */
public class LoggerImpl implements ILogger {

    private static final String LOG_TAG = "LoggerImpl";
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
    private final String TAG;
    /**
     * Define if the method name invoking the log should be printed or not (via exception stack)
     */
    private boolean mAddMethodName = true;

    private List<ILogAppender> mLogAppenders = new ArrayList<>();

    private boolean mIsForceDebug = false;

    /**
     * The logger itself
     */

    public LoggerImpl(Context context, List<ILogAppender> enabledLogAppenders) {
        TAG = AndroidUtils.getApplicationName(context);
        configureAppenders(context, enabledLogAppenders);
    }

    /**
     * Enables or disables logging to console/logcat.
     */
    public void configureAppenders(Context context, List<ILogAppender> enabledLogAppenders) {
        if (enabledLogAppenders == null) {
            enabledLogAppenders = new ArrayList<>();
        }

        //This could possibly be optimized by instead of removing all, just removing the ones we
        //need to disable
        if (!mLogAppenders.isEmpty()) {
            for (ILogAppender mLogAppender : mLogAppenders) {
                mLogAppender.disableAppender();
            }
        }

        mLogAppenders = enabledLogAppenders;

        for (ILogAppender mLogAppender : mLogAppenders) {
            mLogAppender.enableAppender(context);
        }
    }

    private void pushLogToAppenders(Logger.PRIORITY type, String log, Throwable t) {
        for (ILogAppender mLogAppender : mLogAppenders) {
            mLogAppender.log(type, log, t);
        }
    }

    @Override
    public void log(String tag, Logger.PRIORITY type, String text) {
        if (mLogAppenders.isEmpty()) {
            //nothing will be logged so no point in continuing
            return;
        }

        if (tag == null || type == null || text == null) {
            Logger.e(LOG_TAG, "Something is wrong, logger caught null -> " + text);

            return;
        }

        final String log = String.format(LOG_FORMAT_4ARGS, tag, getObjectHash(tag), getCurrentThreadId(), text);

        pushLogToAppenders(type, log, null);
    }

    @Override
    public void log(String tag, Logger.PRIORITY type, String text, Throwable t) {
        if (mLogAppenders.isEmpty()) {
            //nothing will be logged so no point in continuing
            return;
        }

        if (tag == null || type == null || text == null || t == null) {
            Logger.e(LOG_TAG, "Something is wrong, logger caught null -> " + text);
            return;
        }

        final String log = String.format(LOG_FORMAT_4ARGS, tag, getObjectHash(tag), getCurrentThreadId(), text);

        pushLogToAppenders(type, log, t);
    }

    public void log(Class<?> clazz, Logger.PRIORITY type, String text) {
        if (mLogAppenders.isEmpty()) {
            //nothing will be logged so no point in continuing
            return;
        }

        if (clazz == null || type == null || text == null) {
            Logger.e(LOG_TAG, "Something is wrong, logger caught null -> " + text);
            return;
        }

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(clazz), getCurrentThreadId(), text);

        pushLogToAppenders(type, log, null);
    }

    public void log(Class<?> clazz, Logger.PRIORITY type, String text, Throwable t) {
        if (mLogAppenders.isEmpty()) {
            //nothing will be logged so no point in continuing
            return;
        }

        if (clazz == null || type == null || text == null || t == null) {
            Logger.e(LOG_TAG, "Something is wrong, logger caught null -> " + text);
            return;
        }

        final String log = String.format(LOG_FORMAT_3ARGS, getTag(clazz), getCurrentThreadId(), text);
        pushLogToAppenders(type, log, t);
    }

    /**
     * Get class name with ClassName pre appended.
     *
     * @param clazz Class to get the tag from
     * @return Tag string
     */
    public String getTag(Class clazz) {
        final String methodName;
        if (mAddMethodName) {
            methodName = "." + getMethodName(clazz);
        } else {
            methodName = "";
        }
        return clazz.getCanonicalName() + methodName + " ";
    }

    /**
     * Get class method name. This will only work when proguard is not active
     *
     * @param clazz The class being logged
     */
    public String getMethodName(Class clazz) {
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
    public String getObjectHash(Object object) {
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
