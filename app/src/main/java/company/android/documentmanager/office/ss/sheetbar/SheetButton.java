/*
 * 文件名称:          SheetButton.java
 *
 * 编译器:            android2.2
 * 时间:              下午6:06:50
 */
package company.android.documentmanager.office.ss.sheetbar;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import company.android.documentmanager.R;
/**
 * sheet表名称按钮
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            ljj8494
 * <p>
 * 日期:            2011-12-6
 * <p>
 * 负责人:          ljj8494
 * <p>
 * 负责小组:
 * <p>
 * <p>
 */
public class SheetButton extends LinearLayout {
    private static final int FONT_SIZE = 14;
    private static final int SHEET_BUTTON_MIN_WIDTH = 100;
    private boolean active;
    private View left;
    private View right;
    private int sheetIndex;
    private SheetbarResManager sheetbarResManager;
    private TextView textView;

    public SheetButton(Context context, String str, int i, SheetbarResManager sheetbarResManager2) {
        super(context);
        setOrientation(0);
        this.sheetIndex = i;
        this.sheetbarResManager = sheetbarResManager2;
        init(context, str);
    }

    private void init(Context context, String str) {
        this.left = new View(context);
        int i = (int) ((getResources().getDisplayMetrics().density * 12.0f) + 0.5f);
        this.left.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_border));
        this.left.setPadding(i, i, i, i);
        addView(this.left);
        this.textView = new TextView(context);
        this.textView.setBackgroundColor(getResources().getColor(R.color.rippel));
        this.textView.setPadding(14, 1, 14, 1);
        this.textView.setText(str);
        this.textView.setTextSize(14.0f);
        this.textView.setGravity(17);
        this.textView.setTextColor(getResources().getColor(R.color.black));
        Math.max((int) this.textView.getPaint().measureText(str), 100);
        addView(this.textView, new LayoutParams(-10, -1));
        this.right = new View(context);
        this.right.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_border));
        this.right.setPadding(i, i, i, i);
        addView(this.right);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x000d, code lost:
        if (r0 != 3) goto L_0x0015;
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action != 2) {
                }
            }
            boolean z = this.active;
            return super.onTouchEvent(motionEvent);
        }
        boolean z2 = this.active;
        return super.onTouchEvent(motionEvent);
    }

    public void changeFocus(boolean z) {
        int i;
        int i2;
        int i3;
        int i4;
        this.active = z;
        View view = this.left;
        if (z) {
            i = getResources().getColor(R.color.black);
        } else {
            i = getResources().getColor(R.color.rippel);
        }
        view.setBackgroundColor(i);
        TextView textView2 = this.textView;
        if (z) {
            i2 = getResources().getColor(R.color.black);
        } else {
            i2 = getResources().getColor(R.color.rippel);
        }
        textView2.setBackgroundColor(i2);
        TextView textView3 = this.textView;
        if (z) {
            i3 = getResources().getColor(R.color.back);
        } else {
            i3 = getResources().getColor(R.color.black);
        }
        textView3.setTextColor(i3);
        View view2 = this.right;
        if (z) {
            i4 = getResources().getColor(R.color.black);
        } else {
            i4 = getResources().getColor(R.color.rippel);
        }
        view2.setBackgroundColor(i4);
    }

    public int getSheetIndex() {
        return this.sheetIndex;
    }

    public void dispose() {
        this.sheetbarResManager = null;
        this.left = null;
        this.textView = null;
        this.right = null;
    }
}
