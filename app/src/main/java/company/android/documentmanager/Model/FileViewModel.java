package company.android.documentmanager.Model;

import java.io.File;
import java.util.Comparator;

public class FileViewModel {

    private File file;
    private long Size;
    private String name;
    private long Date;

    public FileViewModel(File file, long size, String name, long date) {
        this.file = file;
        Size = size;
        this.name = name;
        Date = date;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }


    public static Comparator<FileViewModel>  compareFileSize = (o1, o2) -> {
        long size1 =o1.getSize();
        long size2 = o2.getSize();
        return Long.compare(size1,size2);
    };

    public static Comparator<FileViewModel>  compareDate = (o1, o2) -> {
        long size1 = o1.getDate();
        long size2 = o2.getDate();
        return Long.compare(size1,size2);
    };

    public static Comparator<FileViewModel>  compareName = (o1, o2) -> {
        String size1 =o1.getName().toUpperCase();
        String size2 = o2.getName().toUpperCase();
        return size1.compareTo(size2);
    };
}
