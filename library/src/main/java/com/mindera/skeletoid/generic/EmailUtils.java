package com.mindera.skeletoid.generic;

/**
 * Created by neteinstein on 19/05/2017.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class with utils related to email addresses
 */
public class EmailUtils {

    /**
     * Compare emails. Ignore case.
     * @param e1 The first email
     * @param e2 The second email
     * @return true if it's the same, false if not.
     */
    public static boolean emailsEquals(String e1, String e2) {
        if(e1 == null || e2 == null){
            return false;
        }

        final String email1 = e1.toLowerCase();
        final String email2 = e2.toLowerCase();

        if(email1.equals(email2)){
            return true;
        }

        final boolean result;

        Pattern address = Pattern.compile("([\\w\\.]+)@([\\w\\.]+\\.\\w+)");
        Matcher match1 = address.matcher(email1);
        Matcher match2 = address.matcher(email2);
        if (!match1.find() || !match2.find()) {
            result = false; //Not an e-mail address? Already false
        } else if (!match1.group(2).equalsIgnoreCase(match2.group(2))) {
            result = false; //Not same serve? Already false
        } else if (match1.group(2).toLowerCase().equals("gmail.com")) {
            String gmail1 = match1.group(1).replace(".", "");
            String gmail2 = match2.group(1).replace(".", "");
            result = gmail1.equalsIgnoreCase(gmail2);
        } else {
            result = match1.group(1).equalsIgnoreCase(match2.group(1));
        }
        return result;
    }
}
