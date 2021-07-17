package company.android.documentmanager.Utility;

public class Constant {
public static int GRID_LAYOUT = 2;
public static int LINEAR_LAYOUT = 1;
    public static  final String SHARED_PREF_USED_FIRST="userdata";

    public static final  String LAYOUT_VIEW= "layoutView";
    public static final  String LAYOUT_VIEW_FILEMANAGER= "layoutViewfilemanager";
    public static final  String LAYOUT_VIEW_FOLDERVIEW= "layoutViewfolderView";
    public static final String HOME="home";
    public static final String Others="others";
    public static  final String SHARED_PREF_USED_SETTING="setting";
    public static final String DISPLAY_FULL_PATH_KEY="DisplayFullPathKey";
    public static final String SAVE_SWITCH="save_switch";
    public static  final String SPLASH_SCREEN = "splash_screen";
    public static  final String GET_STARTED = "getstarted";



    public static String [] getAllfile(){
        String value [] = {"ppt","doc","pdf","txt","xls","xlsx","docx","pptx"};
        return value;
    }


}
