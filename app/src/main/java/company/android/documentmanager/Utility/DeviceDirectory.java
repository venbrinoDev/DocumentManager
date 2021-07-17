package company.android.documentmanager.Utility;

import android.content.Context;

import java.io.File;

public class DeviceDirectory {

    private  Context context;

    public DeviceDirectory(Context context) {
        this.context = context;
    }


    public  String getDeviceHomeStorage(){
        File dir =context.getExternalFilesDir(null);
        String path = dir.getAbsolutePath();
        int index = path.indexOf("/Android/data/");
       return path.substring(0,index);
    }

    public  String getAppSpecificStorage(){
        File dir =context.getExternalFilesDir(null);
       return dir.getAbsolutePath();
    }
}
