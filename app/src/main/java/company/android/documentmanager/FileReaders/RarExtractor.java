package company.android.documentmanager.FileReaders;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import company.android.documentmanager.R;

public class RarExtractor  extends AppCompatActivity {
private String filePath;
private String CACHE_DIR;
private  String EXTENSION="temporary";
private String file_name;
ProgressDialog progressDialog;
private boolean mBackPressed =false;

AsyncTask<Integer, Integer, String> rarExtractor;
    public static ArrayList<String> childParentDirectory;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_dm_zipview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String str = "EXTRA_FILENAME";
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(str)) {
            this.file_name = getIntent().getExtras().getString(str);
        }
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle((CharSequence) this.file_name);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });


        new Thread(() -> FilOperationns.deleteTemp(getApplicationContext(), suspectedFolderDir())).start();

        childParentDirectory = new ArrayList<>();

        collectFilePath();
        setUpProgressDialog();

        if (filePath !=null){
            rarExtractor = new Rar().execute();
        }
    }


    private void setUpProgressDialog(){
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Unzipping File");
        this.progressDialog.setCancelable(true);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                if (mBackPressed) {
                    mBackPressed = false;
                  onBackPressed();
                }
            }
        });
        this.progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == 4) {
                   mBackPressed = true;
                }
                return false;
            }
        });
    }

    private void extractzip(String filePath){
        File rar = new File(filePath);
        File destination = new File(this.CACHE_DIR);
        try {
            Junrar.extract(rar, destination);
        } catch (RarException |IOException e) {
            e.printStackTrace();
        }
    }

    private void collectFilePath(){
        String str2 = "EXTRA_PATH";
        if(getIntent().hasExtra(str2)){
            if (getIntent().getStringExtra(str2) !=null){
                this.filePath = getIntent().getStringExtra(str2);
            }
        }
    }

    private void createCacheDir() {
        File file =new File(getExternalFilesDir(null),EXTENSION);
        if (!file.exists()){
            file.mkdirs();
        }
        this.CACHE_DIR = file.getAbsolutePath();
    }

    private String suspectedFolderDir(){
        File file =new File(getExternalFilesDir(null),EXTENSION);
        return file.getAbsolutePath();
    }

    class  Rar extends AsyncTask<Integer, Integer, String> {

        public void onProgressUpdate(Integer... numArr) {
        }

        Rar() {
        }


        public String doInBackground(Integer... numArr) {
            createCacheDir();
            extractzip(filePath);
            return "Task Completed.";
        }


        public void onPostExecute(String str) {
            progressDialog.dismiss();
            File[] listFiles = new File(CACHE_DIR).listFiles();
            if (listFiles == null || listFiles.length == 0) {
                Toast.makeText(company.android.documentmanager.FileReaders.RarExtractor.this, "Bad rar archive", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
                rarExploadatta dM_FragmentFileExplore = new rarExploadatta();
                Bundle bundle = new Bundle();
               childParentDirectory.add(CACHE_DIR);
                bundle.putString("PATH", CACHE_DIR);
                dM_FragmentFileExplore.setArguments(bundle);
                beginTransaction.replace(R.id.fragment, dM_FragmentFileExplore);
                beginTransaction.commit();
            }
        }

        public void onPreExecute() {
            progressDialog.show();
        }

    }



    @Override
    public void onBackPressed() {
        AsyncTask<Integer, Integer, String> asyncTask2 = this.rarExtractor;
        if (asyncTask2 != null) {
            asyncTask2.cancel(true);
        }
            FilOperationns.deleteTemp(getApplicationContext(), suspectedFolderDir());


        super.onBackPressed();
    }


}
