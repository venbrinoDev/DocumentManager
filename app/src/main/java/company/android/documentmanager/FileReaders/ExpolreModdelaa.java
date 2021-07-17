package company.android.documentmanager.FileReaders;

import java.io.File;

public class ExpolreModdelaa {
    private String extension;
    private String fileName;
    private String fileThumb;
    private String path;
    private long size;

    public ExpolreModdelaa(File file) {
        setPath(file.getAbsolutePath());
        setFileName(file.getName());
        setFileThumb("");
        setSize((long) Integer.parseInt(String.valueOf(file.length())));
        if (file.isFile()) {
            setExtension(FilOperationns.fileExt(file.getName()));
        } else {
            setExtension("");
        }
    }

    public ExpolreModdelaa(String str, String str2, String str3, long j) {
        this.path = str;
        this.fileName = str2;
        this.fileThumb = str3;
        this.size = j;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public String getFileThumb() {
        return this.fileThumb;
    }

    public void setFileThumb(String str) {
        this.fileThumb = str;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String str) {
        this.extension = str;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }
}