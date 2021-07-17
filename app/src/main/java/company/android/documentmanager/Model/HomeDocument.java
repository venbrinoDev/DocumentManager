package company.android.documentmanager.Model;

import android.widget.ImageView;

import java.io.File;

public class HomeDocument {

    private int  FileImageView;
    private String FileText;
    private String FileNumber;
    public String filePath;
    public File file;

    public HomeDocument(int fileImageView, String fileText, String fileNumber, String filePath, File file) {
        FileImageView = fileImageView;
        FileText = fileText;
        FileNumber = fileNumber;
        this.filePath = filePath;
        this.file = file;
    }

    public int getFileImageView() {
        return FileImageView;
    }

    public void setFileImageView(int fileImageView) {
        FileImageView = fileImageView;
    }

    public String getFileText() {
        return FileText;
    }

    public void setFileText(String fileText) {
        FileText = fileText;
    }

    public String getFileNumber() {
        return FileNumber;
    }

    public void setFileNumber(String fileNumber) {
        FileNumber = fileNumber;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
