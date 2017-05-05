package com.mindera.skeletoid.logs.utils;


/**
 * Abstract LOG Appender
 */
public class LogAppenderUtils {
    /**
     * Convert log array to string
     *
     * @param logs List of strings
     * @return the list of strings concatenated or an empty string if null
     */
    public static String getLogString(String... logs) {
        if (logs == null) {
            return "";
        }

        StringBuilder strBuilder = new StringBuilder();
        for (String log : logs) {
            strBuilder.append(log);
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }


    /**
     * Get class name with ClassName pre appended.
     *
     * @param clazz Class to get the tag from
     * @return Tag string
     */
    public static String getTag(Class clazz, boolean usePackageName, String packageName, boolean methodName) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (usePackageName) {
            stringBuilder.append(packageName);
            stringBuilder.append("/");
        }

        stringBuilder.append(clazz.getCanonicalName());

        if (methodName) {
            stringBuilder.append("." + getMethodName(clazz));
        }

        return stringBuilder.toString();
    }

    /**
     * Get class method name. This will only work when proguard is not active
     *
     * @param clazz The class being logged
     */
    public static String getMethodName(Class clazz) {
        int index = 0;
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        for (StackTraceElement ste : stackTrace) {
            if (ste.getClassName().equals(clazz.getName())) {
                break;
            }
            index++;
        }

        final String methodName;

        if (stackTrace.length > index) {
            methodName = stackTrace[index].getMethodName();
        } else {
            methodName = "UnknownMethod";
        }

        return methodName;
    }

    /**
     * Gets the hashcode of the object sent
     *
     * @return The hashcode of the object in a printable string
     */
    public static String getObjectHash(Object object) {
        String hashCodeString;
        if (object == null) {
            hashCodeString = "[!!!NULL INSTANCE!!!] ";
        } else {
            hashCodeString = "[OID#" + object.hashCode() + "] ";
        }

        return hashCodeString;
    }


}
