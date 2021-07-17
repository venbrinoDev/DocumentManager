package company.android.documentmanager.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfDocumentAdapter extends PrintDocumentAdapter {
    String path;
    Context context;

    public PdfDocumentAdapter(Context context, String s) {
        this.path=s;
        this.context = context;

    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        if (cancellationSignal.isCanceled()){
            callback.onLayoutCancelled();
        }else {

            PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("file name");
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build();

            callback.onLayoutFinished(builder.build(),newAttributes.equals(oldAttributes));

        }


    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File file = new File(path);
            inputStream = new FileInputStream(file);
            outputStream = new FileOutputStream(destination.getFileDescriptor());

            byte [] buff = new byte [16384];

            int size;
            while ((size = inputStream.read(buff))>=0&&!cancellationSignal.isCanceled()){

                outputStream.write(buff,0,size);

            }

            if (cancellationSignal.isCanceled()){
                callback.onWriteCancelled();;
            }else {
                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            }

        } catch (Exception e) {
            callback.onWriteFailed(e.getMessage());
            Log.d("PDfreader", "onWrite: "+e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
