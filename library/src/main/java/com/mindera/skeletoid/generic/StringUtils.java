package com.mindera.skeletoid.generic;

import android.support.annotation.VisibleForTesting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * @param n      nth ocurrence
     * @return The index
     */
    public static int ordinalIndexOf(String str, String substr, int n) {
        if (str == null || substr == null || n <= 0) {
            return -1;
        }

        if (str.equals(substr) && n > 1) {
            return -1;
        }

        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1) {
            pos = str.indexOf(substr, pos + 1);
        }

        return pos;
    }


    public static String capitalize(String text)
    {
        if (text != null)
        {
            boolean previousWasWhiteSpace = true;
            char[] chars = text.toCharArray();
            for (int i = 0; i < chars.length; i++)
            {
                if (Character.isLetter(chars[i]))
                {
                    if (previousWasWhiteSpace)
                    {
                        chars[i] = Character.toUpperCase(chars[i]);
                    }
                    previousWasWhiteSpace = false;
                }
                else
                {
                    previousWasWhiteSpace = Character.isWhitespace(chars[i]);
                }
            }
            text = new String(chars);
        }
        return text;
    }


    public static boolean hasUpperCase(String text)
    {
        final Pattern pattern = Pattern.compile("[A-Z]+");
        final Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static boolean hasLowerCase(String text)
    {
        final Pattern pattern = Pattern.compile("[a-z]+");
        final Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static boolean hasNumerics(String text)
    {
        final Pattern pattern = Pattern.compile("[0-9]+");
        final Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static boolean hasSpecials(String text)
    {
        final Pattern pattern = Pattern.compile("(\\@|\\-|\\_|\\+|\\/|\\#|\\$|\\%|\\!|\\?|\\:|\\.|\\(|\\)|\\{|\\}|\\[|\\]|\\^|\\~)+");
        final Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static boolean hasThreeConsecutiveEqualCharacters(String text)
    {
        final Pattern pattern = Pattern.compile("(.)\\1\\1", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
}
