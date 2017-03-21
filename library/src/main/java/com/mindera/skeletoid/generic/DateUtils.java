package com.mindera.skeletoid.generic;

import java.util.Calendar;

public class DateUtils {

    /**
     * Check if a timestamp if from the same day or after
     *
     * @param afterDate    The date to check if is after the other
     * @param originalDate The original date
     * @return true if the afterDate is after the originalDate
     */
    public static boolean isSameDayOrAfter(long afterDate, long originalDate) {

        final Calendar afterDateCal = Calendar.getInstance();
        afterDateCal.setTimeInMillis(afterDate);
        final Calendar originalDateCal = Calendar.getInstance();
        originalDateCal.setTimeInMillis(originalDate);

        if (afterDateCal.get(Calendar.YEAR) > originalDateCal.get(Calendar.YEAR)) {
            return true;
        } else if (afterDateCal.get(Calendar.YEAR) == originalDateCal.get(Calendar.YEAR)) {
            if (afterDateCal.get(Calendar.DAY_OF_YEAR) >= originalDateCal.get(Calendar.DAY_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }
}
