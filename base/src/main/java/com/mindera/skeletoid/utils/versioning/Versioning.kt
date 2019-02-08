package com.mindera.skeletoid.utils.versioning


object Versioning {
    /**
     * Compares two version strings.
     * Credits to: https://gist.github.com/antalindisguise/d9d462f2defcfd7ae1d4
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    fun compareVersions(str1: String, str2: String): Int {
        if (str1.isBlank() || str2.isBlank()) {
            throw IllegalArgumentException("Invalid Version")
        }

        val vals1 = str1.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val vals2 = str2.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var i = 0
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.size && i < vals2.size && vals1[i] == vals2[i]) {
            i++
        }
        // compare first non-equal ordinal number
        return run {
            // compare first non-equal ordinal number
            if (i < vals1.size && i < vals2.size) {
                vals1[i].toInt().compareTo(vals2[i].toInt())
            } else {
                // the strings are equal or one string is a substring of the other
                // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
                vals1.size - vals2.size
            }
        }.sign
            val diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]))
            Integer.signum(diff)
        } else {
            Integer.signum(vals1.size - vals2.size)
        }// the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
    }

}
