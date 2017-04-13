package com.mindera.skeletoid.logs.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.mindera.skeletoid.generic.AndroidUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * A way to share log file easily
 * <p>
 * Remember to add this to the Android Manifest of the App
 * <p>
 * <provider
 * android:name="android.support.v4.content.FileProvider"
 * android:authorities="${applicationId}"
 * android:exported="false"
 * android:grantUriPermissions="true">
 * <meta-data
 * android:name="android.support.FILE_PROVIDER_PATHS"
 * android:resource="@xml/fileprovider"/>
 * </provider>
 * <p>
 * and add the file fileprovider.xml to the resources with
 * <p>
 * <?xml version="1.0" encoding="utf-8"?>
 * <paths>
 * <files-path
 * path="."
 * name="logs" />
 * </paths>
 */
public class ShareLogFilesUtils {

    /**
     * Class to be able to share LogFileAppender generated files
     *
     * @param activity           The activity
     * @param intentChooserTitle Intent chooser title
     * @param subjectTitle       Subject title (for email)
     * @param bodyText           Body text
     */
    public static void sendLogs(Activity activity, String intentChooserTitle, String subjectTitle, String bodyText) {
        File file = new File(getFileLogPath(activity.getApplicationContext()));

        final Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName(), file);
        ArrayList<Uri> uris = new ArrayList<Uri>();
        uris.add(uri);

        final Intent intent;
        if (uris.size() == 1) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
        } else {
            intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        }
//        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        intent.putExtra(Intent.EXTRA_SUBJECT, subjectTitle);

        // Add additional information to the email
        intent.putExtra(Intent.EXTRA_TEXT, bodyText);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("text/plain");
        activity.startActivity(Intent.createChooser(intent, intentChooserTitle));
    }

    public static String getFileLogPath(Context context) {
        return AndroidUtils.getFileDirPath(context, "");
    }
}
