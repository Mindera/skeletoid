package com.mindera.skeletoid.logs.appenders

import android.content.Context
import android.support.annotation.VisibleForTesting
import android.util.Log
import com.mindera.skeletoid.generic.AndroidUtils
import com.mindera.skeletoid.logs.LOG
import com.mindera.skeletoid.logs.utils.LogAppenderUtils.getLogString
import com.mindera.skeletoid.threads.threadpools.ThreadPoolUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.logging.FileHandler
import java.util.logging.Formatter
import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.SimpleFormatter

/**
 * Log appender for file
 */
class LogFileAppender(
    private val TAG: String,
    fileName: String,
    writeToExternalStorage: Boolean = false
) : ILogAppender {

    companion object {
        private const val LOG_TAG = "LogFileAppender"
        private const val THREAD_POOL_NAME = "LogToFileTP"
        private const val LOG_ID = "LogFileAppender"
    }

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    /**
     * Size of the log file in MBytes
     */
    var logFileSize = 5
    /**
     * Number of files to log (rolling appending)
     */
    var numberOfLogFiles = 1
    /**
     * Whether or not logging to file is possible (don't change value! This is controlled
     * automatically)
     */
    @VisibleForTesting
    @Volatile
    var canWriteToFile = false
    /**
     * Whether or not logging to file is possible (don't change value! This is controlled
     * automatically)
     */
    private var writeToExternal = false
    /**
     * FileHandler logger: To write to file *
     */
    @VisibleForTesting
    var fileHandler: FileHandler? = null
    /**
     * Thread pool to write files to disk. It will only span 1 thread
     */
    private var fileLoggingTP: ThreadPoolExecutor? = null

    //Default name, it will be replaced to packageName.log on constructor
    //It presents no problem, because this will be used only after the logger is instantiated.
    private var logFileName = "debug.log"
    /**
     * Minimum log level for this appender
     */
    override var minLogLevel: LOG.PRIORITY = LOG.PRIORITY.VERBOSE
    /**
     * LogAppender ID
     */
    override val loggerId: String = LOG_ID


    val isThreadPoolRunning: Boolean
        get() = (fileLoggingTP != null
                && fileLoggingTP?.isTerminating != true
                && fileLoggingTP?.isTerminated != true
                && fileLoggingTP?.isShutdown != true)

    init {
        if (!isFilenameValid(fileName)) {
            throw IllegalArgumentException("Invalid fileName")
        }

        writeToExternal = writeToExternalStorage

        logFileName = "$fileName.log"
    }

    /**
     * Check if fileName is valid
     *
     * @param fileName fileName
     * @return true if it is, false if not
     */
    fun isFilenameValid(fileName: String): Boolean {
        return fileName.matches("\\w+".toRegex())
    }

    /**
     * Converts LOG level into FileHandler level
     *
     * @param type LOG type
     * @return FileHandler level
     */
    @VisibleForTesting
    fun getFileHandlerLevel(type: LOG.PRIORITY): Level {
        return when (type) {
            LOG.PRIORITY.VERBOSE -> Level.ALL
            LOG.PRIORITY.WARN -> Level.WARNING
            LOG.PRIORITY.ERROR, LOG.PRIORITY.FATAL -> Level.SEVERE
            LOG.PRIORITY.INFO -> Level.INFO
            LOG.PRIORITY.DEBUG -> Level.ALL
        }
    }

    override fun enableAppender(context: Context) {
        val megabyteInBytes = 1024 * 1024

        fileLoggingTP = ThreadPoolUtils.getFixedThreadPool(THREAD_POOL_NAME, 1)

        fileLoggingTP?.run {
            submit {
                try {
                    fileHandler = FileHandler(
                        getFileLogPath(context),
                        logFileSize * megabyteInBytes, numberOfLogFiles, true
                    )
                    fileHandler!!.formatter = SimpleFormatter()
                    fileHandler!!.formatter = object : Formatter() {
                        override fun format(logRecord: LogRecord): String {
                            return logRecord.message + "\n"
                        }
                    }

                    canWriteToFile = true

                } catch (e: Throwable) {
                    canWriteToFile = false
                    LOG.e(LOG_TAG, e, "Logging to file startup error")
                }
            }
        }
    }

    override fun disableAppender() {
        fileHandler?.run {
            close()
        }

        fileHandler = null
        canWriteToFile = false

        fileLoggingTP?.run {
            ThreadPoolUtils.shutdown(this)

            try {
                // wait until task terminate
                if (!awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    ThreadPoolUtils.shutdownNow(this)
                }
            } catch (e: InterruptedException) {
                ThreadPoolUtils.shutdownNow(this)
            }
        }
    }

    override fun log(type: LOG.PRIORITY, t: Throwable?, vararg logs: String) {
        if (type.ordinal < minLogLevel.ordinal) {
            return
        }

        if (!canWriteToFile) {
            Log.e(LOG_TAG, "Cannot write to file")
            return
        }

        if (!isThreadPoolRunning) {
            canWriteToFile = false
            LOG.e(LOG_TAG, "Error on submitToFileLoggingQueue: fileLoggingTP is not available")
        }

        fileLoggingTP?.run {
            submit {
                fileHandler?.run {
                    val level = getFileHandlerLevel(type)

                    try {
                        val logText = formatLog(type, t, *logs)
                        val logRecord = LogRecord(level, logText)

                        t?.run {
                            logRecord.thrown = this
                        }

                        publish(logRecord)

                    } catch (e: Exception) {
                        Log.e(TAG, "Something is wrong", e)
                    }
                }
            }
        }
    }

    /**
     * Formats the log
     *
     * @param type Type of log
     * @param t    Throwable (can be null)
     * @param logs Log
     */
    fun formatLog(type: LOG.PRIORITY, t: Throwable?, vararg logs: String): String {
        val builder = StringBuilder()
        builder.append(dateFormatter.format(Date()))
        builder.append(": ")
        builder.append(type.name[0])
        builder.append("/")
        builder.append(TAG)
        builder.append("(").append(Thread.currentThread().id)
        builder.append(")")
        builder.append(": ")
        builder.append(getLogString(*logs))

        return builder.toString()

    }

    fun getFileLogPath(context: Context): String {
        return if (writeToExternal) {
            AndroidUtils.getExternalPublicDirectory(File.separator + logFileName)
        } else {
            AndroidUtils.getFileDirPath(context, File.separator + logFileName)
        }
    }

    fun canWriteToFile(): Boolean {
        return canWriteToFile && fileHandler != null
    }
}
