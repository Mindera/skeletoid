package com.mindera.skeletoid.logs;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Log appender for Logcat
 */
public class LogcatAppender implements ILogAppender {

    public static final int LOGGER_ID = 0;
    /**
     * The maximum number of chars to log in a single line.
     */
    private final int MAX_LINE_LENGTH = 4000;
    /**
     * To check if logcat should show the complete lines or just the first 4000 chars.
     */
    private final boolean mSplitLinesAboveMaxLength = true;

    public final String TAG;

    public LogcatAppender(String tag) {
        TAG = tag;
    }

    @Override
    public void enableAppender(Context context) {

    }

    @Override
    public void disableAppender() {

    }

    @Override
    public void log(Logger.PRIORITY type, String log, Throwable t) {
        final ArrayList<String> logs = formatLog(log);

        for (String logText : logs) {

            switch (type) {
                case VERBOSE:
                    Log.v(TAG, logText, t);
                    break;
                case WARN:
                    Log.w(TAG, logText, t);
                    break;
                case ERROR:
                    Log.e(TAG, logText, t);
                    break;
                case DEBUG:
                    Log.d(TAG, logText, t);
                    break;
                case INFO:
                    Log.i(TAG, logText, t);
                    break;
                default:
                    Log.wtf(TAG, logText, t);
                    break;
            }
        }
    }

    /**
     * Formats the log to respect the value defined in MAX_LINE_LENGTH
     *
     * @param text The log text
     * @return A list of lines to log
     */
    private ArrayList<String> formatLog(String text) {

        final ArrayList<String> result = new ArrayList<String>();

        StringBuilder textToSplit = new StringBuilder();
        if (text != null) {
            textToSplit.append(text);
        }

        if (textToSplit.length() > MAX_LINE_LENGTH) {
            if (mSplitLinesAboveMaxLength) {

                int chunkCount = textToSplit.length() / MAX_LINE_LENGTH;
                for (int i = 0; i <= chunkCount; i++) {
                    int max = MAX_LINE_LENGTH * (i + 1);
                    if (max >= textToSplit.length()) {
                        result.add("[Chunk " + i + " of " + chunkCount + "]  " + textToSplit.substring(4000 * i));
                    } else {
                        result.add("[Chunk " + i + " of " + chunkCount + "]  " + textToSplit.substring(4000 * i, max));
                    }
                }
            }

        } else {
            result.add(textToSplit.toString());
        }
        return result;
    }
}