package com.mindera.skeletoid.logs.utils;

import com.mindera.skeletoid.generic.AndroidUtils;
import com.mindera.skeletoid.logs.LOG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    private static final String TAG = "ShareLogFilesUtils";

    private static final String FOLDER_LOGS = "logs";
    private static final String FILE_LOG_ZIP = "logs.zip";

    @VisibleForTesting
    ShareLogFilesUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Class to be able to share LogFileAppender generated files
     *
     * @param activity           The activity
     * @param intentChooserTitle Intent chooser title
     * @param subjectTitle       Subject title (for email)
     * @param bodyText           Body text
     * @param emails             Emails to add on to: field (for email)
     * @param file               Log file to be sent
     */
    public static void sendLogs(Activity activity, String intentChooserTitle, String subjectTitle,
            String bodyText, String[] emails, File file) {


        final Intent intent;

        //TODO Currently this only supports 1 file. The code commented would support multiple.
//        if (uris.size() == 1) {
        intent = new Intent(Intent.ACTION_SEND);
//        }
//        else {
//            intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
//            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//        }
//        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        intent.putExtra(Intent.EXTRA_SUBJECT, subjectTitle);

        // Add emails to show on to: field
        if (emails != null && emails.length > 0) {
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
        }

        // Add additional information to the email
        intent.putExtra(Intent.EXTRA_TEXT, bodyText);

        if (file != null) {
            Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName(), file);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType(activity.getContentResolver().getType(uri));
        } else {
            intent.setType("text/plain");
        }
        activity.startActivity(Intent.createChooser(intent, intentChooserTitle));
    }

    /**
     * Class to be able to share compressed LogFileAppender generated files
     *
     * @param activity           The activity
     * @param intentChooserTitle Intent chooser title
     * @param subjectTitle       Subject title (for email)
     * @param bodyText           Body text
     * @param emails             Emails to add on to: field (for email)
     */
    public static void sendLogsEmail(Activity activity, String intentChooserTitle, String subjectTitle,
                                     String bodyText, String[] emails) {

        File output = new File(getCompressedLogsPath(activity));
        if (!zipLogFiles(getFileLogPath(activity) + File.separator, output.getAbsolutePath()) || !output.exists()) {
            return;
        }

        sendLogs(activity, intentChooserTitle, subjectTitle, bodyText, emails, output);
    }

    /**
     * Returns the path where the application logs are being stored.
     *
     * @param context Application context
     *
     * @return path   The default path where the application logs are being stored
     *
     * @see AndroidUtils
     */
    public static String getFileLogPath(Context context) {
        return AndroidUtils.getFileDirPath(context, "");
    }

    private static String getCompressedLogsPath(Context context) {
        String path = getFileLogPath(context) + File.separator + FOLDER_LOGS;
        ensureFolderExists(path);

        return path + File.separator + FILE_LOG_ZIP;
    }

    private static void ensureFolderExists(String path) {
        new File(path).mkdirs();
    }

    private static boolean zipLogFiles(String source, String output) {
        try {
            FileOutputStream fos = new FileOutputStream(output);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(source);
            File[] files = srcFile.listFiles();

            LOG.d(TAG, "Compress directory: " + srcFile.getName() + " via zip.");
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }

                LOG.d(TAG, "Adding file: " + file.getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(file);
                zos.putNextEntry(new ZipEntry(file.getName()));

                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                zos.closeEntry();
                fis.close();
            }

            zos.close();
            return true;
        } catch (IOException ex) {
            LOG.e(TAG, "Unable to zip folder: " + ex.getMessage());
            return false;
        }
    }
}
