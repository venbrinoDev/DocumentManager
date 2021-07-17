package company.android.documentmanager.FileReaders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.FileOutputStream;
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
import company.android.documentmanager.office.macro.DialogListener;
import company.android.documentmanager.office.mycommsmoms.IOfficeToPicture;
import company.android.documentmanager.office.officereader.FindToolBar;
import company.android.documentmanager.office.officereader.beans.AImageButton;
import company.android.documentmanager.office.officereader.beans.AImageCheckButton;
import company.android.documentmanager.office.officereader.beans.AToolsbar;
import company.android.documentmanager.office.officereader.beans.CalloutToolsbar;
import company.android.documentmanager.office.officereader.beans.PGToolsbar;
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


public class PPTViewData extends AppCompatActivity implements IMainFrame,Actions,Finsihcallback {
    RelativeLayout RRloading;
    LinearLayout appFrame;
    private int applicationType = -1;
    private Object bg = Integer.valueOf(Color.parseColor("#EEEEEE"));
    private SheetBar bottomBar;
    private CalloutToolsbar calloutBar;

    public MainControl control;
    private AImageCheckButton eraserButton;
    private String filePath;
    private boolean fullscreen;
    private View gapView;
    private String intentAction;
    private boolean isDispose;
    private boolean isThumbnail;


    TextView mPageCountView;
    private boolean marked;
//    MenuItem jump_to_page ;
//    MenuItem menu_item_other_app_file_opener;

    private AImageButton pageDown;
    private AImageButton pageUp;
    private AImageCheckButton penButton;
    private FindToolBar searchBar;
    private AImageButton settingsButton;
    private String tempFilePath;
    private Toast toast;
    private AToolsbar toolsbar;
    private int totalPageCount = 0;
    private WindowManager wm = null;
    private LayoutParams wmParams = null;
    private boolean writeLog = true;


    public AtomicInteger displayTextMode;
    @BindView(R.id.share)ImageView share;
    @BindView(R.id.rotate)ImageView rotate;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheetLayout;
    @BindView(R.id.goTo)ImageView GotoPage;

    @BindView(R.id.headerView) RelativeLayout headerView;



    Actions actions;

    UtilityAction utilityAction;

    PerformAction performAction;
    MenuItem bookmarkItem;

    BottomSheetBehavior<View> bottomSheetBehavior;

    AtomicBoolean isFileABookMark;
    AtomicBoolean isReady;
    ActionBox viewState;



    @Override
    public void Print() {

        if(filePath !=null){

            utilityAction.print(this.filePath);
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

            headerView.setBackground(ContextCompat.getDrawable(PPTViewData.this,R.drawable.background_layout_dark));
            return;
        }
        headerView.setBackground(ContextCompat.getDrawable(PPTViewData.this,R.drawable.background_layout));

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
            int pageCount = ((PGControl) PPTViewData.this.control.getAppControl()).getPageCount();
            if (this.text.getText().toString().trim().length() <= 0) {
                PPTViewData.dialogWarning(PPTViewData.this, "Please enter a page number");
            } else if (pageCount >= Integer.parseInt(this.text.getText().toString())) {
                if (!ObjectUtil.isEmpty(this.text.getText().toString().trim())) {
                    int i = 0;
                    try {
                        i = Integer.parseInt(this.text.getText().toString());
                    } catch (Exception unused) {
                    }
                    if (i > 0 && !ObjectUtil.isNull(PPTViewData.this.control) && (PPTViewData.this.control.getAppControl() instanceof PGControl)) {
                        ((PGControl) PPTViewData.this.control.getAppControl()).showSlidePage(i - 1);
                    }
                }
                this.dialog.dismiss();
            } else {
                PPTViewData.dialogWarning(PPTViewData.this, "Please enter a valid page number.");
            }
        }
    }

    public void changePage() {
    }

    public void changeZoom() {
    }

    public void completeLayout() {
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

    public void onCurrentPageChange() {
    }

    public boolean onEventMethod(View view, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, byte b) {
        return false;
    }

    public void onPagesCountChange() {
    }

    public void setIgnoreOriginalSize(boolean z) {
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
        setContentView(R.layout.activity_ppt_view);
        ButterKnife.bind(this);



        this.performAction = new PerformAction(PPTViewData.this,this);
        this.isFileABookMark = new AtomicBoolean(false);
        this.isReady = new AtomicBoolean(false);
        this.actions = this;
        this.utilityAction = new UtilityAction(this);
        this.performAction = new PerformAction(this,this);
        this.viewState  = new ActionBox(false,false,0,false);

        this.bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        this.displayTextMode = new AtomicInteger(1);

        TouchView();

        this.intentAction = getIntent().getAction();
        this.RRloading = (RelativeLayout) findViewById(R.id.RRloading);
        this.RRloading.setVisibility(View.VISIBLE);
        this.appFrame = (LinearLayout) findViewById(R.id.llmainframe);
        this.mPageCountView = (TextView) findViewById(R.id.mPageCountView);
        this.control = new MainControl(this);
        this.appFrame.post(new Runnable() {
            public void run() {
                PPTViewData.this.init();
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
                PPTViewData.this.saveBitmapToFile(bitmap2);
            }
        });
        onClickView();
    }

    private void onClickView() {


        share.setOnClickListener(v -> actions.Share());
        rotate.setOnClickListener(v -> {
            actions.Rotate();

        });


        GotoPage.setOnClickListener(v -> {

            actions.GotoPage();


        });


    }


//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_jump_page, menu);
////        this.menu_item_other_app_file_opener = menu.findItem(R.id.other_app_file_opener);
////        this.jump_to_page = menu.findItem(R.id.jump_to_page);
////        if (this.intentAction.equals("a")) {
////            this.menu_item_other_app_file_opener.setVisible(true);
////        } else {
////            this.menu_item_other_app_file_opener.setVisible(false);
////        }
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        if (menuItem.getItemId() == R.id.other_app_file_opener) {
//            if (VERSION.SDK_INT >= 24) {
//                try {
//                    StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            ConstantDataa.openPPTDocument(this, this.filePath);
//            return true;
//        }
//        if (menuItem.getItemId() == R.id.jump_to_page) {
//            openJumpPageDialog();
//        }
//        return super.onOptionsItemSelected(menuItem);
//    }
//



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detailmenu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_search);
        bookmarkItem = menu.findItem(R.id.action_bookmark);

        menuItem.setVisible(false);

        performAction.isBookMark(new File(filePath));
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
                ConstantDataa.openPPTDocument(this, this.filePath);
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
                    bookmarkItem.setIcon(ContextCompat.getDrawable(PPTViewData.this,R.drawable.ic_bookmarkfile_white));
                }
                isFileABookMark.set(find);

            }
        });

    }


    private void openJumpPageDialog() {
        if (!ObjectUtil.isNull(this.control)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle((CharSequence) "Go to page");
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
                bitmap.compress(CompressFormat.JPEG, 70, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException unused) {
            }
        }
    }


    public void onPause() {
        super.onPause();
        Object actionValue = this.control.getActionValue(EventConstant.PG_SLIDESHOW, null);
        if (actionValue != null && ((Boolean) actionValue).booleanValue()) {
            this.wm.removeView(this.pageUp);
            this.wm.removeView(this.pageDown);
            this.wm.removeView(this.penButton);
            this.wm.removeView(this.eraserButton);
            this.wm.removeView(this.settingsButton);
        }
    }


    public void onResume() {
        super.onResume();
        Object actionValue = this.control.getActionValue(EventConstant.PG_SLIDESHOW, null);
        if (actionValue != null && ((Boolean) actionValue).booleanValue()) {
            LayoutParams layoutParams = this.wmParams;
            layoutParams.gravity = 53;
            layoutParams.x = 5;
            this.wm.addView(this.penButton, layoutParams);
            LayoutParams layoutParams2 = this.wmParams;
            layoutParams2.gravity = 53;
            layoutParams2.x = 5;
            layoutParams2.y = layoutParams2.height;
            this.wm.addView(this.eraserButton, this.wmParams);
            LayoutParams layoutParams3 = this.wmParams;
            layoutParams3.gravity = 53;
            layoutParams3.x = 5;
            layoutParams3.y = layoutParams3.height * 2;
            this.wm.addView(this.settingsButton, this.wmParams);
            LayoutParams layoutParams4 = this.wmParams;
            layoutParams4.gravity = 19;
            layoutParams4.x = 5;
            layoutParams4.y = 0;
            this.wm.addView(this.pageUp, layoutParams4);
            LayoutParams layoutParams5 = this.wmParams;
            layoutParams5.gravity = 21;
            this.wm.addView(this.pageDown, layoutParams5);
        }
    }

    public void onBackPressed() {
        if (isSearchbarActive()) {
            showSearchBar(false);
            updateToolsbarStatus();
            return;
        }
        Object actionValue = this.control.getActionValue(EventConstant.PG_SLIDESHOW, null);
        if (actionValue == null || !((Boolean) actionValue).booleanValue()) {
            if (this.control.getReader() != null) {
                this.control.getReader().abortReader();
            }
            MainControl mainControl = this.control;
            if (mainControl == null || !mainControl.isAutoTest()) {

                super.onBackPressed();
            } else {
                System.exit(0);
            }
        } else {
            fullScreen(false);
            this.control.actionEvent(EventConstant.PG_SLIDESHOW_END, null);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (isSearchbarActive()) {
            this.searchBar.onConfigurationChanged(configuration);
        }
    }


    public void onDestroy() {

        super.onDestroy();
    }

    public void showProgressBar(boolean z) {
        setProgressBarIndeterminateVisibility(false);
    }


    public void init() {
        this.toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        getIntent();
        if (getIntent().getExtras() != null) {
            String str = "filepath";
            if (getIntent().getExtras().containsKey(str)) {
                this.filePath = getIntent().getExtras().getString(str);
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PPTViewData.this.finish();
            }
        });
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle((CharSequence) FilesDataade.getFileName(this.filePath));
        createView();
        this.control.openFile(this.filePath);
        initMarked();
    }

    public boolean isPopUpErrorDlg() {
        this.mPageCountView.setVisibility(View.GONE);
        this.RRloading.setVisibility(View.GONE);
        return true;
    }

    private void createView() {
        String lowerCase = this.filePath.toLowerCase();
        if (lowerCase.endsWith(MainConstant.FILE_TYPE_PPT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTM)) {
            this.applicationType = 2;
            this.toolsbar = new PGToolsbar(getApplicationContext(), (IControl) this.control);
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_XLS) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLT) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSM)) {
            this.applicationType = 1;
            this.toolsbar = new SSToolsbar(getApplicationContext(), (IControl) this.control);
        } else {
            this.applicationType = 0;
            this.toolsbar = new WPToolsbar(getApplicationContext(), this.control);
        }
        this.appFrame.addView(this.toolsbar);
    }

    private boolean isSearchbarActive() {
        LinearLayout linearLayout = this.appFrame;
        boolean z = false;
        if (linearLayout != null && !this.isDispose) {
            int childCount = linearLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.appFrame.getChildAt(i);
                if (childAt instanceof FindToolBar) {
                    if (childAt.getVisibility() == View.GONE) {
                        z = true;
                    }
                    return z;
                }
            }
        }
        return false;
    }

    public void showSearchBar(boolean z) {
        if (z) {
            if (this.searchBar == null) {
                this.searchBar = new FindToolBar(this, this.control);
                this.appFrame.addView(this.searchBar, 0);
            }
            this.searchBar.setVisibility(View.GONE);
            this.toolsbar.setVisibility(View.GONE);
            return;
        }
        FindToolBar findToolBar = this.searchBar;
        if (findToolBar != null) {
            findToolBar.setVisibility(View.GONE);
        }
        this.toolsbar.setVisibility(View.GONE);
    }

    public void showCalloutToolsBar(boolean z) {
        if (z) {
            if (this.calloutBar == null) {
                this.calloutBar = new CalloutToolsbar(getApplicationContext(), (IControl) this.control);
            }
            this.calloutBar.setCheckState(EventConstant.APP_PEN_ID, (short) 1);
            this.calloutBar.setCheckState(EventConstant.APP_ERASER_ID, (short) 2);
            this.calloutBar.setVisibility(View.GONE);
            this.toolsbar.setVisibility(View.GONE);
            return;
        }
        CalloutToolsbar calloutToolsbar = this.calloutBar;
        if (calloutToolsbar != null) {
            calloutToolsbar.setVisibility(View.GONE);
        }
        this.toolsbar.setVisibility(View.GONE);
    }

    public void setPenUnChecked() {
        if (this.fullscreen) {
            this.penButton.setState((short) 2);
            this.penButton.postInvalidate();
            return;
        }
        this.calloutBar.setCheckState(EventConstant.APP_PEN_ID, (short) 2);
        this.calloutBar.postInvalidate();
    }

    public void setEraserUnChecked() {
        if (this.fullscreen) {
            this.eraserButton.setState((short) 2);
            this.eraserButton.postInvalidate();
            return;
        }
        this.calloutBar.setCheckState(EventConstant.APP_ERASER_ID, (short) 2);
        this.calloutBar.postInvalidate();
    }

    public void setFindBackForwardState(boolean z) {
        if (isSearchbarActive()) {
            this.searchBar.setEnabled(EventConstant.APP_FIND_BACKWARD, z);
            this.searchBar.setEnabled(EventConstant.APP_FIND_FORWARD, z);
        }
    }

    public void fileShare() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Uri.fromFile(new File(this.filePath)));
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        intent.putExtra("android.intent.extra.STREAM", arrayList);
        intent.setType("application/octet-stream");
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.sys_share_title)));
    }

    private void markFile() {
        this.marked = !this.marked;
    }

    public void resetTitle(String str) {
        if (str != null) {
            setTitle(str);
        }
    }

    public FindToolBar getSearchBar() {
        return this.searchBar;
    }

    public Dialog onCreateDialog(int i) {
        return this.control.getDialog(this, i);
    }

    public void updateToolsbarStatus() {
        LinearLayout linearLayout = this.appFrame;
        if (linearLayout != null && !this.isDispose) {
            int childCount = linearLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.appFrame.getChildAt(i);
                if (childAt instanceof AToolsbar) {
                    ((AToolsbar) childAt).updateStatus();
                }
            }
        }
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

    public boolean doActionEvent(int i, Object obj) {
        if (i == 0) {
            onBackPressed();
        } else if (i == 15) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getResources().getString(R.string.sys_url_arcsoft))));
        } else if (i == 20) {
            updateToolsbarStatus();
        } else if (i == 25) {
            setTitle((String) obj);
        } else if (i == 268435464) {
            markFile();
        } else if (i == 1073741828) {
            this.bottomBar.setFocusSheetButton(((Integer) obj).intValue());
        } else if (i == 536870912) {
            showSearchBar(true);
        } else if (i != 536870913) {
            switch (i) {
                case EventConstant.APP_DRAW_ID /*536870937*/:
                    showCalloutToolsBar(true);
                    this.control.getSysKit().getCalloutManager().setDrawingMode(1);
                    this.appFrame.post(new Runnable() {
                        public void run() {
                            PPTViewData.this.control.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null);
                        }
                    });
                    break;
                case EventConstant.APP_BACK_ID /*536870938*/:
                    showCalloutToolsBar(false);
                    this.control.getSysKit().getCalloutManager().setDrawingMode(0);
                    break;
                case EventConstant.APP_PEN_ID /*536870939*/:
                    if (!((Boolean) obj).booleanValue()) {
                        this.control.getSysKit().getCalloutManager().setDrawingMode(0);
                        break;
                    } else {
                        this.control.getSysKit().getCalloutManager().setDrawingMode(1);
                        setEraserUnChecked();
                        this.appFrame.post(new Runnable() {
                            public void run() {
                                PPTViewData.this.control.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null);
                            }
                        });
                        break;
                    }
                case EventConstant.APP_ERASER_ID /*536870940*/:
                    if (!((Boolean) obj).booleanValue()) {
                        this.control.getSysKit().getCalloutManager().setDrawingMode(0);
                        break;
                    } else {
                        this.control.getSysKit().getCalloutManager().setDrawingMode(2);
                        setPenUnChecked();
                        break;
                    }
                case EventConstant.APP_COLOR_ID /*536870941*/:
                    break;
                default:
                    switch (i) {
                        case EventConstant.APP_FINDING /*788529152*/:
                            String trim = ((String) obj).trim();
                            if (trim.length() > 0 && this.control.getFind().find(trim)) {
                                setFindBackForwardState(true);
                                break;
                            } else {
                                setFindBackForwardState(false);
                                this.toast.setText(getLocalString("DIALOG_FIND_NOT_FOUND"));
                                this.toast.show();
                                break;
                            }
                        case EventConstant.APP_FIND_BACKWARD /*788529153*/:
                            if (this.control.getFind().findBackward()) {
                                this.searchBar.setEnabled(EventConstant.APP_FIND_FORWARD, true);
                                break;
                            } else {
                                this.searchBar.setEnabled(EventConstant.APP_FIND_BACKWARD, false);
                                this.toast.setText(getLocalString("DIALOG_FIND_TO_BEGIN"));
                                this.toast.show();
                                break;
                            }
                        case EventConstant.APP_FIND_FORWARD /*788529154*/:
                            try {
                                if (this.control.getFind().findForward()) {
                                    this.searchBar.setEnabled(EventConstant.APP_FIND_BACKWARD, true);
                                    break;
                                } else {
                                    this.searchBar.setEnabled(EventConstant.APP_FIND_FORWARD, false);
                                    this.toast.setText(getLocalString("DIALOG_FIND_TO_END"));
                                    this.toast.show();
                                    break;
                                }
                            } catch (Exception e) {
                                this.control.getSysKit().getErrorKit().writerLog(e);
                                break;
                            }
                        default:
                            return false;
                    }
            }
        } else {
            fileShare();
        }
        return true;
    }

    public void openFileFinish() {
        this.RRloading.setVisibility(View.INVISIBLE);
        this.gapView = new View(getApplicationContext());
        this.gapView.setBackgroundColor(Color.parseColor("#EEEEEE"));
        this.appFrame.addView(this.gapView, new LinearLayout.LayoutParams(-1, 1));
//        this.jump_to_page.setVisible(true);
        this.mPageCountView.setVisibility(View.VISIBLE);
        this.appFrame.addView(this.control.getView(), new LinearLayout.LayoutParams(-1, -1));
//        if (this.menu_item_other_app_file_opener == null) {
//            return;
//        }
//        if (this.intentAction.equals("a")) {
//            this.menu_item_other_app_file_opener.setVisible(true);
//        } else {
//            this.menu_item_other_app_file_opener.setVisible(false);
//        }
    }

    public int getBottomBarHeight() {
        SheetBar sheetBar = this.bottomBar;
        if (sheetBar != null) {
            return sheetBar.getSheetbarHeight();
        }
        return 0;
    }

    public void changePage(int i, int i2) {
        this.totalPageCount = i2;
        if (this.mPageCountView != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append(" / ");
            sb.append(i2);
            String sb2 = sb.toString();
            if (this.mPageCountView.getVisibility() != View.VISIBLE) {
                this.mPageCountView.setVisibility(View.VISIBLE);
            }
            this.mPageCountView.setText(sb2);
        }
    }

    public String getAppName() {
        return getString(R.string.app_name);
    }

    private void initFloatButton() {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        this.wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.wmParams = new LayoutParams();
        LayoutParams layoutParams = this.wmParams;
        layoutParams.type = 2002;
        layoutParams.format = 1;
        layoutParams.flags = 40;
        layoutParams.width = options.outWidth;
        this.wmParams.height = options.outHeight;
    }

    public void fullScreen(boolean z) {
        this.fullscreen = z;
        if (z) {
            if (this.wm == null || this.wmParams == null) {
                initFloatButton();
            }
            LayoutParams layoutParams = this.wmParams;
            layoutParams.gravity = 53;
            layoutParams.x = 5;
            this.wm.addView(this.penButton, layoutParams);
            LayoutParams layoutParams2 = this.wmParams;
            layoutParams2.gravity = 53;
            layoutParams2.x = 5;
            layoutParams2.y = layoutParams2.height;
            this.wm.addView(this.eraserButton, this.wmParams);
            LayoutParams layoutParams3 = this.wmParams;
            layoutParams3.gravity = 53;
            layoutParams3.x = 5;
            layoutParams3.y = layoutParams3.height * 2;
            this.wm.addView(this.settingsButton, this.wmParams);
            LayoutParams layoutParams4 = this.wmParams;
            layoutParams4.gravity = 19;
            layoutParams4.x = 5;
            layoutParams4.y = 0;
            this.wm.addView(this.pageUp, layoutParams4);
            LayoutParams layoutParams5 = this.wmParams;
            layoutParams5.gravity = 21;
            this.wm.addView(this.pageDown, layoutParams5);
            ((View) getWindow().findViewById(16908310).getParent()).setVisibility(View.GONE);
            this.toolsbar.setVisibility(View.GONE);
            this.gapView.setVisibility(View.GONE);
            this.penButton.setState((short) 2);
            this.eraserButton.setState((short) 2);
            LayoutParams attributes = getWindow().getAttributes();
            attributes.flags |= 1024;
            getWindow().setAttributes(attributes);
            getWindow().addFlags(512);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            return;
        }
        this.wm.removeView(this.pageUp);
        this.wm.removeView(this.pageDown);
        this.wm.removeView(this.penButton);
        this.wm.removeView(this.eraserButton);
        this.wm.removeView(this.settingsButton);
        ((View) getWindow().findViewById(16908310).getParent()).setVisibility(View.GONE);
        this.toolsbar.setVisibility(View.GONE);
        this.gapView.setVisibility(View.GONE);
        LayoutParams attributes2 = getWindow().getAttributes();
        attributes2.flags &= -1025;
        getWindow().setAttributes(attributes2);
        getWindow().clearFlags(512);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    public void error(int i) {
        this.mPageCountView.setVisibility(View.GONE);
        this.RRloading.setVisibility(View.GONE);
    }

    public void destroyEngine() {
        super.onBackPressed();
    }

    public String getLocalString(String str) {
        return ResKit.instance().getLocalString(str);
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

    public void dispose() {
        this.isDispose = true;
        MainControl mainControl = this.control;
        if (mainControl != null) {
            mainControl.dispose();
            this.control = null;
        }
        this.toolsbar = null;
        this.searchBar = null;
        this.bottomBar = null;
        LinearLayout linearLayout = this.appFrame;
        if (linearLayout != null) {
            int childCount = linearLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.appFrame.getChildAt(i);
                if (childAt instanceof AToolsbar) {
                    ((AToolsbar) childAt).dispose();
                }
            }
            this.appFrame = null;
        }
        if (this.wm != null) {
            this.wm = null;
            this.wmParams = null;
            this.pageUp.dispose();
            this.pageDown.dispose();
            this.penButton.dispose();
            this.eraserButton.dispose();
            this.settingsButton.dispose();
            this.pageUp = null;
            this.pageDown = null;
            this.penButton = null;
            this.eraserButton = null;
            this.settingsButton = null;
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
