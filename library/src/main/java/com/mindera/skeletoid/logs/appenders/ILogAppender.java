package com.mindera.skeletoid.logs.appenders;

import android.content.Context;

import com.mindera.skeletoid.logs.LOG;

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
     * @param t    Throwable (can be null)
     * @param log  Log
     */
    void log(LOG.PRIORITY type, Throwable t, String... log);

    /**
     * Get LOG id (it should be unique)
     */
    String getLoggerId();
}
