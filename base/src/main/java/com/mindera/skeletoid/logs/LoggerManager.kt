package com.mindera.skeletoid.logs

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.mindera.skeletoid.generic.AndroidUtils
import com.mindera.skeletoid.logs.LOG.PRIORITY
import com.mindera.skeletoid.logs.appenders.interfaces.ILogAppender
import com.mindera.skeletoid.logs.interfaces.ILoggerManager
import com.mindera.skeletoid.logs.utils.LogAppenderUtils
import com.mindera.skeletoid.threads.utils.ThreadUtils
import java.util.ArrayList
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
    private val packageName: String

    /**
     * Define if the method name invoking the log should be printed or not (via exception stack)
     */
    @JvmField
    @VisibleForTesting
    var addMethodName = false

    /**
     * List of appenders (it can be improved to an ArrayMap if we want to add the support lib as dependency
     */
    private val logAppenders: MutableMap<String, ILogAppender> = HashMap()

    constructor(context: Context) {
        packageName = AndroidUtils.getApplicationPackage(context)
    }

    constructor(packageName: String) {
        this.packageName = packageName
    }

    /**
     * Enables or disables logging to console/logcat.
     */
    override fun addAppenders(
        invokingClass: Any,
        context: Context,
        logAppenders: List<ILogAppender>
    ): Set<String> {
        val appenderIds: MutableSet<String> = HashSet()
        for (logAppender in logAppenders) {
            val loggerId = logAppender.loggerId
            if (this.logAppenders.containsKey(loggerId)) {
                log(invokingClass,
                    LOG_TAG, PRIORITY.ERROR, null, "Replacing Log Appender with ID: $loggerId")
                val oldLogAppender = this.logAppenders.remove(loggerId)
                oldLogAppender!!.disableAppender()
            }
            logAppender.enableAppender(context)
            appenderIds.add(loggerId)
            this.logAppenders[loggerId] = logAppender
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
        if(logAppenders.isNotEmpty()) {
            for (logId in loggerIds) {
                val logAppender = logAppenders.remove(logId)
                logAppender?.disableAppender()
            }
        }
    }

    override fun removeAllAppenders() {
        if(logAppenders.isNotEmpty()) {
            val appendersKeys: List<String?> = ArrayList(logAppenders.keys)
            for (logId in appendersKeys) {
                val analyticsAppender = logAppenders.remove(logId)
                analyticsAppender?.disableAppender()
            }
        }
    }

    override fun setUserProperty(key: String, value: String) {
        for (logAppender in logAppenders.values) {
            logAppender.setUserProperty(key, value)
        }
    }

    override fun setMethodNameVisible(visibility: Boolean) {
        addMethodName = visibility
    }

    private fun pushLogToAppenders(
        type: PRIORITY,
        t: Throwable?,
        log: String
    ) {
        for ((_, value) in logAppenders) {
            value.log(type, t, log)
        }
    }

    override fun log(
        invokingClass: Any,
        tag: String,
        priority: PRIORITY,
        t: Throwable?,
        vararg text: String
    ) {
        if(logAppenders.isNotEmpty()) {
            val log = String.format(
                LOG_FORMAT_4ARGS,
                tag,
                LogAppenderUtils.getObjectHash(invokingClass),
                ThreadUtils.currentThreadName,
                LogAppenderUtils.getLogString(*text)
            )
            pushLogToAppenders(priority, t, log)
        }
    }
}