package company.android.documentmanager.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefrences {



    private SharedPreferences sharedPreferences;

    public AppPrefrences(Context context,String filename) {
        this.sharedPreferences = context.getSharedPreferences(filename,Context.MODE_PRIVATE);
    }
    public String getString(String key,String defvalue){
        return sharedPreferences.getString(key,defvalue);
    }
    public int getInt(String key,int  defvalue)
    {
        return sharedPreferences.getInt(key,defvalue);
    }

    public boolean getBoolean(String key,boolean defValue)
    {
        return sharedPreferences.getBoolean(key,defValue);
    }
    public void putBoolean(String key,boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public void putString(String key,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public void putInt(String key,int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }


    public void clear(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
