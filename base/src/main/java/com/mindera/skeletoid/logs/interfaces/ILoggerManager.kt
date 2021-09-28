package com.mindera.skeletoid.logs.interfaces

import android.content.Context
import com.mindera.skeletoid.logs.LOG.PRIORITY
import com.mindera.skeletoid.logs.appenders.interfaces.ILogAppender

/**
 * LOG interface
 */
interface ILoggerManager {

    /**
     * Log to all log appenders
     *
     * @param tag      Log tag
     * @param priority Log priority
     * @param t        Trowable
     * @param text     Log text
     */
    fun log(
        invokingClass: Any? = null,
        tag: String,
        priority: PRIORITY,
        t: Throwable? = null,
        vararg text: String
    )

    /**
     * Set method name visible in logs (careful this is a HEAVY operation)
     *
     * @param visibility true if enabled
     */
    fun setMethodNameVisible(visibility: Boolean)

    /**
     * Enable log appenders
     *
     * @param context      Context
     * @param logAppenders Log appenders to enable
     * @return Ids of the logs enabled by their order
     */
    fun addAppenders(
        invokingClass: Any? = null,
        context: Context,
        logAppenders: List<ILogAppender>
    ): Set<String>

    /**
     * Disable log appenders
     *
     * @param context   Context
     * @param loggerIds Log ids of each of the loggers enabled by the order sent
     */
    fun removeAppenders(
        context: Context,
        loggerIds: Set<String>
    )

    /**
     * Disable all log appenders
     */
    fun removeAllAppenders()

    /**
     * Sets a custom property of the user
     *
     * @param key Property key
     * @param value Property value
     */
    fun setUserProperty(key: String, value: String)
}