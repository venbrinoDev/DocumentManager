/*
 * 文件名称:           MainControl.java
 * 
 * 编译器:             android2.2
 * 时间:               下午1:34:44
 */

package company.android.documentmanager.office.officereader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import company.android.documentmanager.R;
import company.android.documentmanager.office.DataaaObjectUtil;
import company.android.documentmanager.office.mycommsmoms.IOfficeToPicture;
import company.android.documentmanager.office.valconstttaa.EventConstant;
import company.android.documentmanager.office.valconstttaa.MainConstant;
import company.android.documentmanager.office.macro.DialogListener;
import company.android.documentmanager.office.officereader.beans.AImageButton;
import company.android.documentmanager.office.officereader.beans.AImageCheckButton;
import company.android.documentmanager.office.officereader.beans.AToolsbar;
import company.android.documentmanager.office.officereader.beans.CalloutToolsbar;
import company.android.documentmanager.office.officereader.beans.PDFToolsbar;
import company.android.documentmanager.office.officereader.beans.PGToolsbar;
import company.android.documentmanager.office.officereader.beans.SSToolsbar;
import company.android.documentmanager.office.officereader.beans.WPToolsbar;
import company.android.documentmanager.office.officereader.database.DBService;
import company.android.documentmanager.office.pg.control.PGControl;
import company.android.documentmanager.office.res.ResConstant;
import company.android.documentmanager.office.res.ResKit;
import company.android.documentmanager.office.ss.sheetbar.SheetBar;
import company.android.documentmanager.office.system.FileKit;
import company.android.documentmanager.office.system.IControl;
import company.android.documentmanager.office.system.IMainFrame;
import company.android.documentmanager.office.system.MainControl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppActivity extends Activity implements IMainFrame {
    private LinearLayout appFrame;
    private int applicationType = -1;
    private Object bg = Integer.valueOf(-7829368);
    private SheetBar bottomBar;
    private CalloutToolsbar calloutBar;

    public MainControl control;
    private DBService dbService;
    private AImageCheckButton eraserButton;
    private String filePath;
    private boolean fullscreen;
    private View gapView;
    private boolean isDispose;
    private boolean isThumbnail;
    private boolean marked;
    private AImageButton pageDown;
    private AImageButton pageUp;
    private AImageCheckButton penButton;
    private FindToolBar searchBar;
    private AImageButton settingsButton;
    private String tempFilePath;
    private Toast toast;
    private AToolsbar toolsbar;
    private WindowManager wm = null;
    private LayoutParams wmParams = null;
    private boolean writeLog = true;

    class CustomListener implements OnClickListener {
        private final Dialog dialog;
        EditText text;

        public CustomListener(Dialog dialog2, EditText editText) {
            this.dialog = dialog2;
            this.text = editText;
        }

        public void onClick(View view) {
            int pageCount = ((PGControl) AppActivity.this.control.getAppControl()).getPageCount();
            if (this.text.getText().toString().trim().length() <= 0) {
                AppActivity.dialogWarning(AppActivity.this, "Please enter a page number");
            } else if (pageCount >= Integer.parseInt(this.text.getText().toString())) {
                if (!DataaaObjectUtil.isEmpty(this.text.getText().toString().trim())) {
                    int i = 0;
                    try {
                        i = Integer.parseInt(this.text.getText().toString());
                    } catch (Exception unused) {
                    }
                    if (i > 0 && !DataaaObjectUtil.isNull(AppActivity.this.control) && (AppActivity.this.control.getAppControl() instanceof PGControl)) {
                        ((PGControl) AppActivity.this.control.getAppControl()).showSlidePage(i - 1);
                    }
                }
                this.dialog.dismiss();
            } else {
                AppActivity.dialogWarning(AppActivity.this, "Please enter a valid page number.");
            }
        }
    }

    public void changePage() {
    }

    public void changeZoom() {
    }

    public void completeLayout() {
    }

    public void error(int i) {
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

    public boolean isChangePage() {
        return true;
    }

    public boolean isDrawPageNumber() {
        return true;
    }

    public boolean isIgnoreOriginalSize() {
        return false;
    }

    public boolean isPopUpErrorDlg() {
        return true;
    }

    public boolean isShowFindDlg() {
        return true;
    }

    public boolean isShowPasswordDlg() {
        return true;
    }

    public boolean isShowProgressBar() {
        return true;
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

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(5);
        setContentView(R.layout.activity_ppt_view);
        this.control = new MainControl(this);
        this.appFrame = (LinearLayout) findViewById(R.id.llmainframe);
        this.appFrame.post(new Runnable() {
            public void run() {
                AppActivity.this.init();
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
                AppActivity.this.saveBitmapToFile(bitmap2);
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

    public void setButtonEnabled(boolean z) {
        if (this.fullscreen) {
            this.pageUp.setEnabled(z);
            this.pageDown.setEnabled(z);
            this.penButton.setEnabled(z);
            this.eraserButton.setEnabled(z);
            this.settingsButton.setEnabled(z);
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
            boolean z = this.marked;
            DBService dBService = this.dbService;
            String str = this.filePath;
            String str2 = MainConstant.TABLE_STAR;
            if (z != dBService.queryItem(str2, str)) {
                if (!this.marked) {
                    this.dbService.deleteItem(str2, this.filePath);
                } else {
                    this.dbService.insertStarFiles(str2, this.filePath);
                }
                Intent intent = new Intent();
                intent.putExtra(MainConstant.INTENT_FILED_MARK_STATUS, this.marked);
                setResult(-1, intent);
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
        dispose();
        super.onDestroy();
    }

    public void showProgressBar(boolean z) {
        setProgressBarIndeterminateVisibility(z);
    }


    public void init() {
        this.toast = Toast.makeText(getApplicationContext(), "", 0);
        Intent intent = getIntent();
        this.dbService = new DBService(getApplicationContext());
        this.filePath = "/storage/emulated/0/test/june.xlsx";
        if (this.filePath == null) {
            this.filePath = intent.getDataString();
            int indexOf = getFilePath().indexOf(":");
            if (indexOf > 0) {
                this.filePath = this.filePath.substring(indexOf + 3);
            }
            this.filePath = Uri.decode(this.filePath);
        }
        int lastIndexOf = this.filePath.lastIndexOf(File.separator);
        if (lastIndexOf > 0) {
            setTitle(this.filePath.substring(lastIndexOf + 1));
        } else {
            setTitle(this.filePath);
        }
        if (FileKit.instance().isSupport(this.filePath)) {
            this.dbService.insertRecentFiles(MainConstant.TABLE_RECENT, this.filePath);
        }
        createView();
        this.control.openFile(this.filePath);
        initMarked();
    }

    private void createView() {
        String lowerCase = this.filePath.toLowerCase();
        if (lowerCase.endsWith(MainConstant.FILE_TYPE_DOC) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOCX) || lowerCase.endsWith(MainConstant.FILE_TYPE_TXT) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOT) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTM)) {
            this.applicationType = 0;
            this.toolsbar = new WPToolsbar(getApplicationContext(), this.control);
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_XLS) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLT) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSM)) {
            this.applicationType = 1;
            this.toolsbar = new SSToolsbar(getApplicationContext(), (IControl) this.control);
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_PPT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTM)) {
            this.applicationType = 2;
            this.toolsbar = new PGToolsbar(getApplicationContext(), (IControl) this.control);
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_PDF)) {
            this.applicationType = 3;
            this.toolsbar = new PDFToolsbar(getApplicationContext(), this.control);
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
                    if (childAt.getVisibility() == 0) {
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
            this.searchBar.setVisibility(0);
            this.toolsbar.setVisibility(8);
            return;
        }
        FindToolBar findToolBar = this.searchBar;
        if (findToolBar != null) {
            findToolBar.setVisibility(8);
        }
        this.toolsbar.setVisibility(0);
    }

    public void showCalloutToolsBar(boolean z) {
        if (z) {
            if (this.calloutBar == null) {
                this.calloutBar = new CalloutToolsbar(getApplicationContext(), (IControl) this.control);
                this.appFrame.addView(this.calloutBar, 0);
            }
            this.calloutBar.setCheckState(EventConstant.APP_PEN_ID, (short) 1);
            this.calloutBar.setCheckState(EventConstant.APP_ERASER_ID, (short) 2);
            this.calloutBar.setVisibility(0);
            this.toolsbar.setVisibility(8);
            return;
        }
        CalloutToolsbar calloutToolsbar = this.calloutBar;
        if (calloutToolsbar != null) {
            calloutToolsbar.setVisibility(8);
        }
        this.toolsbar.setVisibility(0);
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

    public void initMarked() {
        this.marked = this.dbService.queryItem(MainConstant.TABLE_STAR, this.filePath);
        if (this.marked) {
            this.toolsbar.setCheckState(EventConstant.FILE_MARK_STAR_ID, (short) 1);
        } else {
            this.toolsbar.setCheckState(EventConstant.FILE_MARK_STAR_ID, (short) 2);
        }
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
                            AppActivity.this.control.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null);
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
                                AppActivity.this.control.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null);
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
        Resources resources = getResources();
        AImageButton aImageButton = new AImageButton(this, this.control, resources.getString(R.string.pg_slideshow_pageup), -1, -1, EventConstant.APP_PAGE_UP_ID);
        this.pageUp = aImageButton;
        this.pageUp.setNormalBgResID(R.drawable.icon_folder);
        this.pageUp.setPushBgResID(R.drawable.icon_folder);
        this.pageUp.setLayoutParams(new LinearLayout.LayoutParams(options.outWidth, options.outHeight));
        AImageButton aImageButton2 = new AImageButton(this, this.control, resources.getString(R.string.pg_slideshow_pagedown), -1, -1, EventConstant.APP_PAGE_DOWN_ID);
        this.pageDown = aImageButton2;
        this.pageDown.setNormalBgResID(R.drawable.icon_folder);
        this.pageDown.setPushBgResID(R.drawable.icon_folder);
        this.pageDown.setLayoutParams(new LinearLayout.LayoutParams(options.outWidth, options.outHeight));
        BitmapFactory.decodeResource(getResources(), R.drawable.icon_folder, options);
        AImageCheckButton aImageCheckButton = new AImageCheckButton(this, this.control, resources.getString(R.string.app_toolsbar_pen_check), resources.getString(R.string.app_toolsbar_pen), R.drawable.icon_folder, R.drawable.icon_folder, R.drawable.icon_folder, EventConstant.APP_PEN_ID);
        this.penButton = aImageCheckButton;
        this.penButton.setNormalBgResID(R.drawable.icon_folder);
        this.penButton.setPushBgResID(R.drawable.icon_folder);
        this.penButton.setLayoutParams(new LinearLayout.LayoutParams(options.outWidth, options.outHeight));
        AImageCheckButton aImageCheckButton2 = new AImageCheckButton(this, this.control, resources.getString(R.string.app_toolsbar_eraser_check), resources.getString(R.string.app_toolsbar_eraser), R.drawable.icon_folder, R.drawable.icon_folder, R.drawable.icon_folder, EventConstant.APP_ERASER_ID);
        this.eraserButton = aImageCheckButton2;
        this.eraserButton.setNormalBgResID(R.drawable.icon_folder);
        this.eraserButton.setPushBgResID(R.drawable.icon_folder);
        this.eraserButton.setLayoutParams(new LinearLayout.LayoutParams(options.outWidth, options.outHeight));
        AImageButton aImageButton3 = new AImageButton(this, this.control, resources.getString(R.string.app_toolsbar_color), -1, -1, EventConstant.APP_COLOR_ID);
        this.settingsButton = aImageButton3;
        this.settingsButton.setNormalBgResID(R.drawable.icon_folder);
        this.settingsButton.setPushBgResID(R.drawable.icon_folder);
        this.settingsButton.setLayoutParams(new LinearLayout.LayoutParams(options.outWidth, options.outHeight));
        this.wm = (WindowManager) getApplicationContext().getSystemService("window");
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
            ((View) getWindow().findViewById(16908310).getParent()).setVisibility(8);
            this.toolsbar.setVisibility(8);
            this.gapView.setVisibility(8);
            this.penButton.setState((short) 2);
            this.eraserButton.setState((short) 2);
            LayoutParams attributes = getWindow().getAttributes();
            attributes.flags |= 1024;
            getWindow().setAttributes(attributes);
            getWindow().addFlags(512);
            setRequestedOrientation(0);
            return;
        }
        this.wm.removeView(this.pageUp);
        this.wm.removeView(this.pageDown);
        this.wm.removeView(this.penButton);
        this.wm.removeView(this.eraserButton);
        this.wm.removeView(this.settingsButton);
        ((View) getWindow().findViewById(16908310).getParent()).setVisibility(0);
        this.toolsbar.setVisibility(0);
        this.gapView.setVisibility(0);
        LayoutParams attributes2 = getWindow().getAttributes();
        attributes2.flags &= -1025;
        getWindow().setAttributes(attributes2);
        getWindow().clearFlags(512);
        setRequestedOrientation(4);
    }

    public void destroyEngine() {
        super.onBackPressed();
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
        Toast.makeText(this, sb2.toString(), 0).show();
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
        DBService dBService = this.dbService;
        if (dBService != null) {
            dBService.dispose();
            this.dbService = null;
        }
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

    private void openJumpPageDialog() {
        if (!DataaaObjectUtil.isNull(this.control)) {
            Builder builder = new Builder(this);
            builder.setTitle("Enter Page Number to Jump");
            View inflate = getLayoutInflater().inflate(R.layout.alert_label_editor, null);
            builder.setView(inflate);
            EditText editText = (EditText) inflate.findViewById(R.id.label_field);
            builder.setPositiveButton(ResConstant.BUTTON_OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog create = builder.create();
            create.show();
            create.getButton(-1).setOnClickListener(new CustomListener(create, editText));
        }
    }

    public static void dialogWarning(Activity activity, String str) {
        Builder builder = new Builder(activity);
        builder.setTitle("Warning");
        builder.setMessage(str);
        builder.setPositiveButton(ResConstant.BUTTON_OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }
}
