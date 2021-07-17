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

public class GotoReadears {

    private Activity context;
    public GotoReadears(Activity context){

        this.context = context;
    }

    public void gotToActivity(String filePath){
        File file = new File(filePath);
        FaceBooKAds faceBooKAds = new FaceBooKAds(context);
        faceBooKAds.LoadInterstitial();
//        String fileType = filePath.substring(filePath.length()-3);
        String fileType = FilOperationns.fileExt(filePath);

        switch (fileType){
            case  "pdf":
                Intent intent = new Intent(context, PdfViewActtt.class);
                intent.putExtra("filepath",file.getAbsolutePath());
                intent.setAction("a");
                context.startActivity(intent);
                break;
            case "txt":
                Intent textIntent = new Intent(context, Texttt.class);
                textIntent.putExtra("filepath",""+file.getAbsolutePath());
                textIntent.setAction("a");
                context.startActivity(textIntent);
                break;
            case "docx":
            case "doc":
                Intent docsIntent = new Intent(context, WordViewAndroid.class);
                docsIntent.putExtra("filepath",file.getAbsolutePath());
                docsIntent.setAction("a");
                context.startActivity(docsIntent);
                break;
            case "ppt":
            case"pptx":
                Intent pptViewer = new Intent(context, PPTViewData.class);
                pptViewer.putExtra("filepath",file.getAbsolutePath());
                pptViewer.setAction("a");
                context.startActivity(pptViewer);
                break;
            case "xlsx":
            case "xls":
                String str4 = "a";
                String str5 = "filepath";
                String str6 = "filename";
                String str = "";
                Intent intent6 = new Intent(context, ExcelActivity.class);
                intent6.putExtra(str6, file.getName());
                intent6.putExtra(str5, file.getAbsolutePath());
                intent6.putExtra("authority", str);
                intent6.putExtra("filetype", str);
                intent6.setAction(str4);
                context.startActivity(intent6);
                break;
            case "rar":
                Intent rar = new Intent(context, RarExtractor.class );
                rar.putExtra("EXTRA_FILENAME",file.getName());
                rar.putExtra("EXTRA_PATH",file.getAbsolutePath());
                context.startActivity(rar);
                break;

            case "zip":
                Intent zip = new Intent(context,zipviewActttt.class );
                zip.putExtra("EXTRA_FILENAME",file.getName());
                zip.putExtra("EXTRA_PATH",file.getAbsolutePath());
                zip.setAction("a");
                context.startActivity(zip);
                break;

            default:
                Toast.makeText(context,"no Reader available",Toast.LENGTH_LONG).show();
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
