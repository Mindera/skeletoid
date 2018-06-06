package com.mindera.skeletoid.generic;


import android.support.annotation.VisibleForTesting;

public class StringUtils {

    @VisibleForTesting
    StringUtils() {
        throw new UnsupportedOperationException();
    }
    /**
     * Get the nth index of a substring on a string
     *
     * @param str    The string
     * @param substr the substring
     * @param index  nth ocurrence
     * @return The index
     */
    public static int ordinalIndexOf(String str, String substr, int index) {

        if (str == null || substr == null || index <= 0) {
            return -1;
        }

        if (str.equals(substr) && index > 1) {
            return -1;
        }

        int pos = str.indexOf(substr);

        int position = index;
        while (--position > 0 && pos != -1) {
            pos = str.indexOf(substr, pos + 1);
        }

        return pos;
    }
}
