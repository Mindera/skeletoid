package com.mindera.skeletoid.logs

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.mindera.skeletoid.generic.AndroidUtils
import com.mindera.skeletoid.logs.LOG.PRIORITY
import com.mindera.skeletoid.logs.LOG.e
import com.mindera.skeletoid.logs.appenders.ILogAppender
import com.mindera.skeletoid.logs.utils.LogAppenderUtils
import com.mindera.skeletoid.threads.utils.ThreadUtils
import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap
import java.util.HashSet

/**
 * LOG main class. It contains all the logic and feeds the appenders
 */
internal class LoggerManager : ILoggerManager {

    companion object {
        private const val LOG_TAG = "LoggerManager"

        /**
         * Log format
         */
        const val LOG_FORMAT_4ARGS = "%s %s %s | %s"

    }
    /**
     * Application TAG for logs
     */
    private val PACKAGE_NAME: String

    /**
     * Define if the method name invoking the log should be printed or not (via exception stack)
     */
    @JvmField
    @VisibleForTesting
    var mAddMethodName = false

    /**
     * List of appenders (it can be improved to an ArrayMap if we want to add the support lib as dependency
     */
    private val mLogAppenders: MutableMap<String, ILogAppender> =
        HashMap()

    /**
     * The logger itself
     */
    constructor(context: Context) {
        PACKAGE_NAME = AndroidUtils.getApplicationPackage(context)
    }

    /**
     * This is to be used on ONLY ON UNIT TESTS.
     */
    constructor(packageName: String) {
        PACKAGE_NAME = packageName
    }

    /**
     * Enables or disables logging to console/logcat.
     */
    override fun addAppenders(
        context: Context,
        logAppenders: List<ILogAppender>
    ): Set<String> {
        val appenderIds: MutableSet<String> =
            HashSet()
        for (logAppender in logAppenders) {
            val loggerId = logAppender.loggerId
            if (mLogAppenders.containsKey(loggerId)) {
                log(
                    LOG_TAG,
                    PRIORITY.ERROR,
                    null,
                    "Replacing Log Appender with ID: $loggerId"
                )
                val oldLogAppender = mLogAppenders.remove(loggerId)
                oldLogAppender!!.disableAppender()
            }
            logAppender.enableAppender(context)
            appenderIds.add(loggerId)
            mLogAppenders[loggerId] = logAppender
        }
        return appenderIds
    }

    /**
     * Enables or disables logging to console/logcat.
     */
    override fun removeAppenders(
        context: Context,
        loggerIds: Set<String>
    ) {
        if(mLogAppenders.isNotEmpty()) {
            for (logId in loggerIds) {
                val logAppender = mLogAppenders.remove(logId)
                logAppender?.disableAppender()
            }
        }
    }

    override fun removeAllAppenders() {
        if(mLogAppenders.isNotEmpty()) {
            val appendersKeys: List<String?> =
                ArrayList(mLogAppenders.keys)
            for (logId in appendersKeys) {
                val analyticsAppender = mLogAppenders.remove(logId)
                analyticsAppender?.disableAppender()
            }
        }
    }

    override fun setUserProperty(key: String, value: String) {
        for (logAppender in mLogAppenders.values) {
            logAppender.setUserProperty(key, value)
        }
    }

    override fun setMethodNameVisible(visibility: Boolean) {
        mAddMethodName = visibility
    }

    private fun pushLogToAppenders(
        type: PRIORITY,
        t: Throwable?,
        log: String
    ) {
        for ((_, value) in mLogAppenders) {
            value.log(type, t, log)
        }
    }

    override fun log(
        tag: String,
        priority: PRIORITY,
        t: Throwable?,
        vararg text: String
    ) {
        if(mLogAppenders.isNotEmpty()) {
            val log = String.format(
                LOG_FORMAT_4ARGS,
                tag,
                LogAppenderUtils.getObjectHash(tag),
                ThreadUtils.currentThreadName,
                LogAppenderUtils.getLogString(*text)
            )
            pushLogToAppenders(priority, t, log)
        }
    }
}