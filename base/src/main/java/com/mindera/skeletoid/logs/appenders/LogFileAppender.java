package com.mindera.skeletoid.logs.appenders;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.mindera.skeletoid.generic.AndroidUtils;
import com.mindera.skeletoid.logs.LOG;
import com.mindera.skeletoid.threads.threadpools.ThreadPoolUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

    private java.text.DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
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
     * Whether or not logging to file is possible (don't change value! This is controlled
     * automatically)
     */
    @VisibleForTesting
    public volatile boolean mCanWriteToFile = false;
    /**
     * Whether or not logging to file is possible (don't change value! This is controlled
     * automatically)
     */
    private boolean mWriteToExternal = false;
    /**
     * FileHandler logger: To write to file *
     */
    @VisibleForTesting
    public FileHandler mFileHandler = null;
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
    private LOG.PRIORITY mMinLogLevel = LOG.PRIORITY.VERBOSE;


    /**
     * Contructor
     *
     * @param tag      Log tag
     * @param fileName Log filename
     */
    public LogFileAppender(String tag, String fileName) {
        this(tag, fileName, false);
    }

    /**
     * Contructor
     *
     * @param tag      Log tag
     * @param fileName Log filename
     */
    public LogFileAppender(String tag, String fileName, boolean writeToExternalStorage) {
        if (tag == null) {
            throw new IllegalArgumentException("TAG cannot be null");
        }

        if (fileName == null) {
            throw new IllegalArgumentException("FileName cannot be null");
        }

        if (!isFilenameValid(fileName)) {
            throw new IllegalArgumentException("Invalid fileName");
        }

        mWriteToExternal = writeToExternalStorage;

        LOG_FILE_NAME = fileName + ".log";

        TAG = tag;
    }

    /**
     * Check if fileName is valid
     *
     * @param fileName fileName
     * @return true if it is, false if not
     */
    protected boolean isFilenameValid(String fileName) {
        return fileName.matches("\\w+");
    }

    /**
     * Converts LOG level into FileHandler level
     *
     * @param type LOG type
     * @return FileHandler level
     */
    @VisibleForTesting
    public Level getFileHandlerLevel(LOG.PRIORITY type) {

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
    public void enableAppender(final Context context) {
        final int MBYTE_IN_BYTES = 1024 * 1024;

        mFileLoggingTP = ThreadPoolUtils.getFixedThreadPool("LogToFileTP", 1);

        mFileLoggingTP.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    mFileHandler = new FileHandler(getFileLogPath(context),
                            mLogFileSize * MBYTE_IN_BYTES, mNumberOfLogFiles, true);
                    mFileHandler.setFormatter(new SimpleFormatter());
                    mFileHandler.setFormatter(new Formatter() {
                        @Override
                        public String format(LogRecord logRecord) {
                            return logRecord.getMessage() + "\n";
                        }
                    });

                    mCanWriteToFile = true;

                } catch (Throwable e) {
                    mCanWriteToFile = false;
                    LOG.e(LOG_TAG, e, "Logging to file startup error");
                }
            }
        });

    }

    @Override
    public void disableAppender() {

        if (mFileHandler != null) {
            mFileHandler.close();
            mFileHandler = null;
        }

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
        if (type.ordinal() < mMinLogLevel.ordinal()) {
            return;
        }

        if (!mCanWriteToFile) {
            Log.e(LOG_TAG, "Cannot write to file");
            return;
        }

        if (!isThreadPoolRunning()) {
            mCanWriteToFile = false;
            LOG.e(LOG_TAG, "Error on submitToFileLoggingQueue: mFileLoggingTP is not available");
        }

        mFileLoggingTP.submit(new Runnable() {
            @Override
            public void run() {
                if (mFileHandler != null) {
                    Level level = getFileHandlerLevel(type);

                    try {
                        String logText = formatLog(type, t, logs);

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


    }

    /**
     * Formats the log
     *
     * @param type Type of log
     * @param t    Throwable (can be null)
     * @param logs Log
     */
    protected String formatLog(final LOG.PRIORITY type, final Throwable t, final String... logs) {
        final StringBuilder builder = new StringBuilder();
        builder.append(dateFormatter.format(new Date()));
        builder.append(": ");
        builder.append(type.name().charAt(0));
        builder.append("/");
        builder.append(TAG);
        builder.append("(").append(Thread.currentThread().getId());
        builder.append(")");
        builder.append(": ");
        builder.append(getLogString(logs));

        return builder.toString();

    }

//    /**
//     * Get list of file logs
//     *
//     * @param logsPath Path to the logs folder
//     * @return List of logs files
//     */
//    public ArrayList<String> getListLogFiles(String logsPath) {
//        if (logsPath == null || logsPath.isEmpty()) {
//            return null;
//        }
//
//        ArrayList<String> logFiles = new ArrayList<>();
//
//        File logsDirectory = new File(logsPath);
//        if (logsDirectory.exists()) {
//            String[] fileList = logsDirectory.list();
//            if (fileList != null && fileList.length != 0) {
//                for (String file : fileList) {
//                    if (file.contains(".log")) {
//                        logFiles.add(file);
//                    }
//                }
//            }
//        }
//        return logFiles;
//    }

    public void setLogFileSize(int LOG_FILE_SIZE) {
        this.mLogFileSize = LOG_FILE_SIZE;
    }

    public void setNumberOfLogFiles(int NUMBER_OF_LOG_FILES) {
        this.mNumberOfLogFiles = NUMBER_OF_LOG_FILES;
    }

    public int getLogFileSize() {
        return mLogFileSize;
    }

    public int getNumberOfLogFiles() {
        return mNumberOfLogFiles;
    }

    public String getFileLogPath(Context context) {
        String path;
        if (mWriteToExternal) {
            path = AndroidUtils.getExternalPublicDirectory(File.separator + LOG_FILE_NAME);
        } else {
            path = AndroidUtils.getFileDirPath(context, File.separator + LOG_FILE_NAME);
        }
        return path;
    }

    @Override
    public LOG.PRIORITY getMinLogLevel() {
        return mMinLogLevel;
    }

    @Override
    public void setMinLogLevel(LOG.PRIORITY minLogLevel) {
        mMinLogLevel = minLogLevel;
    }

    public boolean canWriteToFile() {
        return mCanWriteToFile && mFileHandler != null;
    }

    @Override
    public String getLoggerId() {
        return LOG_ID;
    }

    public boolean isThreadPoolRunning() {
        return mFileLoggingTP != null
                && !mFileLoggingTP.isTerminating()
                && !mFileLoggingTP.isTerminated()
                && !mFileLoggingTP.isShutdown();
    }
}
