package com.mindera.skeletoid.logs

import android.content.Context
import com.mindera.skeletoid.logs.appenders.interfaces.ILogAppender
import com.mindera.skeletoid.logs.interfaces.ILoggerManager

/**
 * LOG static class. It is used to abstract the LOG and have multiple possible implementations
 * It is used also to serve as static references for logging methods to be called.
 */
object LOG {

    enum class PRIORITY {
        VERBOSE, DEBUG, INFO, ERROR, WARN, FATAL
    }

    /**
     * Get LoggerManager instance.
     */
    private var instance: ILoggerManager? = null

    /**
     * Return true if initialized
     *
     * @return true if initialized
     */
    val isInitialized: Boolean
        get() = instance != null

    /**
     * Sets a custom property of the user
     *
     * @param key Property key
     * @param value Property value
     */
    fun setUserProperty(key: String, value: String) {
        instance?.setUserProperty(key, value)
    }

    /**
     * Init the logger. This method MUST be called before using LoggerManager
     *
     * @param context      Context app
     * @param packageName  Packagename
     * @param logAppenders The log appenders to be started
     */
    @JvmStatic
    @Synchronized
    fun init(
        invokingClass: Any,
        context: Context,
        packageName: String? = null,
        logAppenders: List<ILogAppender> = emptyList()
    ): Set<String> {
        val logger = getInstance(context, packageName)
        logger.removeAllAppenders()
        return logger.addAppenders(invokingClass, context, logAppenders)
    }

    /**
     * Deinit the logger
     * This method can be called if the LOG is not needed any longer on the app.
     */
    @JvmStatic
    fun deinit() {
        instance?.removeAllAppenders()
        instance = null
    }

    /**
     * Enable log appenders
     *
     * @param context      Context
     * @param logAppenders Log appenders to enable
     * @return Ids of the logs enabled by their order
     */
    @JvmStatic
    fun addAppenders(
        invokingClass: Any,
        context: Context,
        logAppenders: List<ILogAppender>
    ): Set<String> {
        return instance?.addAppenders(invokingClass, context, logAppenders)
            ?: throw UninitializedPropertyAccessException("Please call init() before using")
    }

    /**
     * Disable log appenders
     *
     * @param context   Context
     * @param loggerIds Log ids of each of the loggers enabled by the order sent
     */
    @JvmStatic
    fun removeAppenders(
        context: Context,
        loggerIds: Set<String>
    ) {
        instance?.removeAppenders(context, loggerIds)
    }

    /**
     * Set method name visible in logs (careful this is a HEAVY operation)
     *
     * @param visibility true if enabled
     */
    fun setMethodNameVisible(visibility: Boolean) {
        instance?.setMethodNameVisible(visibility)
    }

    /**
     * Obtain a instance of the logger to guarantee it's unique
     *
     * @param context
     * @return
     */
    private fun getInstance(
        context: Context,
        packageName: String? = null
    ): ILoggerManager {

        instance ?: synchronized(LOG.javaClass) {
            instance = if (packageName != null) LoggerManager(packageName) else LoggerManager(context)
        }
        return instance!!
    }

    /**
     * Log with a DEBUG level
     *
     * @param tag  Tag
     * @param text List of strings
     */
    @JvmStatic
    fun d(tag: String, vararg text: String) {
        instance?.log(null, tag, PRIORITY.DEBUG, null, *text)
    }

    @JvmStatic
    fun d(invokingClass: Any, tag: String, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.DEBUG, null, *text)
    }

    /**
     * Log with a ERROR level
     *
     * @param tag  Tag
     * @param text List of strings
     */
    @JvmStatic
    fun e(tag: String, vararg text: String) {
        instance?.log(null, tag, PRIORITY.ERROR, null, *text)
    }

    @JvmStatic
    fun e(invokingClass: Any, tag: String, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.ERROR, null, *text)
    }

    /**
     * Log with a VERBOSE level
     *
     * @param tag  Tag
     * @param text List of strings
     */
    @JvmStatic
    fun v(tag: String, vararg text: String) {
        instance?.log(null, tag, PRIORITY.VERBOSE, null, *text)
    }

    @JvmStatic
    fun v(invokingClass: Any, tag: String, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.VERBOSE, null, *text)
    }

    /**
     * Log with a INFO level
     *
     * @param tag  Tag
     * @param text List of strings
     */
    @JvmStatic
    fun i(tag: String, vararg text: String) {
        instance?.log(null, tag, PRIORITY.INFO, null, *text)
    }

    @JvmStatic
    fun i(invokingClass: Any, tag: String, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.INFO, null, *text)
    }

    /**
     * Log with a WARN level
     *
     * @param tag  Tag
     * @param text List of strings
     */
    @JvmStatic
    fun w(tag: String, vararg text: String) {
        instance?.log(null, tag, PRIORITY.WARN, null, *text)
    }

    @JvmStatic
    fun w(invokingClass: Any, tag: String, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.WARN, null, *text)
    }

    /**
     * Log a What a Terrible Failure: Report an exception that should never happen.
     *
     * @param tag  Tag
     * @param text List of strings
     */
    @JvmStatic
    fun wtf(tag: String, vararg text: String) {
        instance?.log(null, tag, PRIORITY.FATAL, null, *text)
    }

    @JvmStatic
    fun wtf(invokingClass: Any, tag: String, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.FATAL, null, *text)
    }

    /**
     * Log with a DEBUG level
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    @JvmStatic
    fun d(tag: String, t: Throwable, vararg text: String) {
        instance?.log(null, tag, PRIORITY.DEBUG, t, *text)
    }

    @JvmStatic
    fun d(invokingClass: Any, tag: String, t: Throwable, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.DEBUG, t, *text)
    }

    /**
     * Log with a ERROR level
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    @JvmStatic
    fun e(tag: String, t: Throwable, vararg text: String) {
        instance?.log(null, tag, PRIORITY.ERROR, t, *text)
    }

    @JvmStatic
    fun e(invokingClass: Any, tag: String, t: Throwable, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.ERROR, t, *text)
    }

    /**
     * Log with a VERBOSE level
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    @JvmStatic
    fun v(tag: String, t: Throwable, vararg text: String) {
        instance?.log(null, tag, PRIORITY.VERBOSE, t, *text)
    }

    @JvmStatic
    fun v(invokingClass: Any, tag: String, t: Throwable, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.VERBOSE, t, *text)
    }

    /**
     * Log with a INFO level
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    @JvmStatic
    fun i(tag: String, t: Throwable, vararg text: String) {
        instance?.log(null, tag, PRIORITY.INFO, t, *text)
    }

    @JvmStatic
    fun i(invokingClass: Any, tag: String, t: Throwable, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.INFO, t, *text)
    }

    /**
     * Log with a WARN level
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    @JvmStatic
    fun w(tag: String, t: Throwable, vararg text: String) {
        instance?.log(null, tag, PRIORITY.WARN, t, *text)
    }

    @JvmStatic
    fun w(invokingClass: Any, tag: String, t: Throwable, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.WARN, t, *text)
    }

    /**
     * Log a What a Terrible Failure: Report an exception that should never happen.
     *
     * @param tag  Tag
     * @param t    Throwable
     * @param text List of strings
     */
    @JvmStatic
    fun wtf(tag: String, t: Throwable, vararg text: String) {
        instance?.log(null, tag, PRIORITY.FATAL, t, *text)
    }

    @JvmStatic
    fun wtf(invokingClass: Any, tag: String, t: Throwable, vararg text: String) {
        instance?.log(invokingClass, tag, PRIORITY.FATAL, t, *text)
    }
}