package com.mindera.skeletoid.logs;

/**
 * Logger interface
 */
public interface ILogger {

    void log(String tag, Logger.PRIORITY priority, String text);

    void log(String tag, Logger.PRIORITY priority, String text, Throwable t);

}
