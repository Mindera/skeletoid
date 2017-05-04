package com.mindera.skeletoid.logs;

import android.content.Context;

import com.mindera.skeletoid.logs.appenders.ILogAppender;

import java.util.List;
import java.util.Set;

/**
 * LOG static class. It is used to abstract the LOG and have multiple possible implementations
 * It is used also to serve as static references for logging methods to be called.
 */
public class LOG {


    public enum PRIORITY {
        VERBOSE, DEBUG, INFO, ERROR, WARN, FATAL
    }

    private static final String LOGGER = "LOG";

    private static volatile ILoggerManager mInstance;

    /**
     * Init the logger. This method MUST be called before using LoggerManager
     *
     * @param context
     */
    public static void init(Context context) {
        getInstance(context);
    }


    /**
     * Init the logger. This method MUST be called before using LoggerManager
     *
     * @param context
     */
    public static void init(Context context, String packageName) {
        getInstance(context, packageName);
    }

    /**
     * Init the logger. This method MUST be called before using LoggerManager
     *
     * @param context      Context app
     * @param logAppenders The log appenders to be started
     */
    public static Set<String> init(Context context, List<ILogAppender> logAppenders) {
        return getInstance(context).addAppenders(context, logAppenders);
    }


    /**
     * Init the logger. This method MUST be called before using LoggerManager
     *
     * @param context      Context app
     * @param packageName  Packagename
     * @param logAppenders The log appenders to be started
     */
    public static Set<String> init(Context context, String packageName, List<ILogAppender> logAppenders) {
        return getInstance(packageName).addAppenders(context, logAppenders);
    }


    /**
     * Deinit the logger
     * This method can be called if the LOG is not needed any longer on the app.
     */
    public static void deinit(Context context) {
        if (mInstance != null) {
            getInstance().removeAllAppenders();
            mInstance = null;
        }
    }


    /**
     * Enable log appenders
     *
     * @param context      Context
     * @param logAppenders Log appenders to enable
     * @return Ids of the logs enabled by their order
     */
    public static Set<String> addAppenders(Context context, List<ILogAppender> logAppenders) {
        return getInstance().addAppenders(context, logAppenders);
    }

    /**
     * Disable log appenders
     *
     * @param context   Context
     * @param loggerIds Log ids of each of the loggers enabled by the order sent
     */
    public static void removeAppenders(Context context, Set<String> loggerIds) {
        getInstance().removeAppenders(context, loggerIds);
    }

    /**
     * Set method name visible in logs (careful this is a HEAVY operation)
     *
     * @param visibility true if enabled
     */
    public static void setMethodNameVisible(boolean visibility) {
        getInstance().setMethodNameVisible(visibility);
    }

    /**
     * Obtain a instance of the logger to guarantee it's unique
     *
     * @param context
     * @return
     */
    private static ILoggerManager getInstance(Context context, String packageName) {
        if (context == null && packageName == null) {
            throw new IllegalArgumentException("Context and packageName cannot both be null");
        }

        ILoggerManager result = mInstance;
        if (result == null) {
            synchronized (LOG.class) {
                result = mInstance;
                if (result == null) {
                    if (packageName == null) {
                        mInstance = new LoggerManager(context);
                    } else {
                        mInstance = new LoggerManager(packageName);
                    }
                }
            }
        }
        return mInstance;
    }

    /**
     * Obtain a instance of the logger to guarantee it's unique
     *
     * @param context
     * @return
     */
    private static ILoggerManager getInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        return getInstance(context, null);
    }

    /**
     * Obtain a instance of the logger to guarantee it's unique
     *
     * @param packageName
     * @return
     */
    private static ILoggerManager getInstance(String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("PackageName cannot be null");
        }
        return getInstance(null, packageName);
    }

    /**
     * Get LoggerManager instance.
     *
     * @return
     */
    private static ILoggerManager getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("You MUST init() the LOG.");
        }
        return mInstance;
    }

    /**
     * Log with a DEBUG level
     *
     * @param tag  Tag
     * @param text List of strings
     */
    public static void d(String tag, String... text) {
        getInstance().log(tag, PRIORITY.DEBUG, text);
    }

    /**
     * Log with a ERROR level
     *
     * @param tag  Tag
     * @param text List of strings
     */
    public static void e(String tag, String... text) {
        getInstance().log(tag, PRIORITY.ERROR, text);
    }

    /**
     * Log with a VERBOSE level
     *
     * @param tag  Tag
     * @param text List of strings
     */
    public static void v(String tag, String... text) {
        getInstance().log(tag, PRIORITY.VERBOSE, text);
    }

    /**
     * Log with a INFO level
     *
     * @param tag  Tag
     * @param text List of strings
     */
    public static void i(String tag, String... text) {
        getInstance().log(tag, PRIORITY.INFO, text);
    }

    /**
     * Log with a WARN level
     *
     * @param tag  Tag
     * @param text List of strings
     */
    public static void w(String tag, String... text) {
        getInstance().log(tag, PRIORITY.WARN, text);
    }

    /**
     * Log a What a Terrible Failure: Report an exception that should never happen.
     *
     * @param tag  Tag
     * @param text List of strings
     */
    public static void wtf(String tag, String... text) {
        getInstance().log(tag, PRIORITY.FATAL, text);
    }

    /**
     * Log with a DEBUG level
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    public static void d(String tag, Throwable t, String... text) {
        getInstance().log(tag, PRIORITY.DEBUG, t, text);
    }

    /**
     * Log with a ERROR level
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    public static void e(String tag, Throwable t, String... text) {
        getInstance().log(tag, PRIORITY.ERROR, t, text);
    }

    /**
     * Log with a VERBOSE level
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    public static void v(String tag, Throwable t, String... text) {
        getInstance().log(tag, PRIORITY.VERBOSE, t, text);
    }

    /**
     * Log with a INFO level
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    public static void i(String tag, Throwable t, String... text) {
        getInstance().log(tag, PRIORITY.INFO, t, text);
    }

    /**
     * Log with a WARN level
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    public static void w(String tag, Throwable t, String... text) {
        getInstance().log(tag, PRIORITY.WARN, t, text);
    }

    /**
     * Log a What a Terrible Failure: Report an exception that should never happen.
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    public static void wtf(String tag, Throwable t, String... text) {
        getInstance().log(tag, PRIORITY.FATAL, t, text);
    }

    /**
     * Return true if initialized
     *
     * @return true if initialized
     */
    public static boolean isInitialized() {
        return mInstance != null;
    }


}
