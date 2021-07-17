package company.android.documentmanager.FileReaders;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import company.android.documentmanager.Ads.FaceBooKAds;
import company.android.documentmanager.BottomAction.ActionBox;
import company.android.documentmanager.BottomAction.Actions;
import company.android.documentmanager.BottomAction.UtilityAction;
import company.android.documentmanager.R;
import company.android.documentmanager.TopAction.Finsihcallback;
import company.android.documentmanager.TopAction.PerformAction;
import company.android.documentmanager.Utility.DataaaObjectUtil;
import company.android.documentmanager.office.macro.DialogListener;
import company.android.documentmanager.office.mycommsmoms.IOfficeToPicture;
import company.android.documentmanager.office.officereader.FindToolBar;
import company.android.documentmanager.office.officereader.beans.AImageButton;
import company.android.documentmanager.office.officereader.beans.AImageCheckButton;
import company.android.documentmanager.office.officereader.beans.AToolsbar;
import company.android.documentmanager.office.officereader.beans.CalloutToolsbar;
import company.android.documentmanager.office.officereader.beans.SSToolsbar;
import company.android.documentmanager.office.officereader.beans.WPToolsbar;
import company.android.documentmanager.office.pg.control.PGControl;
import company.android.documentmanager.office.res.ResConstant;
import company.android.documentmanager.office.res.ResKit;
import company.android.documentmanager.office.ss.sheetbar.SheetBar;
import company.android.documentmanager.office.system.IControl;
import company.android.documentmanager.office.system.IMainFrame;
import company.android.documentmanager.office.system.MainControl;
import company.android.documentmanager.office.valconstttaa.EventConstant;
import company.android.documentmanager.office.valconstttaa.MainConstant;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelActivity extends AppCompatActivity implements IMainFrame,Actions,Finsihcallback {
    RelativeLayout RRjarLayout;
    RelativeLayout RRlibraryLayout;
    RelativeLayout RRloading;
    private LinearLayout appFrame;
    private int applicationType = -1;

    public String authority;
    private Object bg = Integer.valueOf(-7829368);
    private SheetBar bottomBar;
    private CalloutToolsbar calloutBar;

    public MainControl control;
    private AImageCheckButton eraserButton;

    public boolean errerToFileReading = false;
    private String fileName;

    public String filePath;

    public String filetype;
    private boolean fullscreen;
    private View gapView;
    private String intentAction;
    private boolean isDispose;
    private boolean isThumbnail;
    private boolean marked;
    MenuItem menu_item_other_app_file_opener;
    private AImageButton pageDown;
    private AImageButton pageUp;
    private AImageCheckButton penButton;
    private FindToolBar searchBar;
    private AImageButton settingsButton;

    private  boolean printError= false;
    public TabHost tabHost;
    private String tempFilePath;
    private Toast toast;
    private AToolsbar toolsbar;
    private WindowManager wm = null;
    private LayoutParams wmParams = null;
    private boolean writeLog = true;

    public AtomicInteger displayTextMode;

    @BindView(R.id.share)ImageView share;
    @BindView(R.id.rotate)ImageView rotate;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheetLayout;

    @BindView(R.id.print)ImageView print;
    @BindView(R.id.headerView) RelativeLayout headerView;
    @BindView(R.id.txtPrint) TextView txtPrint;
    @BindView(R.id.mainLayout)CoordinatorLayout layout;

    private boolean errorSavingFile=false;
    private String PdfFile;

    Actions actions;

    UtilityAction utilityAction;

    PerformAction performAction;
    MenuItem bookmarkItem;

    BottomSheetBehavior<View> bottomSheetBehavior;

    AtomicBoolean isFileABookMark;
    AtomicBoolean isReady;
    ActionBox viewState;

    private  String convertedXLs;


    @Override
    public void Print() {
File file = new File(tempoDirectory());
        if(file.exists()){
            utilityAction.print(file.getAbsolutePath());
        }
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

        changeDisplayLayout();
    }

    private void changeDisplayLayout() {
        if (displayTextMode.get()==2){
            headerView.setBackground(ContextCompat.getDrawable(ExcelActivity.this,R.drawable.background_layout_dark));
            return;
        }
        headerView.setBackground(ContextCompat.getDrawable(ExcelActivity.this,R.drawable.background_layout));

    }

    @Override
    public void GotoPage() {
        openJumpPageDialog();
    }

    @Override
    public void Share() {
        if (filePath!=null){
            utilityAction.shareFile(filePath);
        }
    }



    class CustomListener implements OnClickListener {
        private final Dialog dialog;
        EditText text;

        public CustomListener(Dialog dialog2, EditText editText) {
            this.dialog = dialog2;
            this.text = editText;
        }

        public void onClick(View view) {
            int pageCount = ((PGControl) ExcelActivity.this.control.getAppControl()).getPageCount();
            if (this.text.getText().toString().trim().length() <= 0) {
                ExcelActivity.dialogWarning(ExcelActivity.this, "Please enter a page number");
            } else if (pageCount >= Integer.parseInt(this.text.getText().toString())) {
                if (!DataaaObjectUtil.isEmpty(this.text.getText().toString().trim())) {
                    int i = 0;
                    try {
                        i = Integer.parseInt(this.text.getText().toString());
                    } catch (Exception unused) {
                    }
                    if (i > 0 && !DataaaObjectUtil.isNull(ExcelActivity.this.control) && (ExcelActivity.this.control.getAppControl() instanceof PGControl)) {
                        ((PGControl) ExcelActivity.this.control.getAppControl()).showSlidePage(i - 1);
                    }
                }
                this.dialog.dismiss();
            } else {
                ExcelActivity.dialogWarning(ExcelActivity.this, "Please enter a valid page number.");
            }
        }
    }

    class ReadExcelAsync extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();
        String fileExt = "";

        ReadExcelAsync() {
        }


        public void onPreExecute() {
            super.onPreExecute();
        }


        public String doInBackground(Void... voidArr) {
            String str = "";
            String str2 = ".xls";
            String str3 = ".xlsx";
            if (ExcelActivity.this.authority.equals(str)) {
                this.fileExt = FilesDataade.getFileExt(ExcelActivity.this.filePath);
            } else if (!ExcelActivity.this.authority.contains("com.whatsapp.provider.media")) {
                this.fileExt = FilesDataade.getFileExt(ExcelActivity.this.filePath);
            } else if (ExcelActivity.this.filetype.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                this.fileExt = str3;
            } else if (ExcelActivity.this.filetype.equals("application/vnd.ms-excel")) {
                this.fileExt = str2;
            }
            if (!ExcelActivity.this.errerToFileReading) {
                if (this.fileExt.endsWith(str2)) {
                    ExcelActivity dM_ActivityExcelView = ExcelActivity.this;
                    dM_ActivityExcelView.readXlsFile(dM_ActivityExcelView.filePath);
                } else if (this.fileExt.endsWith(str3)) {
                    ExcelActivity.this.readXlsxFile();
                } else {
                    ExcelActivity.this.errerToFileReading = true;
                }
            }
            return str;
        }


        public void onPostExecute(String str) {
            super.onPostExecute(str);
            if (ExcelActivity.this.errerToFileReading) {
                ExcelActivity.this.errorToReadingFile();
            }
            ExcelActivity.this.hideLoder();
        }
    }

    private boolean isSearchbarActive() {
        return false;
    }

    private void markFile() {
    }

    public void changePage() {
    }

    public void changeZoom() {
    }

    public void completeLayout() {
    }

    public void dispose() {
    }

    public boolean doActionEvent(int i, Object obj) {
        if (!(i == 0 || i == 15 || i == 20 || i == 25 || i == 268435464 || i == 1073741828 || i == 536870912 || i == 536870913)) {
            switch (i) {
                case EventConstant.APP_DRAW_ID /*536870937*/:
                case EventConstant.APP_BACK_ID /*536870938*/:
                case EventConstant.APP_PEN_ID /*536870939*/:
                case EventConstant.APP_ERASER_ID /*536870940*/:
                case EventConstant.APP_COLOR_ID /*536870941*/:
                    break;
                default:
                    switch (i) {
                        case EventConstant.APP_FINDING /*788529152*/:
                        case EventConstant.APP_FIND_BACKWARD /*788529153*/:
                        case EventConstant.APP_FIND_FORWARD /*788529154*/:
                            break;
                        default:
                            return false;
                    }
            }
        }
        return true;
    }

    public void fullScreen(boolean z) {
    }

    public Activity getActivity() {
        return this;
    }

    public DialogListener getDialogListener() {
        return null;
    }

    public byte getPageListViewMovingPosition() {
        return 0;
    }

    public String getTXTDefaultEncode() {
        return "GBK";
    }

    public int getTopBarHeight() {
        return 0;
    }

    public byte getWordDefaultView() {
        return 0;
    }

    public void initMarked() {
    }

    public boolean isChangePage() {
        return true;
    }

    public boolean isDrawPageNumber() {
        return true;
    }

    public boolean isIgnoreOriginalSize() {
        return false;
    }

    public boolean isShowFindDlg() {
        return true;
    }

    public boolean isShowPasswordDlg() {
        return false;
    }

    public boolean isShowProgressBar() {
        return false;
    }

    public boolean isShowTXTEncodeDlg() {
        return true;
    }

    public boolean isShowZoomingMsg() {
        return true;
    }

    public boolean isTouchZoom() {
        return true;
    }

    public boolean isZoomAfterLayoutForWord() {
        return true;
    }

    public boolean onEventMethod(View view, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, byte b) {
        return false;
    }

    public void setEraserUnChecked() {
    }

    public void setFindBackForwardState(boolean z) {
    }

    public void setIgnoreOriginalSize(boolean z) {
    }

    public void setPenUnChecked() {
    }

    public void showCalloutToolsBar(boolean z) {
    }

    public void showSearchBar(boolean z) {
    }

    public void updateToolsbarStatus() {
    }

    public void updateViewImages(List<Integer> list) {
    }

//
//    public void onStart() {
//        super.onStart();
//        AdsContttt.getInstance(this).showDirectCallInterstitialAds();
//    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_excel_view);


        ButterKnife.bind(this);



        FaceBooKAds faceBooKAds = new FaceBooKAds(this);
        faceBooKAds.LoadInterstitial();
        this.performAction = new PerformAction(ExcelActivity.this,this);
        this.isFileABookMark = new AtomicBoolean(false);
        this.isReady = new AtomicBoolean(false);
        this.actions = this;
        this.utilityAction = new UtilityAction(this);
        this.viewState  = new ActionBox(false,false,0,false);

        this.bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        this.displayTextMode = new AtomicInteger(1);


        androidx.appcompat.widget.Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.intentAction = getIntent().getAction();
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ExcelActivity.this.finish();
            }
        });
TouchView();

        this.RRlibraryLayout = (RelativeLayout) findViewById(R.id.RRlibraryLayout);
        this.RRjarLayout = (RelativeLayout) findViewById(R.id.RRjarLayout);
        this.control = new MainControl(this);
        this.appFrame = (LinearLayout) findViewById(R.id.llmainframe);
        this.RRloading = (RelativeLayout) findViewById(R.id.RRloading);
        this.RRloading.setVisibility(View.INVISIBLE);
        this.tabHost = (TabHost) findViewById(R.id.sheets);
        this.tabHost.setVisibility(View.INVISIBLE);






        this.appFrame.post(new Runnable() {
            public void run() {
                ExcelActivity.this.init();
            }
        });
        this.control.setOffictToPicture(new IOfficeToPicture() {
            private Bitmap bitmap;

            public void dispose() {
            }

            public byte getModeType() {
                return 1;
            }

            public boolean isZoom() {
                return false;
            }

            public void setModeType(byte b) {
            }

            public Bitmap getBitmap(int i, int i2) {
                if (i == 0 || i2 == 0) {
                    return null;
                }
                Bitmap bitmap2 = this.bitmap;
                if (!(bitmap2 != null && bitmap2.getWidth() == i && this.bitmap.getHeight() == i2)) {
                    Bitmap bitmap3 = this.bitmap;
                    if (bitmap3 != null) {
                        bitmap3.recycle();
                    }
                    this.bitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                }
                return this.bitmap;
            }

            public void callBack(Bitmap bitmap2) {
                ExcelActivity.this.saveBitmapToFile(bitmap2);
            }
        });
        onClickView();
    }

    private void onClickView() {


        share.setOnClickListener(v -> actions.Share());
        rotate.setOnClickListener(v -> {
            actions.Rotate();

        });


        print.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                openPrintPage();
            }
        });
    }


    public void saveBitmapToFile(Bitmap bitmap) {
        if (bitmap != null) {
            if (this.tempFilePath == null) {
                if ("mounted".equals(Environment.getExternalStorageState())) {
                    this.tempFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                }
                StringBuilder sb = new StringBuilder();
                sb.append(this.tempFilePath);
                sb.append(File.separatorChar);
                sb.append("tempPic");
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdir();
                }
                this.tempFilePath = file.getAbsolutePath();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.tempFilePath);
            sb2.append(File.separatorChar);
            sb2.append("export_image.jpg");
            File file2 = new File(sb2.toString());
            try {
                if (file2.exists()) {
                    file2.delete();
                }
                file2.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException unused) {
            }
        }
    }


    public void onPause() {
        super.onPause();
    }


    public void onResume() {
        super.onResume();
    }


    public void hideLoder() {
        this.tabHost.setVisibility(View.INVISIBLE);
        this.RRloading.setVisibility(View.VISIBLE);
        adsdispaly();
//        if (this.menu_item_other_app_file_opener == null) {
//            return;
//        }
//        if (this.intentAction.equals("a")) {
//            this.menu_item_other_app_file_opener.setVisible(true);
//        } else {
//            this.menu_item_other_app_file_opener.setVisible(false);
//        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }


    public void onDestroy() {
        super.onDestroy();
    }

    public void showProgressBar(boolean z) {
        setProgressBarIndeterminateVisibility(false);
    }


    public void init() {
        this.toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);

        if (getIntent().getExtras() != null) {
            String str = "filepath";
            if (getIntent().getExtras().containsKey(str)) {
                this.filePath = getIntent().getExtras().getString(str);
            }
        }
        if (getIntent().getExtras() != null) {
            String str2 = "authority";
            if (getIntent().getExtras().containsKey(str2)) {
                this.authority = getIntent().getExtras().getString(str2);
            }
        }
        if (getIntent().getExtras() != null) {
            String str3 = "filetype";
            if (getIntent().getExtras().containsKey(str3)) {
                this.filetype = getIntent().getExtras().getString(str3);
            }
        }
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle((CharSequence) FilesDataade.getFileName(this.filePath));
        createView();
        this.control.openFile(this.filePath);
        initMarked();
    }

    public boolean isPopUpErrorDlg() {
        this.RRloading.setVisibility(View.INVISIBLE);
        this.RRjarLayout.setVisibility(View.INVISIBLE);
        this.RRlibraryLayout.setVisibility(View.VISIBLE);
        return false;
    }

    private void createView() {
        String lowerCase = this.filePath.toLowerCase();
        if (lowerCase.endsWith(MainConstant.FILE_TYPE_XLS) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLT) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSM)) {
            this.applicationType = 1;
            this.toolsbar = new SSToolsbar(getApplicationContext(), (IControl) this.control);
        } else {
            this.applicationType = 0;
            this.toolsbar = new WPToolsbar(getApplicationContext(), this.control);
        }
        this.appFrame.addView(this.toolsbar);
    }

    public Dialog onCreateDialog(int i) {
        return this.control.getDialog(this, i);
    }

    public IControl getControl() {
        return this.control;
    }

    public int getApplicationType() {
        return this.applicationType;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void openFileFinish() {
        adsdispaly();
        this.RRjarLayout.setVisibility(View.VISIBLE);
        this.RRlibraryLayout.setVisibility(View.VISIBLE);
        this.RRloading.setVisibility(View.INVISIBLE);
        this.gapView = new View(getApplicationContext());
        this.gapView.setBackgroundColor(-7829368);
        this.appFrame.addView(this.gapView, new LinearLayout.LayoutParams(-1, 1));
        this.appFrame.addView(this.control.getView(), new LinearLayout.LayoutParams(-1, -1));
    }

    public int getBottomBarHeight() {
        SheetBar sheetBar = this.bottomBar;
        if (sheetBar != null) {
            return sheetBar.getSheetbarHeight();
        }
        return 0;
    }

    public String getAppName() {
        return getString(R.string.app_name);
    }

    private void initFloatButton() {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.icon_folder, options);
        this.wm = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        this.wmParams = new LayoutParams();
        LayoutParams layoutParams = this.wmParams;
        layoutParams.type = 2002;
        layoutParams.format = 1;
        layoutParams.flags = 40;
        layoutParams.width = options.outWidth;
        this.wmParams.height = options.outHeight;
    }

    public void error(int i) {
        this.RRloading.setVisibility(View.INVISIBLE);
        StringBuilder sb = new StringBuilder();
        sb.append("errorCode ");
        sb.append(i);
        Log.d("XXXXXXXXX", sb.toString());
        this.RRjarLayout.setVisibility(View.INVISIBLE);
        this.RRlibraryLayout.setVisibility(View.VISIBLE);
        new ReadExcelAsync().execute(new Void[0]);
    }

    public String getLocalString(String str) {
        return ResKit.instance().getLocalString(str);
    }

    public void changePage(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(" / ");
        sb.append(i2);
        sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("");
        sb2.append(sb.toString());
        Toast.makeText(this, sb2.toString(), Toast.LENGTH_SHORT).show();
    }

    public void setWriteLog(boolean z) {
        this.writeLog = z;
    }

    public boolean isWriteLog() {
        return this.writeLog;
    }

    public void setThumbnail(boolean z) {
        this.isThumbnail = z;
    }

    public Object getViewBackground() {
        return this.bg;
    }

    public boolean isThumbnail() {
        return this.isThumbnail;
    }

    public File getTemporaryDirectory() {
        File externalFilesDir = getExternalFilesDir(null);
        if (externalFilesDir != null) {
            return externalFilesDir;
        }
        return getFilesDir();
    }

    private void openJumpPageDialog() {
        if (!DataaaObjectUtil.isNull(this.control)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle((CharSequence) "Enter Page Number to Jump");
            View inflate = getLayoutInflater().inflate(R.layout.alert_label_editor, null);
            builder.setView(inflate);
            EditText editText = (EditText) inflate.findViewById(R.id.label_field);
            builder.setPositiveButton((CharSequence) ResConstant.BUTTON_OK, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog create = builder.create();
            create.show();
            create.getButton(-1).setOnClickListener(new CustomListener(create, editText));
        }
    }

    public static void dialogWarning(Activity activity, String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle((CharSequence) "Warning");
        builder.setMessage((CharSequence) str);
        builder.setPositiveButton((CharSequence) ResConstant.BUTTON_OK, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
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
        builder.setIcon((int) R.drawable.excel_file);
        builder.setNegativeButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        if (this.intentAction.equals("a")) {
            builder.setPositiveButton((CharSequence) "Open With", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ExcelActivity dM_ActivityExcelView = ExcelActivity.this;
                    ConstantDataa.openExcelDocument(dM_ActivityExcelView, dM_ActivityExcelView.filePath);
                }
            });
        }
        builder.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                ExcelActivity.this.onBackPressed();
            }
        });
        builder.show();
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.detailmenu, menu);
////        this.menu_item_other_app_file_opener = menu.findItem(R.id.other_app_file_opener);
////        if (this.intentAction.equals("a")) {
////            this.menu_item_other_app_file_opener.setVisible(true);
////        } else {
////            this.menu_item_other_app_file_opener.setVisible(false);
////        }
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
//        ConstantDataa.openExcelDocument(this, this.filePath);
//        return true;
//    }

    private void setBackgroundColor() {
        int inactiveColor = getResources().getColor(R.color.black);
        int activeColor = getResources().getColor(R.color.black);

        // In this loop you will set the inactive tabs backgroung color
        for (int i = 0; i < tabHost.getChildCount(); i++) {
            tabHost.getChildAt(i).setBackgroundColor(inactiveColor);
        }

        // Here you will set the active tab background color
        tabHost.getChildAt(tabHost.getCurrentTab()).setBackgroundColor(
                activeColor);
    }


    public void readXlsFile( String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                Sheet[] sheets;
//                String str = "    ";
                try {
                    Workbook workbook = Workbook.getWorkbook((InputStream) new FileInputStream(str));
                    ExcelActivity.this.tabHost.setup();
                    for (final Sheet sheet : workbook.getSheets()) {
                        Log.d("valueOptedOut", "run: "+sheet.getName());
                        TabSpec newTabSpec = ExcelActivity.this.tabHost.newTabSpec(sheet.getName());
                        newTabSpec.setContent(new TabContentFactory() {
                            public View createTabContent(String str) {
                                XlsSheetView xlsSheetView = new XlsSheetView(ExcelActivity.this);
                                xlsSheetView.setSheet(sheet);
                                return xlsSheetView;
                            }
                        });
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(sheet.getName());
                        sb.append(str);
                        newTabSpec.setIndicator(sb.toString());
                        ExcelActivity.this.tabHost.addTab(newTabSpec);
                    }
                    tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.BLACK);



                } catch (IOException e) {
                    e.printStackTrace();
                    ExcelActivity.this.errerToFileReading = true;
                } catch (BiffException e2) {
                    e2.printStackTrace();
                    ExcelActivity.this.errerToFileReading = true;
                } catch (Exception unused) {
                    ExcelActivity.this.errerToFileReading = true;
                }
            }
        });
    }


    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00e3, code lost:
        r14.errerToFileReading = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00e6, code lost:
        r14.errerToFileReading = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:41:? A[ExcHandler: RuntimeException (unused java.lang.RuntimeException), SYNTHETIC, Splitter:B:1:0x0001] */
    public void readXlsxFile() {

        try {

            XSSFWorkbook xSSFWorkbook = new XSSFWorkbook((InputStream) new FileInputStream(this.filePath));
            HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();
            int numberOfSheets = xSSFWorkbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {

                XSSFSheet sheetAt = xSSFWorkbook.getSheetAt(0);
                org.apache.poi.ss.usermodel.Sheet createSheet = hSSFWorkbook.createSheet(sheetAt.getSheetName());
                Iterator rowIterator = sheetAt.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = (Row) rowIterator.next();
                    Row createRow = createSheet.createRow(row.getRowNum());
                    Iterator cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = (Cell) cellIterator.next();
                        Cell createCell = createRow.createCell(cell.getColumnIndex(), cell.getCellType());
                        String cellTypes = String.valueOf(cell.getCellType());
                        int cellType = Integer.valueOf(cellTypes);
                        if (cellType == 0) {
                            createCell.setCellValue(cell.getNumericCellValue());
                        } else if (cellType == 1) {
                            createCell.setCellValue(cell.getStringCellValue());
                        } else if (cellType == 2) {
                            createCell.setCellFormula(cell.getCellFormula());
                        } else if (cellType != 3) {
                            if (cellType == 4) {
                                createCell.setCellValue(cell.getBooleanCellValue());
                            } else if (cellType == 5) {
                                createCell.setCellValue((double) cell.getErrorCellValue());
                            }
                        }
                        createCell.getCellStyle().setDataFormat(cell.getCellStyle().getDataFormat());
                        createCell.setCellComment(cell.getCellComment());
                    }
                }
            }
            File createTempFile = File.createTempFile("myTempxlsFile", ".xls", getApplicationContext().getCacheDir());
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(createTempFile));
            hSSFWorkbook.write(bufferedOutputStream);
            bufferedOutputStream.close();
            if (createTempFile.exists()) {
                convertedXLs = createTempFile.getPath();
                Log.d("reading", "readXlsxFile: "+" exited");
                readXlsFile(createTempFile.getPath());
            } else {
                Log.d("reading", "readXlsxFile: "+" exited");
                this.errerToFileReading = true;
            }
        } catch (Exception unused) {
            Log.d("reading", "readXlsxFile: "+" stoped");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detailmenu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setVisible(false);
        bookmarkItem = menu.findItem(R.id.action_bookmark);

        String filePath=null;
        if (getIntent().getExtras() != null) {
            String str = "filepath";
            if (getIntent().getExtras().containsKey(str)) {
                filePath = getIntent().getExtras().getString(str);
            }
        }

if (filePath !=null){
    performAction.isBookMark(new File(filePath));
}

        return super.onPrepareOptionsMenu(menu);
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
                ConstantDataa.openExcelDocument(this, this.filePath);
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
                    bookmarkItem.setIcon(ContextCompat.getDrawable(ExcelActivity.this,R.drawable.ic_bookmarkfile_white));
                }
                isFileABookMark.set(find);

            }
        });

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

    public void adsdispaly() {

    }


    public class CovertAndPrint extends AsyncTask<Void,String,String> {

        ProgressBar progressBar;

        int val;
        public CovertAndPrint(int i) {

            val = i;
            progressBar = new ProgressBar(ExcelActivity.this, null, android.R.attr.progressBarStyleLarge);
            CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(100,100);
            layoutParams.gravity = Gravity.CENTER;
            ExcelActivity.this.layout.addView(progressBar,layoutParams);
        }
        @Override
        protected String doInBackground(Void... value) {
            String fileExt = FilesDataade.getFileExt(ExcelActivity.this.filePath);


            String str2 = ".xls";
            String str3 = ".xlsx";
            if (filePath != null) {
                if (fileExt.endsWith(str2)){
                    readXlsDocument(filePath, val);
                }else if (fileExt.endsWith(str3)){
                    example(filePath,val);
                }

            }
            return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if (!printError){
                actions.Print();
            }else {
                Toast.makeText(ExcelActivity.this,"Printing Error",Toast.LENGTH_LONG).show();
            }

        }
    }


    private void  readXlsDocument(String filePath, int val){

        try {

            HSSFWorkbook my_workBook =(HSSFWorkbook)WorkbookFactory.create(new File(filePath));

            HSSFSheet sheet = my_workBook.getSheetAt(val-1);
            if (sheet==null){
                printError=true;
                return;
            }

            Iterator<Row> rowIterator = sheet.iterator();

            xlsApiText xlsApiText = new xlsApiText(filePath);

            Document iText_xls_2_pdf = new Document();
            PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream(tempoDirectory()));
            iText_xls_2_pdf.open();

            PdfPTable my_table = new PdfPTable(xlsApiText.getColumnCount(sheet.getSheetName()));

            PdfPCell table_cell;
            while (rowIterator.hasNext()){
                Row row =rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()){

                        case Cell.CELL_TYPE_STRING:
                            table_cell=new PdfPCell(new Phrase(""+cell.getStringCellValue()));
                            my_table.addCell(table_cell);
                            break;
                        case Cell.CELL_TYPE_FORMULA:

                            table_cell=new PdfPCell(new Phrase(""+cell.getCellFormula()));
                            my_table.addCell(table_cell);
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            table_cell=new PdfPCell(new Phrase(""+cell.getNumericCellValue()));
                            my_table.addCell(table_cell);
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            table_cell=new PdfPCell(new Phrase(""+cell.getErrorCellValue()));
                            my_table.addCell(table_cell);
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            table_cell=new PdfPCell(new Phrase(""+cell.getBooleanCellValue()));
                            my_table.addCell(table_cell);
                            break;
                    }
                }

            }

            iText_xls_2_pdf.add(my_table);
            iText_xls_2_pdf.close();
        } catch (Exception e) {
            printError=true;
            e.printStackTrace();

        }

    }
    private void  example(String filePath, int val){


        try {

            XSSFWorkbook my_workBook =(XSSFWorkbook)WorkbookFactory.create(new File(filePath));


            XSSFSheet sheet = my_workBook.getSheetAt(val-1);

            if (sheet==null){
                printError=true;
                return;
            }
            Iterator<Row> rowIterator = sheet.iterator();

            ExcelApiTest xlsApiText = new ExcelApiTest(filePath);

            Document iText_xls_2_pdf = new Document();
            PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream(tempoDirectory()));
            iText_xls_2_pdf.open();

            PdfPTable my_table = new PdfPTable(xlsApiText.getColumnCount(sheet.getSheetName()));

            PdfPCell table_cell;
            while (rowIterator.hasNext()){
                Row row =rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()){

                        case Cell.CELL_TYPE_STRING:
                            table_cell=new PdfPCell(new Phrase(""+cell.getStringCellValue()));
                            my_table.addCell(table_cell);
                            break;
                        case Cell.CELL_TYPE_FORMULA:

                            table_cell=new PdfPCell(new Phrase(""+cell.getCellFormula()));
                            my_table.addCell(table_cell);
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            table_cell=new PdfPCell(new Phrase(""+cell.getNumericCellValue()));
                            my_table.addCell(table_cell);
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            table_cell=new PdfPCell(new Phrase(""+cell.getErrorCellValue()));
                            my_table.addCell(table_cell);
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            table_cell=new PdfPCell(new Phrase(""+cell.getBooleanCellValue()));
                            my_table.addCell(table_cell);
                            break;
                    }
                }

            }

            iText_xls_2_pdf.add(my_table);
            iText_xls_2_pdf.close();
        } catch (Exception e) {
            printError=true;
            e.printStackTrace();
        }
    }

private String tempoDirectory(){
        return getExternalFilesDir(null)+"exceltopdf.pdf";
}
    private void openPrintPage() {
        if (!DataaaObjectUtil.isNull(this.control)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle((CharSequence) "Enter Page Number to Print");
            View inflate = getLayoutInflater().inflate(R.layout.alert_label_editor, null);
            builder.setView(inflate);
            EditText editText = (EditText) inflate.findViewById(R.id.label_field);
            builder.setPositiveButton((CharSequence) ResConstant.BUTTON_OK, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog create = builder.create();
            create.show();
            create.getButton(-1).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText.getText().toString().isEmpty()){
                        Toast.makeText(ExcelActivity.this,"Must put a value",Toast.LENGTH_LONG).show();
                        return;
                    }
                     create.dismiss();
                    printError=false;
                    new CovertAndPrint(Integer.parseInt(editText.getText().toString().trim())).execute();
                }
            });
        }
    }

}