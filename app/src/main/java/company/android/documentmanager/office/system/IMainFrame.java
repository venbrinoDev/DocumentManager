/*
 * 文件名称:          IActivity.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:24:10
 */
package company.android.documentmanager.office.system;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

/**
 * activity interface
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2012-5-15
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public interface IMainFrame {
    public static final byte ON_CLICK = 10;
    public static final byte ON_DOUBLE_TAP = 8;
    public static final byte ON_DOUBLE_TAP_EVENT = 9;
    public static final byte ON_DOWN = 1;
    public static final byte ON_FLING = 6;
    public static final byte ON_LONG_PRESS = 5;
    public static final byte ON_SCROLL = 4;
    public static final byte ON_SHOW_PRESS = 2;
    public static final byte ON_SINGLE_TAP_CONFIRMED = 7;
    public static final byte ON_SINGLE_TAP_UP = 3;
    public static final byte ON_TOUCH = 0;

    void changePage();

    void changePage(int i, int i2);

    void changeZoom();

    void completeLayout();

    void dispose();

    boolean doActionEvent(int i, Object obj);

    void error(int i);

    void fullScreen(boolean z);

    Activity getActivity();

    String getAppName();

    int getBottomBarHeight();

    String getLocalString(String str);

    byte getPageListViewMovingPosition();

    String getTXTDefaultEncode();

    File getTemporaryDirectory();

    int getTopBarHeight();

    Object getViewBackground();

    byte getWordDefaultView();

    boolean isChangePage();

    boolean isDrawPageNumber();

    boolean isIgnoreOriginalSize();

    boolean isPopUpErrorDlg();

    boolean isShowFindDlg();

    boolean isShowPasswordDlg();

    boolean isShowProgressBar();

    boolean isShowTXTEncodeDlg();

    boolean isShowZoomingMsg();

    boolean isThumbnail();

    boolean isTouchZoom();

    boolean isWriteLog();

    boolean isZoomAfterLayoutForWord();

    boolean onEventMethod(View view, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, byte b);

    void openFileFinish();

    void setFindBackForwardState(boolean z);

    void setIgnoreOriginalSize(boolean z);

    void setThumbnail(boolean z);

    void setWriteLog(boolean z);

    void showProgressBar(boolean z);

    void updateToolsbarStatus();

    void updateViewImages(List<Integer> list);
}
