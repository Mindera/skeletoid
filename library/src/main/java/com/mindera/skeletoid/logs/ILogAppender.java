package com.mindera.skeletoid.logs;

import android.content.Context;

/**
 * Interface for all Log appenders
 */
public interface ILogAppender {

    /**
     * Enable the logger
     *
     * @param context Application context
     */
    void enableAppender(Context context);

    /**
     * Disable the logger.
     * If its something like a file logger it should close all things related to it to avoid memory leaks
     */
    void disableAppender();

    /**
     * Write a log
     *
     * @param type Type of log
     * @param log  Log
     * @param t    Throwable (can be null)
     */
    void log(Logger.PRIORITY type, String log, Throwable t);
}
