package com.mindera.skeletoid.logs;

import android.content.Context;
import android.util.Log;

import com.mindera.skeletoid.logs.appenders.ILogAppender;

import java.util.List;

/**
 * LOG static class. It is used to abstract the LOG and have multiple possible implementations
 * It is used also to serve as static references for logging methods to be called.
 */
public class LOG {


    public enum PRIORITY {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }

    private static final String LOGGER = "LOG";

    private static volatile ILogger mInstance;

    /**
     * Init the logger. This method MUST be called before using Logger
     *
     * @param context
     */
    public static void init(Context context) {
        getInstance(context);
    }

    /**
     * Init the logger. This method MUST be called before using Logger
     *
     * @param context      Context app
     * @param logAppenders The log appenders to be started
     */
    public static List<String> init(Context context, List<ILogAppender> logAppenders) {
        return getInstance(context).addAppenders(context, logAppenders);
    }

    /**
     * Obtain a instance of the logger to guarantee it's unique
     *
     * @param context
     * @return
     */
    public static ILogger getInstance(Context context) {
        ILogger result = mInstance;
        if (result == null) {
            synchronized (LOG.class) {
                result = mInstance;
                if (result == null) {
                    mInstance = new Logger(context);
                }
            }
        }
        return mInstance;
    }


    public static ILogger getInstance() {
        if (mInstance == null) {
            Log.e(LOGGER, "You MUST init() the LOG. I will crash now...");
        }
        return mInstance;
    }


    public static void d(String tag, String... text) {
        getInstance().log(tag, PRIORITY.DEBUG, text);
    }

    public static void e(String tag, String... text) {
        getInstance().log(tag, PRIORITY.ERROR, text);
    }

    public static void v(String tag, String... text) {
        getInstance().log(tag, PRIORITY.VERBOSE, text);
    }

    public static void i(String tag, String... text) {
        getInstance().log(tag, PRIORITY.INFO, text);
    }

    public static void w(String tag, String... text) {
        getInstance().log(tag, PRIORITY.WARN, text);
    }

    public static void d(String tag, Throwable t, String... text) {
        getInstance().log(tag, PRIORITY.DEBUG, t, text);
    }

    public static void e(String tag, Throwable t, String... text) {
        getInstance().log(tag, PRIORITY.ERROR, t, text);
    }

    public static void v(String tag, Throwable t, String... text) {
        getInstance().log(tag, PRIORITY.VERBOSE, t, text);
    }

    public static void i(String tag, Throwable t, String... text) {
        getInstance().log(tag, PRIORITY.INFO, t, text);
    }

    public static void w(String tag, Throwable t, String... text) {
        getInstance().log(tag, PRIORITY.WARN, t, text);
    }

    /**
     * Enable log appenders
     * @param context Context
     * @param logAppenders Log appenders to enable
     * @return Ids of the logs enabled by their order
     */
    public static List<String> addAppenders(Context context, List<ILogAppender> logAppenders) {
        return getInstance().addAppenders(context, logAppenders);
    }

    /**
     * Disable log appenders
     * @param context Context
     * @param loggerIds Log ids of each of the loggers enabled by the order sent
     */
    public static void disableAppenders(Context context, List<String> loggerIds) {
        getInstance().disableAppenders(context, loggerIds);
    }

    /**
     * Set method name visible in logs (careful this is a HEAVY operation)
     *
     * @param visibility true if enabled
     */
    public static void setMethodNameVisible(boolean visibility) {
        getInstance().setMethodNameVisible(visibility);
    }
}
