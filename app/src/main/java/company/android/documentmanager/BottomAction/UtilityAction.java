package company.android.documentmanager.BottomAction;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ShareCompat;

import java.io.File;
import java.net.URLConnection;

import company.android.documentmanager.Adapter.PdfDocumentAdapter;

public class UtilityAction {
    private Context context;


    public UtilityAction(Context context){
        this.context = context;

    }

    public void print(String filePath){
        //i added filePath here
        File file = new File(filePath);
        PrintManager printManager = (PrintManager)context.getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(context,file.getAbsolutePath());
            if (printManager !=null){
                printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
            }else {
                Toast.makeText(context,"Try again Now ",Toast.LENGTH_SHORT).show();
            }



        }catch (Exception e){
            Log.d("printing Error", "exception: "+e.getMessage());
        }


    }

    public void shareFile(String filePath){
        File file = new File(filePath);
        Uri uri=Uri.parse(file.getPath());
        ShareCompat.IntentBuilder.from((Activity) context).setStream(uri).setType(URLConnection.guessContentTypeFromName(file.getName())).startChooser();
    }


    public  void Destroy(){
        this.context = null;
    }
}
