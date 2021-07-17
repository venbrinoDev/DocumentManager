package company.android.documentmanager.Utility;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;

import company.android.documentmanager.Ads.FaceBooKAds;
import company.android.documentmanager.FileReaders.ExcelActivity;
import company.android.documentmanager.FileReaders.FilOperationns;
import company.android.documentmanager.FileReaders.PPTViewData;
import company.android.documentmanager.FileReaders.PdfViewActtt;
import company.android.documentmanager.FileReaders.RarExtractor;
import company.android.documentmanager.FileReaders.Texttt;
import company.android.documentmanager.FileReaders.WordViewAndroid;
import company.android.documentmanager.FileReaders.zipviewActttt;

public class ReadDirection {

    private Activity context;
    public ReadDirection(Activity context){

        this.context = context;
    }

    public void gotToActivity(String filePath){
        File file = new File(filePath);
        FaceBooKAds faceBooKAds = new FaceBooKAds(context);
        faceBooKAds.LoadInterstitial();

        String fileType = FilOperationns.fileExt(file.getAbsolutePath());
        switch (fileType){
            case  "pdf":
                Intent intent = new Intent(context, PdfViewActtt.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("filepath",file.getAbsolutePath());
                intent.setAction("a");
                context.startActivity(intent);
                context.finish();
                break;
            case "txt":

                Intent textIntent = new Intent(context, Texttt.class);
                textIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                textIntent.putExtra("filepath",""+file.getAbsolutePath());
                textIntent.setAction("a");
                context.startActivity(textIntent);
                context.finish();
                break;
            case "docx":
            case "doc":

                Intent docsIntent = new Intent(context, WordViewAndroid.class);
                docsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                docsIntent.putExtra("filepath",file.getAbsolutePath());
                docsIntent.setAction("a");
                context.startActivity(docsIntent);
                context.finish();
                break;
            case "pptx":
            case "ppt":
                Intent pptViewer = new Intent(context, PPTViewData.class);
                pptViewer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pptViewer.putExtra("filepath",file.getAbsolutePath());
                pptViewer.setAction("a");
                context.startActivity(pptViewer);
                context.finish();
                break;
            case "xlsx":
            case "xls":
                String str4 = "a";
                String str5 = "filepath";
                String str6 = "filename";
                String str = "";
                Intent intent6 = new Intent(context, ExcelActivity.class);
                intent6.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent6.putExtra(str6, file.getName());
                intent6.putExtra(str5, file.getAbsolutePath());
                intent6.putExtra("authority", str);
                intent6.putExtra("filetype", str);
                intent6.setAction(str4);
                context.startActivity(intent6);
                context.finish();
                break;
            case "rar":
                Intent rar = new Intent(context, RarExtractor.class );
                rar.putExtra("EXTRA_FILENAME",file.getName());
                rar.putExtra("EXTRA_PATH",file.getAbsolutePath());
                context.startActivity(rar);
                break;
            case "zip":
                Intent zip = new Intent(context,zipviewActttt.class );
                zip.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                zip.putExtra("EXTRA_FILENAME",file.getName());
                zip.putExtra("EXTRA_PATH",file.getAbsolutePath());
                zip.setAction("a");
                context.startActivity(zip);
                context.finish();
                break;

            default:
                Toast.makeText(context,"No file readers found",Toast.LENGTH_LONG).show();
                context.finish();
                break;
        }
    }

    public  Activity CheckActivity(){
        return context;
    }
    public   void Destroy(){
        this.context=null;

    }
}
