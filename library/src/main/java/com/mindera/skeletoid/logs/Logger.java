package com.mindera.skeletoid.logs;

import android.content.Context;
import android.util.Log;

import com.mindera.skeletoid.generic.AndroidUtils;

import java.io.File;
import java.util.List;

/**
 * Logger static class. It is used to abstract the Logger and have multiple possible implementations
 * It is used also to serve as static references for logging methods to be called.
 */
public class Logger {

    public enum PRIORITY {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }

    private static final String LOGGER = "Logger";

    private static volatile ILogger mInstance;

    //Default name, it will be replaced to packageName.log on getInstance method.
    //It presents no problem, because this will be used only after the logger is instantiated.
    private static String LOG_FILE_NAME = "debug.log";

    public static void init(Context context) {
        getInstance(context, null);
    }

    public static void init(Context context, List<ILogAppender> enabledLogAppenders) {
        getInstance(context, enabledLogAppenders);
    }

    public static ILogger getInstance(Context context, List<ILogAppender> enabledLogAppenders) {
        ILogger result = mInstance;
        if (result == null) {
            synchronized (Logger.class) {
                result = mInstance;
                if (result == null) {
                    final String packageName = AndroidUtils.getApplicationPackage(context);
                    LOG_FILE_NAME = packageName + ".log";

                    mInstance = new LoggerImpl(context,
                            enabledLogAppenders);
                }
            }
        }
        return mInstance;
    }


    public static ILogger getInstance() {
        if (mInstance == null) {
            Log.e(LOGGER, "You MUST init() the Logger. I will crash now...");
        }
        return mInstance;
    }


    public static void d(String tag, String text) {
        getInstance().log(tag, PRIORITY.DEBUG, text);
    }

    public static void e(String tag, String text) {
        getInstance().log(tag, PRIORITY.ERROR, text);
    }

    public static void v(String tag, String text) {
        getInstance().log(tag, PRIORITY.VERBOSE, text);
    }

    public static void i(String tag, String text) {
        getInstance().log(tag, PRIORITY.INFO, text);
    }

    public static void w(String tag, String text) {
        getInstance().log(tag, PRIORITY.WARN, text);
    }

    public static void d(String tag, String text, Throwable t) {
        getInstance().log(tag, PRIORITY.DEBUG, text, t);
    }

    public static void e(String tag, String text, Throwable t) {
        getInstance().log(tag, PRIORITY.ERROR, text, t);
    }

    public static void v(String tag, String text, Throwable t) {
        getInstance().log(tag, PRIORITY.VERBOSE, text, t);
    }

    public static void i(String tag, String text, Throwable t) {
        getInstance().log(tag, PRIORITY.INFO, text, t);
    }

    public static void w(String tag, String text, Throwable t) {
        getInstance().log(tag, PRIORITY.WARN, text, t);
    }

    public static String getFileLogPath(Context context) {
        return context.getFilesDir().getPath() + File.separator + LOG_FILE_NAME;
    }
}
