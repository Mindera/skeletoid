package com.mindera.skeletoid.logs.appenders;

import android.content.Context;
import android.util.Log;

import com.mindera.skeletoid.logs.LOG;

import java.util.ArrayList;

import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getLogString;

/**
 * Log appender for Logcat
 */
public class LogcatAppender implements ILogAppender {

    /**
     * The maximum number of chars to log in a single line.
     */
    private final int MAX_LINE_LENGTH = 4000;
    /**
     * To check if logcat should show the complete lines or just the first 4000 chars.
     */
    private final boolean mSplitLinesAboveMaxLength = true;

    /**
     * Logcat logger tag
     */
    public final String TAG;
    /**
     * LogAppender ID
     */
    private final String LOG_ID = "LogcatAppender";

    /**
     * Contructor
     *
     * @param tag Log tag
     */
    public LogcatAppender(String tag) {
        TAG = tag;
    }

    @Override
    public void enableAppender(Context context) {
        //Nothing needed here
    }

    @Override
    public void disableAppender() {
        //Nothing needed here
    }

    @Override
    public void log(LOG.PRIORITY type, Throwable t, String... log) {
        final String logString = getLogString(log);
        final ArrayList<String> logs = formatLog(logString);

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


    @Override
    public String getLoggerId() {
        return LOG_ID;
    }
}