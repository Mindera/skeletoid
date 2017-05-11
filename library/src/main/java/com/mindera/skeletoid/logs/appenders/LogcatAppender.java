package com.mindera.skeletoid.logs.appenders;

import android.content.Context;
import android.util.Log;

import com.mindera.skeletoid.logs.LOG;

import java.util.ArrayList;
import java.util.List;

import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getLogString;

/**
 * Log appender for Logcat
 */
public class LogcatAppender implements ILogAppender {

    /**
     * The maximum number of chars to log in a single line.
     */
    private int MAX_LINE_LENGTH = 4000;
    /**
     * To check if logcat should show the complete lines or just the first 4000 chars.
     */
    private boolean mSplitLinesAboveMaxLength = true;

    /**
     * Logcat logger tag
     */
    private final String TAG;
    /**
     * LogAppender ID
     */
    private final String LOG_ID = "LogcatAppender";
    /**
     * Minimum log level for this appender
     */
    private LOG.PRIORITY mMinLogLevel = LOG.PRIORITY.VERBOSE;

    /**
     * Contructor
     *
     * @param tag Log tag
     */
    public LogcatAppender(String tag) {
        if (tag == null) {
            throw new IllegalArgumentException("TAG cannot be null");
        }
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
        if (type.ordinal() > mMinLogLevel.ordinal()) {
            return;
        }
        final String logString = getLogString(log);
        final List<String> logs = formatLog(logString);

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
                case FATAL:
                    Log.wtf(TAG, logText, t);
                    break;
                default:
                    Log.d(TAG, logText, t);
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
    protected List<String> formatLog(String text) {
        final List<String> result = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return result;
        }

        if (text.length() > MAX_LINE_LENGTH) {
            if (mSplitLinesAboveMaxLength) {

                int chunkCount = (int) Math.ceil(1f * text.length() / MAX_LINE_LENGTH );
                for (int i = 1; i <= chunkCount; i++) {
                    int max = MAX_LINE_LENGTH * i;
                    if (max < text.length()) {
                        result.add("[Chunk " + i + " of " + chunkCount + "] " + text.substring(MAX_LINE_LENGTH * (i - 1), max));
                    } else {
                        result.add("[Chunk " + i + " of " + chunkCount + "] " + text.substring(MAX_LINE_LENGTH * (i - 1)));

                    }
                }
            }

        } else {
            result.add(text);
        }
        return result;
    }


    public void setSplitLinesAboveMaxLength(boolean splitLinesAboveMaxLength) {
        this.mSplitLinesAboveMaxLength = splitLinesAboveMaxLength;
    }

    public boolean isSplitLinesAboveMaxLength() {
        return mSplitLinesAboveMaxLength;
    }

    @Override
    public LOG.PRIORITY getMinLogLevel() {
        return mMinLogLevel;
    }

    protected void setMaxLineLength(int maxLineLength) {
        this.MAX_LINE_LENGTH = maxLineLength;
    }

    protected int getMaxLineLength() {
        return MAX_LINE_LENGTH;
    }

    public void setMinLogLevel(LOG.PRIORITY minLogLevel) {
        this.mMinLogLevel = minLogLevel;
    }

    @Override
    public String getLoggerId() {
        return LOG_ID;
    }


}