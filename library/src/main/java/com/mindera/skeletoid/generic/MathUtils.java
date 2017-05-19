package com.mindera.skeletoid.generic;

/**
 * Created by neteinstein on 19/05/2017.
 */

public class MathUtils {


    public static int generateRangedRandomValue(int min, int max)
    {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public static double convertKmToMiles(double kms)
    {
        return kms * 0.621371;
    }

    public static double convertMilesToKm(double miles)
    {
        return miles / 0.621371;
    }

    public static double round(final double number, final int precision)
    {
        return Math.round(number * Math.pow(10, precision)) / Math.pow(10, precision);
    }

}
