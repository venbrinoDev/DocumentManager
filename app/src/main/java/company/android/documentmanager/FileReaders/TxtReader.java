package company.android.documentmanager.FileReaders;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.shockwave.pdfium.PdfDocument.Bookmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import company.android.documentmanager.BottomAction.ActionBox;
import company.android.documentmanager.BottomAction.Actions;
import company.android.documentmanager.BottomAction.UtilityAction;
import company.android.documentmanager.R;
import company.android.documentmanager.TopAction.Finsihcallback;
import company.android.documentmanager.TopAction.PerformAction;

public class TxtReader extends AppCompatActivity implements OnPageChangeListener, Finsihcallback, OnLoadCompleteListener, OnPageErrorListener, OnPageScrollListener, Actions{

    public boolean errerToFileReading = false;
    private String fileName;
    public String filePath;
    private String intentAction;
    private TextView loading;
    MenuItem menu_item_other_app_file_opener;
    Integer pageNumber = Integer.valueOf(0);
    PDFView pdfView;
    private ProgressBar progressBar;
    Actions actions;
    @BindView(R.id.nightMode)
    ImageView nightMode;
    @BindView(R.id.share)ImageView share;
    @BindView(R.id.rotate)ImageView rotate;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheetLayout;
    @BindView(R.id.goTo)ImageView GotoPage;
    @BindView(R.id.print)ImageView print;
    @BindView(R.id.headerView)
    RelativeLayout headerView;
    @BindView(R.id.mainLayout)
    CoordinatorLayout mainlayout;

    boolean isJustLoaded ;
    UtilityAction utilityAction;
    PerformAction performAction;
    MenuItem bookmarkItem;
    BottomSheetBehavior<View> bottomSheetBehavior;
    AtomicInteger integer;
    AtomicInteger trackingInteger;
     boolean bookMarkLoaded=false;
    ActionBox viewState;
    String txtFile;

    public void onPageError(int i, Throwable th) {
    }

    public void onPageScrolled(int i, float f) {
    }

    AtomicBoolean isFileABookMark;
    AtomicBoolean isReady;

//    public void onStart() {
//        super.onStart();
//        AdsContttt.getInstance(this).showDirectCallInterstitialAds();
//    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_pdf_view);
        ButterKnife.bind(this);
        collectIntentData();
        setToolBar();
        initObject();
        showLoder();
//        readPDFFile("");
        new SaveToPDF().execute();
        actions = (Actions) this;
        onClickView();
 mainlayout.setOnClickListener(new OnClickListener() {
     @Override
     public void onClick(View v) {
         if (bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
             bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
         }
     }
 });
 TouchView();

    }




    private void onClickView() {

        nightMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.NightMode();



            }
        });
        share.setOnClickListener(v -> actions.Share());
        rotate.setOnClickListener(v -> {
            actions.Rotate();

        });


        GotoPage.setOnClickListener(v -> {

            actions.GotoPage();


        });

        print.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.Print();
            }
        });
    }
        private void initObject() {
        this.loading = (TextView) findViewById(R.id.loading);
        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        this.pdfView = (PDFView) findViewById(R.id.pdfView);
        this.utilityAction = new UtilityAction(TxtReader.this);
        isJustLoaded= true;
        performAction = new PerformAction(TxtReader.this,this);
            integer = new AtomicInteger(0);
            trackingInteger = new AtomicInteger(0);
            isFileABookMark = new AtomicBoolean(false);
            isReady = new AtomicBoolean(false);
            this.viewState= new ActionBox(false,false,0,false);
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void showLoder() {
        this.loading.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.pdfView.setVisibility(View.INVISIBLE);
    }


    public void hideLoder() {
        this.pdfView.setVisibility(View.VISIBLE);
        this.loading.setVisibility(View.INVISIBLE);
        this.progressBar.setVisibility(View.INVISIBLE);
        if (this.menu_item_other_app_file_opener != null) {
            if (this.intentAction.equals("a")) {
                this.menu_item_other_app_file_opener.setVisible(true);
            } else {
                this.menu_item_other_app_file_opener.setVisible(false);
            }
        }
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        if (!this.errerToFileReading) {
            this.fileName = FilesDataade.getFileName(this.txtFile);
            supportActionBar.setTitle((CharSequence) this.fileName);
        }
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TxtReader.this.finish();
            }
        });
    }

    private void collectIntentData() {
        if (getIntent().getExtras() != null) {
            String str = "filepath";
            if (getIntent().getExtras().containsKey(str)) {
                this.intentAction = getIntent().getAction();
                this.txtFile = getIntent().getExtras().getString(str);
                return;
            }
        }
        this.errerToFileReading = true;
    }


    public void readPDFFile(String str) {
        //pdfView.fromFile(new File(this.filePath)).swipeHorizontal(false).defaultPage(0).pageFitPolicy(FitPolicy.WIDTH).load();
        //pdfView.fromFile(new File(this.filePath)).spacing(4).swipeHorizontal(false).defaultPage(0).enableAnnotationRendering(true).load();

        Log.d("______",filePath);
        File f = new File(filePath);
        //this.pdfView.fromFile(f).load();

        int defaultPage = 0;

        if (viewState.isNumberChangeFromMe()){
            viewState.setNumberChangeFromMe(!viewState.isNumberChangeFromMe());
            defaultPage = viewState.getPageNumber();
        }else {
            defaultPage=this.pageNumber.intValue();
        }

        try {
            if (!this.errerToFileReading) {
                this.pdfView.setBackgroundColor(-3355444);
                this.pdfView.fromFile(f).spacing(4).password(str).onPageChange(this).onPageScroll(this).onLoad(this).enableAnnotationRendering(true).onError(new OnErrorListener() {
                    public void onError(Throwable th) {
                        String str = "Password required or incorrect password.";
                        if (th.getLocalizedMessage().toString().equals(str)) {
                            Toast.makeText(TxtReader.this, str, Toast.LENGTH_SHORT).show();
                            TxtReader.this.dialogPasswordAsk();
                            return;
                        }
                        TxtReader.this.errerToFileReading = true;
                        TxtReader.this.errorToReadingFile();
                    }
                }).swipeHorizontal(viewState.isScreenRotate()).scrollHandle(new DefaultScrollHandle(this)).nightMode(viewState.isNightMode()).defaultPage(defaultPage).load();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
//                        PdfViewActtt.this.hideLoder();
                    }
                }, 500);
                return;

            }



            errorToReadingFile();
        } catch (Exception unused) {
            this.errerToFileReading = true;
            errorToReadingFile();
        }





    }


    public void errorToReadingFile() {
        if (this.errerToFileReading) {
            dialogError();
        }
    }

    public void onPageChanged(int i, int i2) {
        this.pageNumber = Integer.valueOf(i);
        setTitle(String.format("%s %s / %s", new Object[]{this.fileName, Integer.valueOf(i + 1), Integer.valueOf(i2)}));
    }

    public void loadComplete(int i) {
        if (isJustLoaded){
            isJustLoaded=false;
            integer.set(i);
        }
        this.pdfView.getDocumentMeta();
        printBookmarksTree(this.pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<Bookmark> list, String str) {
        for (Bookmark bookmark : list) {
            if (bookmark.hasChildren()) {
                List children = bookmark.getChildren();
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("-");
                printBookmarksTree(children, sb.toString());
                Log.d("pdftree", "printBookmarksTree: "+sb.toString());
            }


        }


    }


    public void dialogPasswordAsk() {
        Builder builder = new Builder(this);
        builder.setTitle("This file is protected");
        View inflate = getLayoutInflater().inflate(R.layout.dialog_enter_password, null);
        builder.setView(inflate);
        final EditText editText = (EditText) inflate.findViewById(R.id.etPassword);
        editText.setInputType(128);
        editText.setTransformationMethod(new PasswordTransformationMethod());
        AlertDialog create = builder.create();
        create.setButton(-1, getString(R.string.okay), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                TxtReader.this.readPDFFile(editText.getText().toString());
            }
        });
        create.setButton(-2, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                TxtReader.this.finish();
            }
        });
        create.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                TxtReader.this.finish();
            }
        });
        create.show();
    }


    public void dialogError() {
        Builder builder = new Builder(this);
        builder.setTitle("Unable to open the document");
        builder.setMessage("An error occurred while opening the document.");
        builder.setIcon(R.drawable.text_file);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        if (this.intentAction.equals("a")) {
            builder.setPositiveButton("Open With", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    TxtReader dM_ActivityPDFView = TxtReader.this;
                    ConstantDataa.openTextDocument(dM_ActivityPDFView, dM_ActivityPDFView.txtFile);
                }
            });
        }
        builder.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                TxtReader.this.onBackPressed();
            }
        });
        builder.show();
    }

    @Override
    public void Print() {
        if (filePath !=null){
            utilityAction.print(filePath);
        }

    }

    @Override
    public void Rotate() {
viewState.setScreenRotate(!viewState.isScreenRotate());
        readPDFFile("");
    }

    @Override
    public void NightMode() {

        viewState.setNightMode(!viewState.isNightMode());
        changeDisplayLayout();
        readPDFFile("");
    }

    private void changeDisplayLayout() {
        if (viewState.isNightMode()){

            headerView.setBackground(ContextCompat.getDrawable(TxtReader.this,R.drawable.background_layout_dark));
            return;
        }
        headerView.setBackground(ContextCompat.getDrawable(TxtReader.this,R.drawable.background_layout));

    }

    @Override
    public void GotoPage() {

        if (integer.get() > 0 && trackingInteger.get() < integer.get()) {
            trackingInteger.set(trackingInteger.get() + 1);
            viewState.setPageNumber(trackingInteger.get());
            viewState.setNumberChangeFromMe(true);
            readPDFFile("");
        }
    }

    @Override
    public void Share() {
        if (filePath!=null){
            utilityAction.shareFile(filePath);
        }

    }

    @Override
    protected void onDestroy() {
        utilityAction.Destroy();
        performAction.destroy();
        super.onDestroy();

    }

    //    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_third_party_douments_open, menu);
//        this.menu_item_other_app_file_opener = menu.findItem(R.id.other_app_file_opener);
//        if (this.intentAction.equals("a")) {
//            this.menu_item_other_app_file_opener.setVisible(true);
//        } else {
//            this.menu_item_other_app_file_opener.setVisible(false);
//        }
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        if (menuItem.getItemId() != R.id.other_app_file_opener) {
//            return super.onOptionsItemSelected(menuItem);
//        }
//        if (VERSION.SDK_INT >= 24) {
//            try {
//                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        ConstantDataa.openPDFDocument(this, this.filePath);
//        return true;
//    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {



       MenuItem menuItem = menu.findItem(R.id.action_search);
       menuItem.setVisible(false);
       bookmarkItem = menu.findItem(R.id.action_bookmark);

        performAction.isBookMark(new File(txtFile));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detailmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_bookmark:
                if (isReady.get()){
                    if (isFileABookMark.get()){
                        isFileABookMark.set(false);
                        performAction.DeleteBookMark(new File(txtFile));
                        item.setIcon(ContextCompat.getDrawable(this,R.drawable.up_bookmark));
                    }else {
                        isFileABookMark.set(true);
                        performAction.bookMark(new File(txtFile));
                        item.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_bookmarkfile_white));
                    }

                }else{
                    Toast.makeText(this,"Try again",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.action_open:
                ConstantDataa.openTextDocument(TxtReader.this,txtFile);
                break;



        }

        return super.onOptionsItemSelected(item);
    }

    //BottomSheet stateChage Listener
    private void TouchView(){
        View view = findViewById(R.id.touchView);
        buttomSheetChange(view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    if (bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        view.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });
    }

    private void buttomSheetChange(View view){
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_EXPANDED:
                        view.setVisibility(View.VISIBLE);

                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public void finish(boolean find) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (!isReady.get()){
                    isReady.set(true);
                }
                if (find){
                    bookmarkItem.setIcon(ContextCompat.getDrawable(TxtReader.this,R.drawable.ic_bookmarkfile_white));
                }
                isFileABookMark.set(find);

            }
        });

    }
    class SaveToPDF extends AsyncTask<Void ,Integer,String> {
        File file=null;
ArrayList<String> stringArrayList ;

        public SaveToPDF() {
            this.stringArrayList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(TxtReader.this.txtFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();

            loop0:
            while (true) {
                int i = 0;
                while (true) {
                    String readLine = null;
                    try {
                        readLine = bufferedReader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (readLine == null) {
                        break loop0;
                    }
                    sb.append(readLine+"\n");
                    if (i == 100) {
                        break;
                    }
//                    sb.append(10);
                    i++;
                }
               stringArrayList.add(sb.toString());
                sb.setLength(0);
                return "";
            }
         stringArrayList.add(sb.toString());
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (stringArrayList.isEmpty()){
                return "";
            }

            try
            {
                String text = "";
                for (String fileText :stringArrayList){
                    text = fileText;
                }
                this.file = File.createTempFile("txtReaderTempo", ".pdf", TxtReader.this.getApplicationContext().getCacheDir());
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                document.addAuthor("Document Manager");
                document.addCreationDate();
                document.add(new Paragraph(text));

                document.close();

            }
            catch (FileNotFoundException e)
            {
                TxtReader.this.errerToFileReading = true;
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (DocumentException e)
            {
                TxtReader.this.errerToFileReading  = true;
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                TxtReader.this.errerToFileReading  = true;
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            hideLoder();
            super.onPostExecute(s);
            File file2 = this.file;
            if (file2 == null) {
                TxtReader.this.errerToFileReading = true;
            } else if (!file2.exists()) {
                TxtReader.this.errerToFileReading  = true;
            }
            if (!TxtReader.this.errerToFileReading ) {
                filePath = file2.getAbsolutePath();
                readPDFFile("");
            } else {
                TxtReader.this.errorToReadingFile();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
