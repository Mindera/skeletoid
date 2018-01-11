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
     * Get appender minimum log level
     * @return
     */
    LOG.PRIORITY getMinLogLevel();

    /**
     * Set appender minimum log level
     * @param minLogLevel {@see com.mindera.skeletoid.logs.LOG.PRIORITY}
     */
    void setMinLogLevel(LOG.PRIORITY minLogLevel);

    /**
     * Get LOG id (it should be unique within LogAppenders)
     * The name LogcatAppender and LogFileAppender are already taken if the default log appenders
     * are used
     */
    String getLoggerId();

    /**
     * Define if the method name invoking the log should be printed or not (via exception stack)
     */
    boolean addMethodName();

    /**
     * Define if Class.number name should be printed or not (via exception stack)
     */
    boolean addCodePathName();

    /**
     * Define if the method name invoking the log should be printed or not (via exception stack)
     */
    boolean addPackageName();

}
