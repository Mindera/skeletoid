package com.mindera.skeletoid.logs;

import android.content.Context;

import com.mindera.skeletoid.logs.appenders.ILogAppender;

import java.util.List;
import java.util.Set;

/**
 * LOG interface
 */
public interface ILoggerManager {

    /**
     * Log to all log appenders
     *
     * @param tag      Log tag
     * @param priority Log priority
     * @param text     Log text
     */
    void log(String tag, LOG.PRIORITY priority, String... text);

    /**
     * Log to all log appenders
     *
     * @param tag      Log tag
     * @param priority Log priority
     * @param t        Trowable
     * @param text     Log text
     */
    void log(String tag, LOG.PRIORITY priority, Throwable t, String... text);

    /**
     * Set method name visible in logs (careful this is a HEAVY operation)
     *
     * @param visibility true if enabled
     */
    void setMethodNameVisible(boolean visibility);

    /**
     * Enable log appenders
     * @param context Context
     * @param logAppenders Log appenders to enable
     * @return Ids of the logs enabled by their order
     */
    Set<String> addAppenders(Context context, List<ILogAppender> logAppenders);


    /**
     * Disable log appenders
     * @param context Context
     * @param loggerIds Log ids of each of the loggers enabled by the order sent
     */
    void removeAppenders(Context context, Set<String> loggerIds);


    /**
     * Disable all log appenders
     */
    void removeAllAppenders();

}
