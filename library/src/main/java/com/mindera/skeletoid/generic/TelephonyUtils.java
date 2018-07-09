package com.mindera.skeletoid.generic;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.VisibleForTesting;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * Created by neteinstein on 19/05/2017.
 */

public class TelephonyUtils {

    private static final String LOG_TAG = "TelephonyUtils";

    private static Boolean hasTelephony;

    /**
     * Minimum IMSI or SIM Serial length to be considered valid. *
     */
    private static final int MIN_ID_LENGTH = 15;

    /**
     * TYPEs of networks we can be connected to
     */
    public final static int TYPE_NONE = -1;
    public final static int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
    public final static int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
    public static final int TYPE_MOBILE_MMS = ConnectivityManager.TYPE_MOBILE_MMS;
    public final static int TYPE_MOBILE_SUPL = ConnectivityManager.TYPE_MOBILE_SUPL;
    public static final int TYPE_MOBILE_DUN = ConnectivityManager.TYPE_MOBILE_DUN;
    public static final int TYPE_MOBILE_HIPRI = ConnectivityManager.TYPE_MOBILE_HIPRI;
    public final static int TYPE_WIMAX = ConnectivityManager.TYPE_WIMAX;
    public static final int TYPE_BLUETOOTH = 7;
    // ConnectivityManager.TYPE_BLUETOOTH only available from API level 13
    public static final int TYPE_DUMMY = 8;
    // ConnectivityManager.TYPE_DUMMY only available from API level 13
    public final static int TYPE_ETHERNET = 9;
    // ConnectivityManager.TYPE_ETHERNET only available from API level 13

    @VisibleForTesting
    TelephonyUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks if the current device has SIM card support.
     *
     * @param context The application context
     * @return True if device supports SIM card, false otherwise.
     */
    public static boolean hasSimCardSupport(Context context) {
        if (context == null) {
            throw new RuntimeException("Context cannot be null");
        }

        PackageManager pm = context.getPackageManager();
        boolean deviceSupportsSIM = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        return deviceSupportsSIM;
    }

    /**
     * Retrieves the MCC/MNC from the currently inserted SIM card.
     *
     * @return MCCMNC string or null if none or not readable.
     */
    public static String getMccMnc(Context context) {

        if (context == null) {
            throw new RuntimeException("Context cannot be null");
        }

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String simOperator = null;

        if (telephonyManager != null) {
            int simState = telephonyManager.getSimState();
            String imsi = telephonyManager.getSubscriberId();
            String simSerial = telephonyManager.getSimSerialNumber();
            simOperator = telephonyManager.getSimOperator();

        }

        return simOperator;
    }

    /**
     * Return TRUE if the SIM is ready, FALSE otherwise.
     *
     * @return TRUE if the SIM is ready, FALSE otherwise.
     */
    public static boolean isSimReady(Context context) {
        if (context == null) {
            throw new RuntimeException("Context cannot be null");
        }

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * Return the SIM IMSI value or NULL if not available.
     *
     * @return SIM IMSI value or NULL if not available
     */
    public static String getImsi(Context context) {
        if (context == null) {
            throw new RuntimeException("Context cannot be null");
        }

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String imsi = null;

        if (telephonyManager != null) {
            imsi = telephonyManager.getSubscriberId();
        }
        return imsi;
    }

    /**
     * Return the SIM MCC/MNC value or NULL if not available.
     *
     * @return SIM MCC/MNC value or NULL if not available
     */

    /**
     * Return the SIM Serial or NULL if not available.
     *
     * @return SIM Serial number or NULL if not available.
     */

    public static String getSimSerial(Context context) {
        if (context == null) {
            throw new RuntimeException("Context cannot be null");
        }

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String simOperator = null;

        if (telephonyManager != null) {
            return null;
        }
        return telephonyManager.getSimSerialNumber();
    }

    /**
     * Return TRUE if the IMSI is valid (i.e. has a sufficient length to authenticate the SIM),
     * FALSE otherwise.
     *
     * @return TRUE if the IMSI is valid (i.e. has a sufficient length to authenticate the SIM),
     * FALSE otherwise.
     */
    public static boolean isSimImsiValid(Context context) {
        String imsi = getImsi(context);

        return (imsi != null) && (imsi.length() >= MIN_ID_LENGTH);
    }

    /**
     * Returns if the MCC/MNC is valid.
     *
     * @return TRUE if the MCC/MNC is valid, FALSE otherwise.
     */
    public static boolean isMccMncValid(Context context) {
        return !TextUtils.isEmpty(getMccMnc(context));
    }

    /**
     * Return TRUE if the SIM Serial is valid (i.e. has a sufficient length to authenticate the
     * SIM), FALSE otherwise.
     *
     * @return TRUE if the SIM Serial is valid (i.e. has a sufficient length to authenticate the
     * SIM), FALSE otherwise.
     */
    public static boolean isSimSerialValid(Context context) {
        String simSerial = getSimSerial(context);
        return (simSerial != null) && (simSerial.length() >= MIN_ID_LENGTH);
    }

    /**
     * Get the current sim country
     *
     * @param context The context of the app
     * @return return the country code is of the sim if available, or null
     */
    public static String getCurrentCountryISO(Context context) {
        if (context == null) {
            throw new RuntimeException("Context cannot be null");
        }

        final String countryCodeISO;
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        // Access Sim Country Code
        if (telephonyManager != null) {
            countryCodeISO = telephonyManager.getSimCountryIso();
        } else {
            countryCodeISO = null;
        }
        return countryCodeISO;
    }

    /**
     * Checks if the device has mobile network support.
     *
     * @param context an Android Context.
     * @return true if the phone has support for mobile network.
     */
    public static boolean hasMobileDataSupport(Context context) {
        // The approach on this method is to check both system variables, FEATURE_TELEPHONY and from the network info
        // the TYPE_MOBILE, and consider that the device supports mobile data if one of this variables is true. This is
        // done in order to workaround the possibility of one of this variables being wrongly defined in the system.

        boolean isMobileSupported = false;
        // Check for Telephony feature availability
//        LOG.d(LOG_TAG, "Device has FEATURE_TELEPHONY available?: "
//                + hasSimCardSupport(context));
        isMobileSupported = hasSimCardSupport(context);

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (networkInfo != null) {
            boolean isMobileConn = false;
//            LOG.d(LOG_TAG,
//                    "Mobile network is supported.");
            isMobileConn = networkInfo.isConnected();
//            LOG.d(LOG_TAG,
//                    "Is Mobile network type connected?: "
//                            + isMobileConn);

        } else {
//            LOG.d(LOG_TAG,
//                    "Mobile network is NOT supported.");
        }

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            int simState = tm.getSimState();
            String simStateName = simState + "";
            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    simStateName = "SIM_STATE_ABSENT";
                    break;
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    simStateName = "SIM_STATE_NETWORK_LOCKED";
                    break;
                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    simStateName = "SIM_STATE_PIN_REQUIRED";
                    break;
                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    simStateName = "SIM_STATE_PUK_REQUIRED";
                    break;
                case TelephonyManager.SIM_STATE_READY:
                    simStateName = "SIM_STATE_READY";
                    break;
                case TelephonyManager.SIM_STATE_UNKNOWN:
                    simStateName = "SIM_STATE_UNKNOWN";
                    break;
                default:
                    break;
            }
//            LOG.d(LOG_TAG,
//                    "Current sim state is: " + simStateName);
        } else {
//            LOG.d(LOG_TAG,
//                    "Telephony Manager NOT available.");
        }

        NetworkInfo[] allNetworkInfo = cm.getAllNetworkInfo();
//        LOG.d(LOG_TAG,
//                "The following networks are available on this device.");

        if (allNetworkInfo != null) {
            for (NetworkInfo info : allNetworkInfo) {
                if (info != null) {
                    int type = info.getType();
                    String typeName = type + "";
                    switch (type) {
                        case TYPE_MOBILE:
                            typeName = "TYPE_MOBILE";
                            isMobileSupported = true;
                            break;
                        case TYPE_WIFI:
                            typeName = "TYPE_WIFI";
                            break;
                        case TYPE_MOBILE_MMS:
                            typeName = "TYPE_MOBILE_MMS";
                            break;
                        case TYPE_MOBILE_SUPL:
                            typeName = "TYPE_MOBILE_SUPL";
                            break;
                        case TYPE_MOBILE_DUN:
                            typeName = "TYPE_MOBILE_DUN";
                            break;
                        case TYPE_MOBILE_HIPRI:
                            typeName = "TYPE_MOBILE_HIPRI";
                            break;
                        case TYPE_WIMAX:
                            typeName = "TYPE_WIMAX";
                            isMobileSupported = true;
                            break;
                        case TYPE_BLUETOOTH:
                            typeName = "TYPE_BLUETOOTH";
                            break;
                        case TYPE_DUMMY:
                            typeName = "TYPE_DUMMY";
                            break;
                        case TYPE_ETHERNET:
                            typeName = "TYPE_ETHERNET";
                            break;
                        default:
                            break;
                    }
//                    LOG.d(LOG_TAG, "Network "
//                            + typeName
//                            + " type is supported. Is it available?: "
//                            + info.isAvailable()
//                            + ". Is it connected or connecting?: "
//                            + info.isConnectedOrConnecting()
//                            + ".");
                }
            }
        }

//        LOG.d(LOG_TAG,
//                "Considering that the device has mobile data support?: " + isMobileSupported);´

        return isMobileSupported;
    }

    /**
     * Gets the current network mode.
     *
     * @param context The application context.
     * @return The network mode: "
     */
    public static String getCurrentNetworkMode(Context context) {
        if (context == null) {
            throw new RuntimeException("Context cannot be null");
        }

        final String network = TelephonyUtils.getCurrentNetworkMode(context);
        if (!TextUtils.isEmpty(network) && network.toLowerCase().contains("wifi")) {
            return "wifi";
        }
        TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "H";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "H+";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }
    }

    /**
     * Check if GSM support is available
     *
     * @param context The context
     * @return True if it has Telephony
     */
    public static boolean hasTelephony(Context context) {
        if (context == null) {
            throw new RuntimeException("Context cannot be null");
        }

        boolean hasTelephony = checkTelephony(context);
        if (!hasTelephony) {
            return false;
        }

        final TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int simState = manager.getSimState();

        boolean result = false;
        switch (simState) {
            case TelephonyManager.SIM_STATE_READY:
                result = true;
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
            case TelephonyManager.SIM_STATE_UNKNOWN:
            case TelephonyManager.SIM_STATE_ABSENT:
            default:
                result = false;
                break;
        }
        return result;
    }

    /**
     * Checks whether or not the device supports telephony.
     *
     * @param context The context calling this.
     * @return True if the device supports telephony, false otherwise.
     */
    private static boolean checkTelephony(Context context) {
        if (context == null) {
            throw new RuntimeException("Context cannot be null");
        }

        if (hasTelephony == null) {
            final TelephonyManager tmanager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (tmanager == null) {
                hasTelephony = false;
                return hasTelephony;
            }

            // in 1.6 and older, PackageManager.hasSystemFeature() does not
            // exist - return true
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.DONUT) {
                hasTelephony = true;
                return hasTelephony;
            }

            final PackageManager pmanager = context.getPackageManager();
            if (pmanager == null) {
                hasTelephony = false;
                return hasTelephony;
            } else {
                try {
                    @SuppressWarnings("rawtypes")
                    final Class[] parameters = new Class[1];
                    parameters[0] = String.class;

                    final Method method = pmanager.getClass()
                            .getMethod("hasSystemFeature", parameters);

                    final Object[] argument = new Object[1];
                    argument[0] = new String(PackageManager.FEATURE_TELEPHONY);

                    Object result = method.invoke(pmanager, argument);
                    if (result instanceof Boolean) {
                        hasTelephony = (Boolean) result;
                    } else {
                        hasTelephony = false;
                    }
                } catch (Exception e) {
                    hasTelephony = false;
                }
            }
        }
        return hasTelephony;
    }
}
