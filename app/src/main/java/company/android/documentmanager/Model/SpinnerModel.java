package company.android.documentmanager.Model;


public class SpinnerModel  {

    private String text;
    private int Image;
    private String fileType;


    public SpinnerModel(String text, int image, String fileType) {
        this.text = text;
        Image = image;
        this.fileType = fileType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
