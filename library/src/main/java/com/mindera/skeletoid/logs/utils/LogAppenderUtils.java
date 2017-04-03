package com.mindera.skeletoid.logs.utils;

/**
 * Abstract LOG Appender
 */
public class LogAppenderUtils {
    /**
     * Convert log array to string
     *
     * @param logs List of strings
     * @return the list of strings concatenated or an empty string if null
     */
    public static String getLogString(String... logs) {
        if (logs == null) {
            return "";
        }

        StringBuilder strBuilder = new StringBuilder();
        for (String log : logs) {
            strBuilder.append(log);
        }
        return strBuilder.toString();
    }
}
