package com.mindera.skeletoid.generic;

import android.support.annotation.VisibleForTesting;

/**
 * The HexUtils class provides convenient methods to perform hexadecimal conversions.
 */
public final class HexUtils {

    /**
     * Hex conversion values.
     */
    private static final String HEX_VALUES = "0123456789abcdef";
    /**
     * Static StringBuilder Instance.
     */
    private static final StringBuilder sStringBuilder = new StringBuilder();

    @VisibleForTesting
    HexUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Converts a byte array to an hexadecimal String.
     *
     * @param bytes the byte array to convert
     * @return the equivalent hexadecimal String
     */
    public static String toHex(final byte[] bytes) {

        sStringBuilder.setLength(0);

        for (int i = 0; i < bytes.length; i++) {

            sStringBuilder.append(HEX_VALUES.charAt((bytes[i] >> 4) & 0xf));
            sStringBuilder.append(HEX_VALUES.charAt(bytes[i] & 0xf));
        }

        return sStringBuilder.toString();
    }

    /**
     * Converts an hexadecimal String to a byte array.
     *
     * @param hexString Hexadecimal String to convert.
     * @return Equivalent byte array.
     */
    public static byte[] toByte(final String hexString) {

        int len = hexString.length();
        final String internalString;
        if (len % 2 != 0) {

            internalString = "0" + hexString;
            len++;

        } else {

            internalString = hexString;
        }
        final byte[] ba = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {

            ba[i / 2] = (byte) ((Character.digit(internalString.charAt(i), 16) << 4) + Character.digit(
                    internalString.charAt(i + 1), 16));
        }

        return ba;
    }
}
