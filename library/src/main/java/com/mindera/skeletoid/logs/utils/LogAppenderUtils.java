package com.mindera.skeletoid.logs.utils;

/**
 * Abstract LOG Appender
 */
public class LogAppenderUtils {
    /**
     * Convert log array to string
     *
     * @param text List of strings
     * @return the list of strings concatenated or an empty string if null
     */
    public static String getLogString(String... text) {
        if (text == null) {
            return "";
        }

        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < text.length; i++) {
            strBuilder.append(text[i]);
        }
        return strBuilder.toString();
    }
}
