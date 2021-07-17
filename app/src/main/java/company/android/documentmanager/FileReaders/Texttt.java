package company.android.documentmanager.FileReaders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import company.android.documentmanager.BottomAction.ActionBox;
import company.android.documentmanager.BottomAction.Actions;
import company.android.documentmanager.BottomAction.UtilityAction;
import company.android.documentmanager.BottomAction.viewPosition;
import company.android.documentmanager.R;
import company.android.documentmanager.TopAction.Finsihcallback;
import company.android.documentmanager.TopAction.PerformAction;

public class Texttt extends AppCompatActivity implements Actions , Finsihcallback,viewPosition {
    DataAdapter adapter;

    public boolean errerToFileReading = false;
    private String fileName;
    public String filePath;
    private String intentAction;
    private TextView loading;
    MenuItem menu_item_other_app_file_opener;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    public ArrayList<String> stringArrayList;
    public AtomicInteger displayTextMode;

    public boolean errorSavingFileTxt = false;
    public File file;

    @BindView(R.id.rootLayout)
    CoordinatorLayout layout;
    @BindView(R.id.nightMode)
    ImageView nightMode;
    @BindView(R.id.share)ImageView share;
    @BindView(R.id.rotate)ImageView rotate;
    @BindView(R.id.bottom_sheet)
    LinearLayout bottomSheetLayout;
    @BindView(R.id.goTo)ImageView GotoPage;
    @BindView(R.id.print)ImageView print;
    @BindView(R.id.headerView)
    RelativeLayout headerView;
    Actions actions;

    UtilityAction utilityAction;
    PerformAction performAction;
    MenuItem bookmarkItem;
    BottomSheetBehavior<View> bottomSheetBehavior;

    AtomicBoolean isFileABookMark;
    AtomicBoolean isReady;

    ActionBox viewState;

    AtomicBoolean showHighLight;
    ArrayList<Integer> words;
  SaveToPDF saveToPdf;


    @Override
    public void Print() {
new SaveToPDF(this).execute();
    }

    @Override
    public void Rotate() {
        if (viewState.isScreenRotate()){
            viewState.setScreenRotate(false);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }else {
            viewState.setScreenRotate(true);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }


    }

    @Override
    public void NightMode() {
        if (displayTextMode.get()==1){
            displayTextMode.set(2);
        }else{
            displayTextMode.set(1);
        }

        adapter.notifyDataSetChanged();
        changeDisplayLayout();
    }
    private void changeDisplayLayout() {
        if (displayTextMode.get()==2){

            headerView.setBackground(ContextCompat.getDrawable(Texttt.this,R.drawable.background_layout_dark));
            return;
        }
        headerView.setBackground(ContextCompat.getDrawable(Texttt.this,R.drawable.background_layout));

    }

    @Override
    public void GotoPage() {
        if (!this.errerToFileReading&&!this.stringArrayList.isEmpty()){
            if (recyclerView !=null){
                recyclerView.smoothScrollToPosition(0);
            }

        }
    }

    @Override
    public void Share() {
        if (filePath!=null){
            utilityAction.shareFile(filePath);
        }

    }



    @Override
    public void getViewPosition(Rect item) {


    }


    class DataProcessingAsync extends AsyncTask<Void, Integer, String> {
        DataProcessingAsync() {
        }


        public void onPreExecute() {
            super.onPreExecute();
            Texttt.this.showLoder();
        }


        /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
            com.avp.document.viewer.reader.DM_ActivityTxtView.access$402(r6.this$0, true);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0057, code lost:
            com.avp.document.viewer.reader.DM_ActivityTxtView.access$402(r6.this$0, true);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x005c, code lost:
            return "";
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0051 */
        public String doInBackground(Void... voidArr) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(Texttt.this.filePath));
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
                Texttt.this.stringArrayList.add(sb.toString());
                sb.setLength(0);
                return "";
            }
            Texttt.this.stringArrayList.add(sb.toString());
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }




        public void onPostExecute(String str) {
            super.onPostExecute(str);
            Texttt.this.hideLoder();
            Texttt.this.adapter.notifyDataSetChanged();
            if (Texttt.this.stringArrayList.isEmpty() || Texttt.this.errerToFileReading) {
                Texttt.this.errorToReadingFile();
            }
        }
    }


//    public void onStart() {
//        super.onStart();
//        AdsContttt.getInstance(this).showDirectCallInterstitialAds();
//    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_txt_view);
        ButterKnife.bind(this);
        collectIntentData();
        setToolBar();
        initObject();
        showLoder();
        new DataProcessingAsync().execute(new Void[0]);

        onClickView();
        TouchView();
    }

    private void onClickView() {

        nightMode.setOnClickListener(new View.OnClickListener() {
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
        this.stringArrayList = new ArrayList<>();
        this.recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        actions = (Actions) this;
        this.utilityAction = new UtilityAction(Texttt.this);
        performAction = new PerformAction(Texttt.this,this);
        isFileABookMark = new AtomicBoolean(false);
        showHighLight = new AtomicBoolean(false);
        isReady = new AtomicBoolean(false);
        this.viewState= new ActionBox(false,false,0,false);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        words = new ArrayList<>();
        displayTextMode =new AtomicInteger(1);
        this.adapter = new DataAdapter(this.stringArrayList,this.displayTextMode,showHighLight,this);
        this.recyclerView.setAdapter(this.adapter);

    }


    public void showLoder() {
        this.loading.setVisibility(View.VISIBLE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.recyclerView.setVisibility(View.GONE);
    }


    public void hideLoder() {
        this.loading.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.GONE);
        this.recyclerView.setVisibility(View.VISIBLE);
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
            this.fileName = FilesDataade.getFileName(this.filePath);
            supportActionBar.setTitle((CharSequence) this.fileName);
        }
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Texttt.this.finish();
            }
        });
    }

    private void collectIntentData() {
        if (getIntent().getExtras() != null) {
            String str = "filepath";
            if (getIntent().getExtras().containsKey(str)) {
                this.intentAction = getIntent().getAction();
                this.filePath = getIntent().getExtras().getString(str);
                return;
            }
        }
        this.errerToFileReading = true;
    }


    public void errorToReadingFile() {
        if (this.errerToFileReading) {
            dialogError();
        }
    }


    public void dialogError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) "Unable to open the document");
        builder.setMessage((CharSequence) "An error occurred while opening the document.");
        builder.setIcon((int) R.drawable.text_file);
        builder.setNegativeButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        if (this.intentAction.equals("a")) {
            builder.setPositiveButton((CharSequence) "Open With", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Texttt dM_ActivityTxtView = Texttt.this;
                    ConstantDataa.openTextDocument(dM_ActivityTxtView, dM_ActivityTxtView.filePath, Boolean.valueOf(true));
                }
            });
        }
        builder.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                Texttt.this.onBackPressed();
            }
        });
        builder.show();
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
//
//        ConstantDataa.openTextDocument(this, this.filePath);
//        return true;
//    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_search);
        bookmarkItem = menu.findItem(R.id.action_bookmark);
        menuItem.setVisible(false);
        performAction.isBookMark(new File(filePath));


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
                        performAction.DeleteBookMark(new File(filePath));
                        item.setIcon(ContextCompat.getDrawable(this,R.drawable.up_bookmark));
                    }else {
                        isFileABookMark.set(true);
                        performAction.bookMark(new File(filePath));
                        item.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_bookmarkfile_white));
                    }

                }else{
                    Toast.makeText(this,"Try again",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.action_open:
                ConstantDataa.openTextDocument(this, this.filePath);
                break;



        }

        return super.onOptionsItemSelected(item);
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
                    bookmarkItem.setIcon(ContextCompat.getDrawable(Texttt.this,R.drawable.ic_bookmarkfile_white));
                }
                isFileABookMark.set(find);

            }
        });

    }

    class SaveToPDF extends AsyncTask<Void ,Integer,String>{
        File file=null;
        private WeakReference<Context> contextRef;
        ProgressBar progressBar;

        public SaveToPDF(Context context) {
            contextRef = new WeakReference<>(context);
            progressBar = new ProgressBar(Texttt.this, null, android.R.attr.progressBarStyleLarge);
            CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(100,100);
            layoutParams.gravity = Gravity.CENTER;
            Texttt.this.layout.addView(progressBar,layoutParams);

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (Texttt.this.stringArrayList.isEmpty()){
                return "";
            }
            try
            {
                String text = "";
                for (String fileText :Texttt.this.stringArrayList){
                    text = fileText;
                }
                this.file = File.createTempFile("txtTempo", ".pdf", Texttt.this.getApplicationContext().getCacheDir());
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
                Texttt.this.errorSavingFileTxt = true;
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (DocumentException e)
            {
                Texttt.this.errorSavingFileTxt = true;
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                Texttt.this.errorSavingFileTxt = true;
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            File file2 = this.file;
            if (file2 == null) {
                Texttt.this.errorSavingFileTxt = true;
            } else if (!file2.exists()) {
                Texttt.this.errorSavingFileTxt = true;
            }
            if (!Texttt.this.errorSavingFileTxt) {
                if(!Texttt.this.errerToFileReading){
                    utilityAction.print(file2.getAbsolutePath());
                }
            } else {
                Texttt.this.errorToReadingFile();
            }
        }
    }

    public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
        private ArrayList<String> countries;
        private AtomicInteger displayTextMode;
        private viewPosition viewPosition;
        private AtomicBoolean showHighLight;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txtTextView;

            public ViewHolder(View view) {
                super(view);
                this.txtTextView = (TextView) view.findViewById(R.id.txtTextView);
            }
        }

        public DataAdapter(ArrayList<String> arrayList,AtomicInteger diplayTextMode,AtomicBoolean showHighlight,viewPosition viewPosition)
        {
            this.showHighLight = showHighlight;
            this.viewPosition = viewPosition;
            this.displayTextMode =diplayTextMode;
            this.countries = arrayList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_txt_view, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            if (showHighLight.get()){
                if (!words.isEmpty()){
                   Rect rect = getTexViewSelectionCoordinate(viewHolder.txtTextView,words);
                    Log.d("valueOptedOut", "onBindViewHolder: rectvalue  "+rect.right+"  bottom  "+rect.bottom);
                   if (viewPosition !=null){

                       viewPosition.getViewPosition(rect);
                   }
                }

                showHighLight.set(false);
            }




            int displayMode = displayTextMode.get();
            switch (displayMode){
                case 2:
                    viewHolder.txtTextView.setTextColor(Color.WHITE);
                    break;
                case 1:
                    viewHolder.txtTextView.setTextColor(Color.BLACK);
                    break;

            }
            viewHolder.txtTextView.setText((CharSequence) this.countries.get(i));

        }

        private   Rect getTexViewSelectionCoordinate(TextView  textView ,ArrayList<Integer> list){

                int index = list.get(0);
                Layout layout = textView.getLayout();
                Rect bound = new Rect();
                int line = layout.getLineForOffset(index);
                layout.getLineBounds(line,bound);
                return bound;

        }

        public int getItemCount() {
            return this.countries.size();
        }
    }
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
}
