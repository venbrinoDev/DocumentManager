package company.android.documentmanager.LauncherScreen;

public class Slide {
    private int image;
    private String text;
    public Slide(){

    }

    public Slide(int images, String text){
        this.image = images;
        this.text =text;

    }

    public void setImages(int images){
        this.image = images;
    }

    public  void setText(String text){
        this.text = text;
    }
    public int getImage(){
        return  image;
    }

    public String getText(){
        return  text;
    }
}

