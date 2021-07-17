package company.android.documentmanager.MainActivities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import company.android.documentmanager.R;
import company.android.documentmanager.Utility.ReadDirection;
import company.android.documentmanager.Utility.RealPathUtil;
import company.android.documentmanager.office.fc.util.IOUtils;

public class ReaderDirection extends AppCompatActivity {

    AsyncTask<Uri,String,File> process ;
    ReadDirection gotoReadears;

    boolean errorFileOpening = false;
    String filePath ;
   Uri data;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader_direction);

        ImageView start = findViewById(R.id.imageView);
        Picasso.get().load(R.drawable.getstarted).into(start);
        gotoReadears = new ReadDirection(this);
         data= getIntent().getData();
         startProcessing();
    }
    private void startProcessing(){

        String path = RealPathUtil.getRealPath(this,data);
        if (path ==null){
            String name =getFileNameByUri(this,data);
            if (name==null){
                Toast.makeText(this,"error reading file",Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            File file = new File(getExternalFilesDir(null),name);
            this.filePath = file.getAbsolutePath();


            new process().execute();
        }else {
            gotoReadears.gotToActivity(path);
        }

    }

    private void retrieveFilePath(Uri uri,String filePath)
    {
        ContentResolver contentResolver = this.getContentResolver();
        InputStream inputStream=null;
                FileOutputStream fileOutputStream=null;
        try {
             inputStream =contentResolver.openInputStream(uri);
             fileOutputStream = new FileOutputStream(filePath);

            IOUtils.copy(inputStream,fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            errorFileOpening=true;
        }finally {
            if (inputStream !=null && fileOutputStream !=null){
                try {
                    inputStream.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String getFileNameByUri(Context context, Uri uri)
    {
        String fileName="unknown";//default fileName
        Uri filePathUri = uri;
        if (uri.getScheme().toString().compareTo("content")==0)
        {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst())
            {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                filePathUri = Uri.parse(cursor.getString(column_index));
                fileName = filePathUri.getLastPathSegment().toString();
            }
        }
        else if (uri.getScheme().compareTo("file")==0)
        {
            fileName = filePathUri.getLastPathSegment().toString();
        }
        else
        {
            fileName = fileName+"_"+filePathUri.getLastPathSegment();
        }
        return fileName;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        gotoReadears.Destroy();
    }


    private class process extends AsyncTask<Void,String,String>{

        @Override
        protected String doInBackground(Void... voids) {

            retrieveFilePath(data,filePath);
            return filePath;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
     if (s ==null){
        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
        }else{
    gotoReadears.gotToActivity(s);
     }
        }


    }
}