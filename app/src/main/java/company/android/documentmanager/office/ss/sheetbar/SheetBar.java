/*
 * 文件名称:          SheetList.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:13:50
 */
package company.android.documentmanager.office.ss.sheetbar;

import java.util.Vector;

import company.android.documentmanager.R;
import company.android.documentmanager.office.valconstttaa.EventConstant;
import company.android.documentmanager.office.system.IControl;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-11-15
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class SheetBar extends HorizontalScrollView implements OnClickListener {
    private IControl control;
    private SheetButton currentSheet;
    private int minimumWidth;
    private LinearLayout sheetbarFrame;
    private int sheetbarHeight;
    private SheetbarResManager sheetbarResManager;

    public SheetBar(Context context) {
        super(context);
    }

    public SheetBar(Context context, IControl iControl, int i) {
        super(context);
        this.control = iControl;
        setVerticalFadingEdgeEnabled(false);
        setFadingEdgeLength(0);
        if (i == getResources().getDisplayMetrics().widthPixels) {
            this.minimumWidth = -1;
        } else {
            this.minimumWidth = i;
        }
        init();
    }

    public void onConfigurationChanged(Configuration configuration) {
        LinearLayout linearLayout = this.sheetbarFrame;
        int i = this.minimumWidth;
        if (i == -1) {
            i = getResources().getDisplayMetrics().widthPixels;
        }
        linearLayout.setMinimumWidth(i);
    }

    private void init() {
        Context context = getContext();
        this.sheetbarFrame = new LinearLayout(context);
        this.sheetbarFrame.setGravity(80);
        this.sheetbarResManager = new SheetbarResManager(context);
        this.sheetbarResManager.getDrawable((short) 0);
        this.sheetbarFrame.setBackgroundColor(getResources().getColor(R.color.rippel));
        this.sheetbarFrame.setOrientation(0);
        LinearLayout linearLayout = this.sheetbarFrame;
        int i = this.minimumWidth;
        if (i == -1) {
            i = getResources().getDisplayMetrics().widthPixels;
        }
        linearLayout.setMinimumWidth(i);
        this.sheetbarHeight = 120;
        this.sheetbarResManager.getDrawable((short) 1);
        LayoutParams layoutParams = new LayoutParams(-2, this.sheetbarHeight);
        this.sheetbarFrame.addView(new View(context), layoutParams);
        Vector vector = (Vector) this.control.getActionValue(EventConstant.SS_GET_ALL_SHEET_NAME, null);
        this.sheetbarResManager.getDrawable((short) 4);
        LayoutParams layoutParams2 = new LayoutParams(-2, this.sheetbarHeight);
        int size = vector.size();
        for (int i2 = 0; i2 < size; i2++) {
            SheetButton sheetButton = new SheetButton(context, (String) vector.get(i2), i2, this.sheetbarResManager);
            if (this.currentSheet == null) {
                this.currentSheet = sheetButton;
                this.currentSheet.changeFocus(true);
            }
            sheetButton.setOnClickListener(this);
            this.sheetbarFrame.addView(sheetButton, layoutParams2);
            if (i2 < size - 1) {
                View view = new View(context);
                this.sheetbarResManager.getDrawable((short) 3);
                this.sheetbarFrame.addView(view, layoutParams2);
            }
        }
        View view2 = new View(context);
        this.sheetbarResManager.getDrawable((short) 2);
        this.sheetbarFrame.addView(view2, layoutParams);
        addView(this.sheetbarFrame, new LayoutParams(-2, this.sheetbarHeight));
    }

    public void onClick(View view) {
        this.currentSheet.changeFocus(false);
        SheetButton sheetButton = (SheetButton) view;
        sheetButton.changeFocus(true);
        this.currentSheet = sheetButton;
        this.control.actionEvent(EventConstant.SS_SHOW_SHEET, Integer.valueOf(this.currentSheet.getSheetIndex()));
    }

    public void setFocusSheetButton(int i) {
        if (this.currentSheet.getSheetIndex() != i) {
            int childCount = this.sheetbarFrame.getChildCount();
            View view = null;
            int i2 = 0;
            while (true) {
                if (i2 >= childCount) {
                    break;
                }
                view = this.sheetbarFrame.getChildAt(i2);
                if (view instanceof SheetButton) {
                    SheetButton sheetButton = (SheetButton) view;
                    if (sheetButton.getSheetIndex() == i) {
                        this.currentSheet.changeFocus(false);
                        this.currentSheet = sheetButton;
                        this.currentSheet.changeFocus(true);
                        break;
                    }
                }
                i2++;
            }
            int width = this.control.getActivity().getWindowManager().getDefaultDisplay().getWidth();
            int width2 = this.sheetbarFrame.getWidth();
            if (width2 > width) {
                int left = view.getLeft();
                int right = left - ((width - (view.getRight() - left)) / 2);
                int i3 = right < 0 ? 0 : right + width > width2 ? width2 - width : right;
                scrollTo(i3, 0);
            }
        }
    }

    public int getSheetbarHeight() {
        return this.sheetbarHeight;
    }

    public void dispose() {
        this.sheetbarResManager.dispose();
        this.sheetbarResManager = null;
        this.currentSheet = null;
        LinearLayout linearLayout = this.sheetbarFrame;
        if (linearLayout != null) {
            int childCount = linearLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.sheetbarFrame.getChildAt(i);
                if (childAt instanceof SheetButton) {
                    ((SheetButton) childAt).dispose();
                }
            }
            this.sheetbarFrame = null;
        }
    }
}
