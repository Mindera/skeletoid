package com.mindera.skeletoid.logs.utils


/**
 * Abstract LOG Appender
 */
object LogAppenderUtils {
    /**
     * Convert log array to string
     *
     * @param logs List of strings
     * @return the list of strings concatenated or an empty string if null
     */
    @JvmStatic
    fun getLogString(vararg logs: String): String {
        return logs.joinToString(separator = " ")
    }

    /**
     * Get class name with ClassName pre appended.
     *
     * @param clazz Class to get the tag from
     * @return Tag string
     */
    @JvmStatic
    fun getTag(
        clazz: Class<*>,
        usePackageName: Boolean,
        packageName: String?,
        methodName: Boolean
    ): String {
        val stringBuilder = StringBuilder()
        if (usePackageName && packageName != null) {
            stringBuilder.append(packageName)
            stringBuilder.append("/")
        }
        stringBuilder.append(clazz.canonicalName)
        if (methodName) {
            stringBuilder.append(".")
            stringBuilder.append(getMethodName(clazz))
        }
        return stringBuilder.toString()
    }

    /**
     * Get class method name. This will only work when proguard is not active
     *
     * @param clazz The class being logged
     */
    @JvmStatic
    fun getMethodName(clazz: Class<*>): String {
        var index = 0
        val stackTrace = Thread.currentThread().stackTrace
        for (ste in stackTrace) {
            if (ste.className == clazz.name) {
                break
            }
            index++
        }
        return if (stackTrace.size > index) {
            stackTrace[index].methodName
        } else {
            "UnknownMethod"
        }
    }

    /**
     * Gets the hashcode of the object sent
     *
     * @return The hashcode of the object in a printable string
     */
    @JvmStatic
    fun getObjectHash(any: Any): String {
        return run {
            val stringBuilder = StringBuilder()
            stringBuilder.append("[")
            stringBuilder.append(any.javaClass.simpleName)
            stringBuilder.append("#")
            stringBuilder.append(any.hashCode())
            stringBuilder.append("] ")
            stringBuilder.toString()
        }
    }
}