package com.mindera.skeletoid.generic;

import java.util.Locale;

public class FileUtils {

    /**
     * Return a human readable String for the given file size. When the conversion is done in 1024
     * the string will add a
     * "i" as in ex: GiB, Mib so we can visually spot the difference.
     *
     * @param bytes Size in Bytes.
     * @param si    If the conversion should be done in 1000 or 1024.
     * @return Human readable String for the given file size.
     */
    public static String friendlyFileSize(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.getDefault(), "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
