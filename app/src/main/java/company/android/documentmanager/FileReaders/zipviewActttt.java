package company.android.documentmanager.FileReaders;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import company.android.documentmanager.R;

public class zipviewActttt extends AppCompatActivity {
    public static ArrayList<String> childParentDirectory;

    public String CACHEDIR;
    private String CACHEDIRPATH;
    private String TAG;
    AsyncTask<Integer, Integer, String> ZipExtractor;
    public File compress_file;
    String file_name;
    private boolean isZipFile = true;
    public boolean mBackPressed;
    public ProgressDialog progressDialog;
    AsyncTask<Integer, Integer, String> rarExtractor;
    class RarExtractor extends AsyncTask<Integer, Integer, String> {

        public void onProgressUpdate(Integer... numArr) {
        }

        RarExtractor() {
        }


        public String doInBackground(Integer... numArr) {
            zipviewActttt.this.createCachFolder();
            try {
                String file = zipviewActttt.this.compress_file.toString();
                String access$200 = zipviewActttt.this.CACHEDIR;
                zipviewActttt.this.extractArchive(file, access$200);
            } catch (Exception unused) {
            }
            return "Task Completed.";
        }


        public void onPostExecute(String str) {
            zipviewActttt.this.progressDialog.setProgress(100);
            zipviewActttt.this.progressDialog.dismiss();
            File[] listFiles = new File(zipviewActttt.this.CACHEDIR).listFiles();
            if (listFiles == null || listFiles.length == 0) {
                Toast.makeText(zipviewActttt.this, "Bad rar archive", Toast.LENGTH_SHORT).show();
                zipviewActttt.this.finish();
            } else {
                FragmentTransaction beginTransaction = zipviewActttt.this.getSupportFragmentManager().beginTransaction();
                FileExplodataa dM_FragmentFileExplore = new FileExplodataa();
                Bundle bundle = new Bundle();
                zipviewActttt.childParentDirectory.add(zipviewActttt.this.CACHEDIR);
                bundle.putString("PATH", zipviewActttt.this.CACHEDIR);
                dM_FragmentFileExplore.setArguments(bundle);
                beginTransaction.replace(R.id.fragment, dM_FragmentFileExplore);
                beginTransaction.commit();
            }
        }


        public void onPreExecute() {
            zipviewActttt.this.progressDialog.show();
        }
    }

    class ZipExtractor extends AsyncTask<Integer, Integer, String> {

        public void onProgressUpdate(Integer... numArr) {
        }

        ZipExtractor() {
        }


        public String doInBackground(Integer... numArr) {
            zipviewActttt.this.createCachFolder();
            try {
                String file = zipviewActttt.this.compress_file.toString();
                String access$200 = zipviewActttt.this.CACHEDIR;
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
                int size = new ZipFile(file).size();
                int i = 0;
                do {
                    ZipEntry nextEntry = zipInputStream.getNextEntry();
                    if (nextEntry == null) {
                        break;
                    }
                    zipviewActttt.this.progressDialog.setProgress((i * 100) / size);
                    if (nextEntry.isDirectory()) {
                        zipviewActttt.this._dirChecker(access$200, nextEntry.getName());
                    } else {
                        File file2 = new File(nextEntry.getName());
                        StringBuilder sb = new StringBuilder();
                        sb.append(access$200);
                        sb.append(File.separator);
                        sb.append(file2);
                        File file3 = new File(sb.toString());
                        PrintStream printStream = System.out;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("file unzip : ");
                        sb2.append(file3.getAbsoluteFile());
                        printStream.println(sb2.toString());
                        if (!new File(file3.getParent()).exists()) {
                            new File(file3.getParent()).mkdirs();
                        }
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(access$200);
                        sb3.append(nextEntry.getName());
                        FileOutputStream fileOutputStream = new FileOutputStream(sb3.toString());
                        byte[] bArr = new byte[1024];
                        do {
                            int read = zipInputStream.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        } while (!isCancelled());
                        zipInputStream.closeEntry();
                        fileOutputStream.close();
                    }
                    i++;
                } while (!isCancelled());
                zipInputStream.close();
            } catch (Exception unused) {
            }
            return "Task Completed.";
        }


        public void onPostExecute(String str) {
            zipviewActttt.this.progressDialog.setProgress(100);
            zipviewActttt.this.progressDialog.dismiss();
            if (!isCancelled()) {
                File[] listFiles = new File(zipviewActttt.this.CACHEDIR).listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    Toast.makeText(zipviewActttt.this, "Bad zip archive", Toast.LENGTH_SHORT).show();
                    zipviewActttt.this.finish();
                } else {
                    FragmentTransaction beginTransaction = zipviewActttt.this.getSupportFragmentManager().beginTransaction();
                    FileExplodataa dM_FragmentFileExplore = new FileExplodataa();
                    Bundle bundle = new Bundle();
                    zipviewActttt.childParentDirectory.add(zipviewActttt.this.CACHEDIR);
                    bundle.putString("PATH", zipviewActttt.this.CACHEDIR);
                    dM_FragmentFileExplore.setArguments(bundle);
                    beginTransaction.replace(R.id.fragment, dM_FragmentFileExplore);
                    beginTransaction.commit();
                }
            }
        }


        public void onPreExecute() {
            zipviewActttt.this.progressDialog.setProgressStyle(1);
            zipviewActttt.this.progressDialog.show();
        }
    }

    public zipviewActttt() {
        String str = "";
        this.CACHEDIRPATH = str;
        this.CACHEDIR = ".temp/";
        this.TAG = "DM_ActivityZIPView";
        this.file_name = str;
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                zipviewActttt.this.finish();
            }
        });
        StringBuilder sb = new StringBuilder();

        sb.append(Environment.getExternalStorageDirectory());
        sb.append("/Android/data/");
        sb.append(getApplicationContext().getPackageName());
        sb.append("/");
        this.CACHEDIRPATH = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.CACHEDIRPATH);
        sb2.append(this.CACHEDIR);
        this.CACHEDIR = sb2.toString();
       new Thread(() -> FilOperationns.deleteTemp(getApplicationContext(), zipviewActttt.this.CACHEDIR)).start();

        createCachFolder();
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Unzipping File");
        this.progressDialog.setCancelable(true);
        this.progressDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                if (zipviewActttt.this.mBackPressed) {
                    zipviewActttt.this.mBackPressed = false;
                    zipviewActttt.this.onBackPressed();
                }
            }
        });
        this.progressDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == 4) {
                    zipviewActttt.this.mBackPressed = true;
                }
                return false;
            }
        });
        childParentDirectory = new ArrayList<>();
        Intent intent = getIntent();
        String str2 = "EXTRA_PATH";
        if (intent.hasExtra(str2) && intent.hasExtra(str)) {
            Bundle extras = getIntent().getExtras();
            if (!extras.getString(str2).equals(null)) {
                this.compress_file = new File(getIntent().getStringExtra(str2));
            }
            if (!extras.getString(str).equals(null)) {
                supportActionBar.setTitle((CharSequence) getIntent().getStringExtra(str));
            }
        }
        long length = (this.compress_file.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        if ((((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks())) / 1 << 20 <= length * 2) {
            showOutOfMemoryErrorDialog();
        } else if (FilOperationns.fileExt(this.compress_file.getName()).toLowerCase().equals("zip")) {
            this.ZipExtractor = new ZipExtractor().execute(new Integer[0]);
        } else if (FilOperationns.fileExt(this.compress_file.getName()).toLowerCase().equals("rar")) {
            this.rarExtractor = new RarExtractor().execute(new Integer[0]);
        } else {
            Toast.makeText(this, "Not a valid format", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOutOfMemoryErrorDialog() {
        AlertDialog create = new Builder(this).create();
        create.setTitle("Sorry!!");
        create.setMessage("Out of memory.");
        create.setIcon(R.mipmap.ic_launcher);
        create.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                zipviewActttt.this.finish();
            }
        });
        create.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                zipviewActttt.this.finish();
            }
        });
        create.show();
    }



    public void createCachFolder() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append("/Android/data/");
        sb.append(getApplicationContext().getPackageName());
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(this.CACHEDIR);
        if (!file2.exists()) {
            file2.mkdir();
        }
    }


    public void _dirChecker(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        File file = new File(sb.toString());
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    public void extractArchive(File file, File file2) {
       Toast t= Toast.makeText(this,"error",Toast.LENGTH_SHORT);
        Archive archive;
        try {

            archive = new Archive(file);
        } catch (RarException | IOException unused) {
            archive = null;
        }
        //&& !archive.isEncrypted(
        if (archive != null) {
            while (true) {
                FileHeader nextFileHeader = archive.nextFileHeader();
                if (nextFileHeader != null) {
                    if (!nextFileHeader.isEncrypted()) {
                        try {
                            if (nextFileHeader.isDirectory()) {
                                createDirectory(nextFileHeader, file2);
                            } else {
                                FileOutputStream fileOutputStream = new FileOutputStream(createFile(nextFileHeader, file2));
                                archive.extractFile(nextFileHeader, fileOutputStream);
                                fileOutputStream.close();
                            }
                        } catch (RarException | IOException unused2) {
                        }
                        AsyncTask<Integer, Integer, String> asyncTask = this.rarExtractor;
                        if (asyncTask != null && asyncTask.isCancelled()) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
        }
    }

    private static void createDirectory(FileHeader fileHeader, File file) {
        if (!fileHeader.isDirectory() || !fileHeader.isUnicode()) {
            if (fileHeader.isDirectory() && !fileHeader.isUnicode() && !new File(file, fileHeader.getFileNameString()).exists()) {
                makeDirectory(file, fileHeader.getFileNameString());
            }
        } else if (!new File(file, fileHeader.getFileNameW()).exists()) {
            makeDirectory(file, fileHeader.getFileNameW());
        }
    }

    private static void makeDirectory(File file, String str) {
        String[] split = str.split("\\\\");
        if (split != null) {
            String str2 = "";
            for (String str3 : split) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(File.separator);
                sb.append(str3);
                str2 = sb.toString();
                new File(file, str2).mkdir();
            }
        }
    }

    private static File createFile(FileHeader fileHeader, File file) {
        String str;
        if (!fileHeader.isFileHeader() || !fileHeader.isUnicode()) {
            str = fileHeader.getFileNameString();
        } else {
            str = fileHeader.getFileNameW();
        }
        File file2 = new File(file, str);
        if (file2.exists()) {
            return file2;
        }
        try {
            return makeFile(file, str);
        } catch (IOException unused) {
            return file2;
        }
    }

    private static File makeFile(File file, String str) throws IOException {
        String[] split = str.split("\\\\");
        if (split == null) {
            return null;
        }
        int length = split.length;
        if (length == 1) {
            return new File(file, str);
        }
        if (length <= 1) {
            return null;
        }
        String str2 = "";
        for (int i = 0; i < split.length - 1; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(File.separator);
            sb.append(split[i]);
            str2 = sb.toString();
            new File(file, str2).mkdir();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        sb2.append(File.separator);
        sb2.append(split[split.length - 1]);
        File file2 = new File(file, sb2.toString());
        file2.createNewFile();
        return file2;
    }

    public void extractArchive(String str, String str2) {
        Toast t = Toast.makeText(this,"error",Toast.LENGTH_SHORT);
        if (str == null || str2 == null) {
            t.setText("null error");
            t.show();
            throw new RuntimeException("archive and destination must me set");

        }
        File file = new File(str);
        if (file.exists()) {
            File file2 = new File(str2);
            if (!file2.exists() || !file2.isDirectory()) {
                StringBuilder sb = new StringBuilder();
                sb.append("the destination must exist and point to a directory: ");
                sb.append(str2);
                t.setText("runtiime");
                t.show();
                throw new RuntimeException(sb.toString());

            }
            extractArchive(file, file2);
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("the archive does not exit: ");
        sb2.append(str);
        throw new RuntimeException(sb2.toString());
    }

    public void onBackPressed() {
        AsyncTask<Integer, Integer, String> asyncTask = this.ZipExtractor;
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        AsyncTask<Integer, Integer, String> asyncTask2 = this.rarExtractor;
        if (asyncTask2 != null) {
            asyncTask2.cancel(true);
        }
        FilOperationns.deleteTemp(getApplicationContext(), this.CACHEDIR);
        super.onBackPressed();
    }
}
