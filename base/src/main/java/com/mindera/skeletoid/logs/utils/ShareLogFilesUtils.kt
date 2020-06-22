package com.mindera.skeletoid.logs.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.mindera.skeletoid.generic.AndroidUtils
import com.mindera.skeletoid.logs.LOG
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * A way to share log file easily
 *
 *
 * Remember to add this to the Android Manifest of the App
 *
 *
 * <provider android:authorities="${applicationId}" android:exported="false" android:grantUriPermissions="true">
 * <meta-data android:name="android.support.FILE_PROVIDER_PATHS" android:resource="@xml/fileprovider"></meta-data>
</provider> *
 *
 *
 * and add the file fileprovider.xml to the resources with
 *
 *
 *
 * <paths>
 * <files-path path="." name="logs"></files-path>
</paths> *
 */
object ShareLogFilesUtils {

    private const val TAG = "ShareLogFilesUtils"
    private const val FOLDER_LOGS = "logs"
    private const val FILE_LOG_ZIP = "logs.zip"

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
    fun sendLogs(
        activity: Activity,
        intentChooserTitle: String,
        subjectTitle: String,
        bodyText: String,
        emails: Array<String>?,
        file: File
    ) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_SUBJECT, subjectTitle)

        // Add emails to show on to: field
        if (!emails.isNullOrEmpty()) {
            intent.putExtra(Intent.EXTRA_EMAIL, emails)
        }

        // Add additional information to the email
        intent.putExtra(Intent.EXTRA_TEXT, bodyText)
        val uri = FileProvider.getUriForFile(
            activity,
            activity.packageName,
            file
        )
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = activity.contentResolver.getType(uri)
        activity.startActivity(Intent.createChooser(intent, intentChooserTitle))
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
    fun sendLogsEmail(
        activity: Activity,
        intentChooserTitle: String,
        subjectTitle: String,
        bodyText: String,
        emails: Array<String>?
    ) {
        val output = File(getCompressedLogsPath(activity))
        if (!zipLogFiles(getFileLogPath(activity) + File.separator, output.absolutePath) || !output.exists()) {
            return
        }
        sendLogs(activity, intentChooserTitle, subjectTitle, bodyText, emails, output)
    }

    /**
     * Returns the path where the application logs are being stored.
     *
     * @param context Application context
     * @return path   The default path where the application logs are being stored
     * @see AndroidUtils
     */
    fun getFileLogPath(context: Context): String {
        return AndroidUtils.getFileDirPath(context, "")
    }

    private fun getCompressedLogsPath(context: Context): String {
        val path = getFileLogPath(context) + File.separator + FOLDER_LOGS
        ensureFolderExists(path)
        return path + File.separator + FILE_LOG_ZIP
    }

    private fun ensureFolderExists(path: String) {
        File(path).mkdirs()
    }

    private fun zipLogFiles(source: String, output: String): Boolean {
        var fos: FileOutputStream? = null
        var zos: ZipOutputStream? = null
        var fis: FileInputStream? = null
        return try {
            fos = FileOutputStream(output)
            zos = ZipOutputStream(fos)
            val srcFile = File(source)
            val files = srcFile.listFiles()
            LOG.d(TAG, "Compress directory: " + srcFile.name + " via zip.")
            for (file in files) {
                if (file.isDirectory) {
                    continue
                }
                LOG.d(TAG, "Adding file: " + file.name)
                val buffer = ByteArray(1024)
                fis = FileInputStream(file)
                zos.putNextEntry(ZipEntry(file.name))
                var length: Int
                while (fis.read(buffer).also { length = it } > 0) {
                    zos.write(buffer, 0, length)
                }
                zos.closeEntry()
            }
            true
        } catch (ex: IOException) {
            LOG.e(TAG, "Unable to zip folder: " + ex.message)
            false
        } finally {
            try {
                fis?.close()
            } catch (e: Exception) {
                LOG.e(TAG, e, "Unable to close FileInputStream")
            }
            try {
                fos?.close()
            } catch (e: Exception) {
                LOG.e(TAG, e, "Unable to close FileOutputStream")
            }
            try {
                zos?.close()
            } catch (e: Exception) {
                LOG.e(TAG, e, "Unable to close ZipOutputStream")
            }
        }
    }

}