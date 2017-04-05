package com.mindera.skeletoid.logs.appenders;

import android.content.Context;
import android.util.Log;

import com.mindera.skeletoid.generic.AndroidUtils;
import com.mindera.skeletoid.logs.LOG;
import com.mindera.skeletoid.threadpools.ThreadPoolUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import static com.mindera.skeletoid.logs.utils.LogAppenderUtils.getLogString;

/**
 * Log appender for file
 */
public class LogFileAppender implements ILogAppender {

    private static final String LOG_TAG = "LogFileAppender";

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    /**
     * Logcat logger tag
     */
    private final String TAG;
    /**
     * Log ID
     */
    private final String LOG_ID = "LogFileAppender";
    /**
     * Size of the log file in MBytes
     */
    private int mLogFileSize = 5;
    /**
     * Number of files to log (rolling appending)
     */
    private int mNumberOfLogFiles = 1;
    /**
     * Whether or not logging to file is possible (don't change value! This is controlled automatically)
     */
    private boolean mCanWriteToFile = false;
    /**
     * FileHandler logger: To write to file *
     */
    private FileHandler mFileHandler = null;
    /**
     * Thread pool to write files to disk. It will only span 1 thread
     */
    private java.util.concurrent.ThreadPoolExecutor mFileLoggingTP = null;

    //Default name, it will be replaced to packageName.log on constructor
    //It presents no problem, because this will be used only after the logger is instantiated.
    private String LOG_FILE_NAME = "debug.log";
    /**
     * Minimum log level for this appender
     */
    private int mMinLogLevel = LOG.PRIORITY.VERBOSE.ordinal();

    /**
     * Contructor
     *
     * @param tag      Log tag
     * @param fileName Log filename
     */
    public LogFileAppender(String tag, String fileName) {
        TAG = tag;
        LOG_FILE_NAME = fileName + ".log";
    }

    /**
     * Converts LOG level into FileHandler level
     *
     * @param type LOG type
     * @return FileHandler level
     */
    private static Level getFileHandlerLevel(LOG.PRIORITY type) {

        Level level;

        switch (type) {
            case VERBOSE:
                level = Level.ALL;
                break;
            case WARN:
                level = Level.WARNING;
                break;
            case ERROR:
            case FATAL:
                level = Level.SEVERE;
                break;
            case INFO:
                level = Level.INFO;
                break;
            case DEBUG:
            default:
                level = Level.ALL;
                break;
        }

        return level;
    }

    @Override
    public void enableAppender(Context context) {
        final int MBYTE_IN_BYTES = 1024 * 1024;

        try {
            mFileHandler = new FileHandler(getFileLogPath(context), mLogFileSize * MBYTE_IN_BYTES, mNumberOfLogFiles, true);
            mFileHandler.setFormatter(new SimpleFormatter());
            mFileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord logRecord) {
                    return logRecord.getMessage() + "\n";
                }
            });

            // Always set mCanWriteToFile to true before mLogConfiguration.logToFile() being true to avoid concurrency problems (or at
            // the same time)
            mCanWriteToFile = true;

            mFileLoggingTP = ThreadPoolUtils.getFixedThreadPool("LogToFileTP", 1);

        } catch (Throwable e) {
            Log.e(LOG_TAG, "Logging to file startup error", e);
            // Always set mLogConfiguration.logToFile() to false before mCanWriteToFile being false to avoid concurrency problems (or
            // at the same time)
            mCanWriteToFile = false;
        }
    }

    @Override
    public void disableAppender() {

        if (mFileHandler != null) {
            mFileHandler.close();
            mFileHandler = null;
        }

        // Always set mLogConfiguration.logToFile() to false before mCanWriteToFile being false to avoid concurrency problems (or at
        // the same time)
        mCanWriteToFile = false;

        if (mFileLoggingTP != null) {
            ThreadPoolUtils.shutdown(mFileLoggingTP);

            try {
                // wait until task terminate
                if (!mFileLoggingTP.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    ThreadPoolUtils.shutdownNow(mFileLoggingTP);
                }
            } catch (InterruptedException e) {
                ThreadPoolUtils.shutdownNow(mFileLoggingTP);
            }
        }
    }

    @Override
    public void log(final LOG.PRIORITY type, final Throwable t, final String... logs) {
        if (type.ordinal() > mMinLogLevel) {
            return;
        }

        if (mCanWriteToFile) {

            if (mFileLoggingTP != null
                    && !mFileLoggingTP.isTerminating()
                    && !mFileLoggingTP.isTerminated()
                    && !mFileLoggingTP.isShutdown()) {

                mFileLoggingTP.submit(new Runnable() {
                    @Override
                    public void run() {
                        if (mFileHandler != null) {
                            Level level = getFileHandlerLevel(type);

                            try {
                                final StringBuilder builder = new StringBuilder();
                                builder.append(dateFormatter.format(new Date()));
                                builder.append(": ");
                                builder.append(type.name().charAt(0));
                                builder.append("/");
                                builder.append(TAG);
                                builder.append("(").append(Thread.currentThread().getId()).append(")");
                                builder.append(": ");
                                builder.append(getLogString(logs));

                                final String logText = builder.toString();

                                LogRecord logRecord = new LogRecord(level, logText);
                                if (t != null) {
                                    logRecord.setThrown(t);
                                }

                                mFileHandler.publish(logRecord);

                            } catch (Exception e) {
                                Log.e(TAG, "Something is wrong", e);
                            }
                        }
                    }

                });

            } else {
                //Fail directly to Android Log to avoid Stackoverflow
                Log.e(LOG_TAG, "Error on submitToFileLoggingQueue: mFileLoggingTP is not available");
                //LOG.e(LOG_TAG, "Error on submitToFileLoggingQueue: mFileLoggingTP is not available");
            }

        } else {
            mCanWriteToFile = false;
            //Fail directly to Android Log to avoid Stackoverflow
            Log.e(LOG_TAG, "Error on submitToFileLoggingQueue: Can't write to disk");
            //LOG.e(LOG_TAG, "Error on submitToFileLoggingQueue: Can't write to disk");
        }
    }

    /**
     * Get list of file logs
     *
     * @param logsPath Path to the logs folder
     * @return List of logs files
     */
    public ArrayList<String> getListLogFiles(String logsPath) {
        ArrayList<String> logFiles = new ArrayList<>();

        File logsDirectory = new File(logsPath);
        if (logsDirectory.exists()) {
            String[] fileList = logsDirectory.list();
            if (fileList != null && fileList.length != 0) {
                for (String file : fileList) {
                    if (file.contains(".log")) {
                        logFiles.add(file);
                    }
                }
            }
        }
        return logFiles;
    }


    public void setLogFileSize(int LOG_FILE_SIZE) {
        this.mLogFileSize = LOG_FILE_SIZE;
    }

    public void setNumberOfLogFiles(int NUMBER_OF_LOG_FILES) {
        this.mNumberOfLogFiles = NUMBER_OF_LOG_FILES;
    }

    public String getFileLogPath(Context context){
        return AndroidUtils.getFileDirPath(context, File.separator + LOG_FILE_NAME);
    }

    @Override
    public int getMinLogLevel() {
        return mMinLogLevel;
    }

    @Override
    public void setMinLogLevel(LOG.PRIORITY minLogLevel) {
        mMinLogLevel = minLogLevel.ordinal();
    }

    @Override
    public String getLoggerId() {
        return LOG_ID;
    }

}
