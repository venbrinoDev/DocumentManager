package company.android.documentmanager.FileReaders;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.webkit.MimeTypeMap;
import java.io.File;

public class FilOperationns {
    public static String fileExt(String str) {
        try {
            int lastIndexOf = str.lastIndexOf(".");
            if (lastIndexOf > 0) {
                return str.substring(lastIndexOf + 1, str.length());
            }
        } catch (Exception unused) {
        }
        return "";
    }

    public static String getMimeType(String str) {
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        if (fileExtensionFromUrl != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl);
        }
        return null;
    }

    public static void deleteTemp(Context context, String str) {
        File file = new File(str);
        deleteRecursive(file);
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);
    }

    static void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File deleteRecursive : file.listFiles()) {
                deleteRecursive(deleteRecursive);
            }
        }
        file.delete();
    }
}