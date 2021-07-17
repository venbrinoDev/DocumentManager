/*
 * 文件名称:          AbstractMainFrame.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:08:07
 */
package company.android.documentmanager.office.macro;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import company.android.documentmanager.office.res.ResKit;
import company.android.documentmanager.office.system.IMainFrame;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

class MacroFrame implements IMainFrame {
    private Activity activity;
    private Application app;
    private String appName;
    private Object bg = Integer.valueOf(-7829368);
    private int bottomBarHeight;
    private ErrorListener errorListener;
    private boolean ignoreOriginalSize = false;
    private boolean isChangePage = true;
    private boolean isDrawPageNumber = true;
    private boolean isThumbnail;
    private boolean isTouchZoom = true;
    private boolean isZoomAfterLayoutForWord = true;
    private OpenFileFinishListener openFileFinishListener;
    private byte pageListViewMovingPosition = 0;
    private boolean popupErrorDlg = true;
    private Map<String, Integer> resI18N;
    private boolean showFindDlg = true;
    private boolean showPasswordDlg = true;
    private boolean showProgressbarDlg = true;
    private boolean showTXTEncodeDlg = true;
    private boolean showZoomingMsg = true;
    private int topBarHeight;
    private TouchEventListener touchEventListener;
    private String txtDefalutEncode;
    private UpdateStatusListener updateStatusListener;
    private byte wordDefaultView;
    private boolean writeLog = true;

    public void changePage(int i, int i2) {
    }

    public boolean doActionEvent(int i, Object obj) {
        return false;
    }

    public void fullScreen(boolean z) {
    }

    public void setFindBackForwardState(boolean z) {
    }

    public MacroFrame(Application application, Activity activity2) {
        this.app = application;
        this.activity = activity2;
        int i = activity2.getApplication().getApplicationInfo().labelRes;
        if (i > 0) {
            this.appName = activity2.getResources().getString(i);
        }
    }

    public Activity getActivity() {
        return this.activity;
    }


    public void addTouchEventListener(TouchEventListener touchEventListener2) {
        this.touchEventListener = touchEventListener2;
    }

    public void addUpdateStatusListener(UpdateStatusListener updateStatusListener2) {
        this.updateStatusListener = updateStatusListener2;
    }

    public void addOpenFileFinishListener(OpenFileFinishListener openFileFinishListener2) {
        this.openFileFinishListener = openFileFinishListener2;
    }

    public void addErrorListener(ErrorListener errorListener2) {
        this.errorListener = errorListener2;
    }

    public File getTemporaryDirectory() {
        Activity activity2 = this.activity;
        if (activity2 == null) {
            return null;
        }
        File externalFilesDir = activity2.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            return externalFilesDir;
        }
        return this.activity.getFilesDir();
    }

    public boolean onEventMethod(View view, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, byte b) {
        TouchEventListener touchEventListener2 = this.touchEventListener;
        if (touchEventListener2 != null) {
            touchEventListener2.onEventMethod(view, motionEvent, motionEvent2, f, f2, b);
        }
        return false;
    }

    public void updateToolsbarStatus() {
        UpdateStatusListener updateStatusListener2 = this.updateStatusListener;
        if (updateStatusListener2 != null) {
            updateStatusListener2.updateStatus();
        }
    }

    public void changeZoom() {
        UpdateStatusListener updateStatusListener2 = this.updateStatusListener;
        if (updateStatusListener2 != null) {
            updateStatusListener2.changeZoom();
        }
    }

    public void changePage() {
        UpdateStatusListener updateStatusListener2 = this.updateStatusListener;
        if (updateStatusListener2 != null) {
            updateStatusListener2.changePage();
        }
    }

    public void completeLayout() {
        UpdateStatusListener updateStatusListener2 = this.updateStatusListener;
        if (updateStatusListener2 != null) {
            updateStatusListener2.completeLayout();
        }
    }

    public void showProgressBar(boolean z) {
        if (this.showProgressbarDlg) {
            this.activity.setProgressBarIndeterminateVisibility(z);
        }
    }

    public void updateViewImages(List<Integer> list) {
        UpdateStatusListener updateStatusListener2 = this.updateStatusListener;
        if (updateStatusListener2 != null) {
            updateStatusListener2.updateViewImage((Integer[]) list.toArray(new Integer[list.size()]));
        }
    }

    public void openFileFinish() {
        this.app.openFileFinish();
        OpenFileFinishListener openFileFinishListener2 = this.openFileFinishListener;
        if (openFileFinishListener2 != null) {
            openFileFinishListener2.openFileFinish();
        }
    }

    public void error(int i) {
        ErrorListener errorListener2 = this.errorListener;
        if (errorListener2 != null) {
            errorListener2.error(i);
        }
    }

    public void destroyEngine() {
        ErrorListener errorListener2 = this.errorListener;
    }

    public int getBottomBarHeight() {
        return this.bottomBarHeight;
    }

    public void setBottomBarHeight(int i) {
        this.bottomBarHeight = i;
    }

    public int getTopBarHeight() {
        return this.topBarHeight;
    }

    public void setTopBarHeight(int i) {
        this.topBarHeight = i;
    }

    public String getAppName() {
        String str = this.appName;
        return str == null ? "ArcSoft" : str;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public boolean isDrawPageNumber() {
        return this.isDrawPageNumber;
    }

    public void setDrawPageNumber(boolean z) {
        this.isDrawPageNumber = z;
    }

    public boolean isShowZoomingMsg() {
        return this.showZoomingMsg;
    }

    public void setShowZoomingMsg(boolean z) {
        this.showZoomingMsg = z;
    }

    public boolean isPopUpErrorDlg() {
        return this.popupErrorDlg;
    }

    public void setShowPasswordDlg(boolean z) {
        this.showPasswordDlg = z;
    }

    public boolean isShowPasswordDlg() {
        return this.showPasswordDlg;
    }

    public void setShowProgressBar(boolean z) {
        this.showProgressbarDlg = z;
    }

    public boolean isShowProgressBar() {
        return this.showProgressbarDlg;
    }

    public void setShowFindDlg(boolean z) {
        this.showFindDlg = z;
    }

    public boolean isShowFindDlg() {
        return this.showFindDlg;
    }

    public void setShowTXTEncodeDlg(boolean z) {
        this.showTXTEncodeDlg = z;
    }

    public boolean isShowTXTEncodeDlg() {
        return this.showTXTEncodeDlg;
    }

    public void setTXTDefaultEncode(String str) {
        this.txtDefalutEncode = str;
    }

    public String getTXTDefaultEncode() {
        if (!this.showTXTEncodeDlg) {
            return this.txtDefalutEncode;
        }
        return null;
    }

    public void setPopUpErrorDlg(boolean z) {
        this.popupErrorDlg = z;
    }

    public boolean isTouchZoom() {
        return this.isTouchZoom;
    }

    public void setTouchZoom(boolean z) {
        this.isTouchZoom = z;
    }

    public boolean isZoomAfterLayoutForWord() {
        return this.isZoomAfterLayoutForWord;
    }

    public void setZoomAfterLayoutForWord(boolean z) {
        this.isZoomAfterLayoutForWord = z;
    }

    public byte getWordDefaultView() {
        return this.wordDefaultView;
    }

    public void setWordDefaultView(byte b) {
        this.wordDefaultView = b;
    }

    public void setChangePage(boolean z) {
        this.isChangePage = z;
    }

    public boolean isChangePage() {
        return this.isChangePage;
    }

    public String getLocalString(String str) {
        Field[] declaredFields;
        String str2 = null;
        if (this.resI18N == null) {
            this.resI18N = new HashMap();
            try {
                String packageName = this.activity.getPackageName();
                StringBuilder sb = new StringBuilder();
                sb.append(packageName);
                sb.append(".R$string");
                for (Field field : Class.forName(sb.toString()).getDeclaredFields()) {
                    String upperCase = field.getName().toUpperCase();
                    if (ResKit.instance().hasResName(upperCase)) {
                        this.resI18N.put(upperCase, Integer.valueOf(field.getInt(null)));
                    }
                }
            } catch (Exception unused) {
            }
        }
        Integer num = (Integer) this.resI18N.get(str);
        if (num != null) {
            str2 = this.activity.getResources().getString(num.intValue());
        }
        return (str2 == null || str2.length() == 0) ? ResKit.instance().getLocalString(str) : str2;
    }


    public void addI18NResID(String str, int i) {
        if (this.resI18N == null) {
            this.resI18N = new HashMap();
        }
        this.resI18N.put(str, Integer.valueOf(i));
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

    public boolean isThumbnail() {
        return this.isThumbnail;
    }

    public void setViewBackground(Object obj) {
        this.bg = obj;
    }

    public Object getViewBackground() {
        return this.bg;
    }

    public void setIgnoreOriginalSize(boolean z) {
        this.ignoreOriginalSize = z;
    }

    public boolean isIgnoreOriginalSize() {
        return this.ignoreOriginalSize;
    }

    public void setPageListViewMovingPosition(byte b) {
        this.pageListViewMovingPosition = b;
    }

    public byte getPageListViewMovingPosition() {
        return this.pageListViewMovingPosition;
    }

    public void dispose() {
        this.app = null;
        this.activity = null;
        this.updateStatusListener = null;
        this.touchEventListener = null;
        this.errorListener = null;
        this.openFileFinishListener = null;
        this.txtDefalutEncode = null;
    }
}