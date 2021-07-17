package company.android.documentmanager.FileReaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesDataade {
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final int EOF = -1;

    private FilesDataade() {
    }

    public static String getFileName(String str) {
        return str.substring(str.lastIndexOf("/") + 1);
    }

    public static String getFileExt(String str) {
        return str.substring(str.lastIndexOf("/") + 1).toLowerCase();
    }

    public static File from(Context context, Uri uri) throws IOException {
        String str = "";
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            String fileName = getFileName(context, uri);
            String[] splitFileName = splitFileName(fileName);
            if (splitFileName[0].length() == 0) {
                return new File(str);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("ac");
            sb.append(splitFileName[0]);
            File rename = rename(File.createTempFile(sb.toString(), splitFileName[1]), fileName);
            rename.deleteOnExit();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(rename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (openInputStream != null) {
                copy(openInputStream, fileOutputStream);
                openInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            return rename;
        } catch (NullPointerException unused) {
            return new File(str);
        } catch (RuntimeException unused2) {
            return new File(str);
        }
    }

    private static String[] splitFileName(String str) {
        String str2;
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf != -1) {
            String substring = str.substring(0, lastIndexOf);
            str2 = str.substring(lastIndexOf);
            str = substring;
        } else {
            str2 = "";
        }
        return new String[]{str, str2};
    }

    private static String getFileName(Context context, Uri uri) {
        String str = null;
        if (uri.getScheme().equals("content")) {
            Cursor query = context.getContentResolver().query(uri, null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        str = query.getString(query.getColumnIndex("_display_name"));
                    }
                } catch (Throwable th) {
                    query.close();
                    throw th;
                }
            }
            query.close();
        }
        if (str != null) {
            return str;
        }
        String path = uri.getPath();
        int lastIndexOf = path.lastIndexOf(47);
        return lastIndexOf != -1 ? path.substring(lastIndexOf + 1) : path;
    }

    private static File rename(File file, String str) {
        File file2 = new File(file.getParent(), str);
        if (!file2.equals(file)) {
            if (file2.exists()) {
                file2.delete();
            }
            file.renameTo(file2);
        }
        return file2;
    }

    private static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (-1 == read) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }
}
