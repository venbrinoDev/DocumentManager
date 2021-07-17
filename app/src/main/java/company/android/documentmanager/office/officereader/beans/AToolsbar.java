/*
 * 文件名称:          Toolbar.java
 *  
 * 编译器:            android2.2
 * 时间:              下午1:48:31
 */
package company.android.documentmanager.office.officereader.beans;

import java.util.HashMap;
import java.util.Map;

import company.android.documentmanager.R;
import company.android.documentmanager.office.system.IControl;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class AToolsbar extends HorizontalScrollView {
    protected Map<Integer, Integer> actionButtonIndex;
    private boolean animation;
    protected int buttonHeight;
    protected int buttonWidth;
    protected IControl control;
    protected LinearLayout toolsbarFrame;
    protected int toolsbarWidth;

    public void updateStatus() {
    }

    public AToolsbar(Context context, IControl iControl) {
        super(context);
        this.control = iControl;
        setAnimation(true);
        setVerticalFadingEdgeEnabled(false);
        setFadingEdgeLength(0);
        this.toolsbarFrame = new LinearLayout(context);
        this.toolsbarFrame.setOrientation(0);
        this.toolsbarFrame.setMinimumWidth(getResources().getDisplayMetrics().widthPixels);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.icon_folder, options);
        this.buttonWidth = options.outWidth;
        this.buttonHeight = options.outHeight;
        this.toolsbarFrame.setBackgroundResource(R.drawable.icon_folder);
    }

    public AToolsbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onConfigurationChanged(Configuration configuration) {
        this.toolsbarFrame.setMinimumWidth(getResources().getDisplayMetrics().widthPixels);
    }


    public AImageButton addButton(int i, int i2, int i3, int i4, boolean z) {
        Context context = getContext();
        AImageButton aImageButton = new AImageButton(context, this.control, context.getResources().getString(i3), i, i2, i4);
        aImageButton.setNormalBgResID(R.drawable.icon_folder);
        aImageButton.setPushBgResID(R.drawable.icon_folder);
        aImageButton.setLayoutParams(new LayoutParams(this.buttonWidth, this.buttonHeight));
        this.toolsbarFrame.addView(aImageButton);
        this.toolsbarWidth += this.buttonWidth;
        if (this.actionButtonIndex == null) {
            this.actionButtonIndex = new HashMap();
        }
        this.actionButtonIndex.put(Integer.valueOf(i4), Integer.valueOf(this.toolsbarFrame.getChildCount() - 1));
        if (z) {
            addSeparated();
        }
        return aImageButton;
    }


    public AImageCheckButton addCheckButton(int i, int i2, int i3, int i4, int i5, int i6, boolean z) {
        Context context = getContext();
        Resources resources = context.getResources();
        int i7 = i4;
        AImageCheckButton aImageCheckButton = new AImageCheckButton(context, this.control, resources.getString(i4), resources.getString(i5), i, i2, i3, i6);
        aImageCheckButton.setNormalBgResID(R.drawable.icon_folder);
        aImageCheckButton.setPushBgResID(R.drawable.icon_folder);
        aImageCheckButton.setLayoutParams(new LayoutParams(this.buttonWidth, this.buttonHeight));
        this.toolsbarFrame.addView(aImageCheckButton);
        this.toolsbarWidth += this.buttonWidth;
        if (this.actionButtonIndex == null) {
            this.actionButtonIndex = new HashMap();
        }
        this.actionButtonIndex.put(Integer.valueOf(i6), Integer.valueOf(this.toolsbarFrame.getChildCount() - 1));
        if (z) {
            addSeparated();
        }
        return aImageCheckButton;
    }


    public void addSeparated() {
        LinearLayout linearLayout = this.toolsbarFrame;
        AImageButton aImageButton = new AImageButton(getContext(), this.control, "", R.drawable.icon_folder, -1, -1);
        linearLayout.addView(aImageButton, new LayoutParams(1, this.buttonHeight));
        this.toolsbarWidth++;
    }

    public void onDraw(Canvas canvas) {
        if (isAnimation()) {
            setAnimation(false);
            if (this.toolsbarFrame.getWidth() > getResources().getDisplayMetrics().widthPixels) {
                scrollTo(this.buttonWidth * 3, 0);
            }
            fling(-4000);
        }
        super.onDraw(canvas);
    }

    public void setEnabled(int i, boolean z) {
        Integer num = (Integer) this.actionButtonIndex.get(Integer.valueOf(i));
        if (num != null && num.intValue() >= 0 && num.intValue() < this.toolsbarFrame.getChildCount()) {
            this.toolsbarFrame.getChildAt(num.intValue()).setEnabled(z);
        }
    }

    public void setCheckState(int i, short s) {
        int intValue = ((Integer) this.actionButtonIndex.get(Integer.valueOf(i))).intValue();
        if (intValue >= 0 && intValue < this.toolsbarFrame.getChildCount() && (this.toolsbarFrame.getChildAt(intValue) instanceof AImageCheckButton)) {
            ((AImageCheckButton) this.toolsbarFrame.getChildAt(intValue)).setState(s);
        }
    }

    public int getButtonWidth() {
        return this.buttonWidth;
    }

    public void setButtonWidth(int i) {
        this.buttonWidth = i;
    }

    public int getButtonHeight() {
        return this.buttonHeight;
    }

    public void setButtonHeight(int i) {
        this.buttonHeight = i;
    }

    public int getToolsbarWidth() {
        return this.toolsbarWidth;
    }

    public void setToolsbarWidth(int i) {
        this.toolsbarWidth = i;
    }

    public boolean isAnimation() {
        return this.animation;
    }

    public void setAnimation(boolean z) {
        this.animation = z;
    }

    public void dispose() {
        this.control = null;
        Map<Integer, Integer> map = this.actionButtonIndex;
        if (map != null) {
            map.clear();
            this.actionButtonIndex = null;
        }
        int childCount = this.toolsbarFrame.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.toolsbarFrame.getChildAt(i);
            if (childAt instanceof AImageButton) {
                ((AImageButton) childAt).dispose();
            }
        }
        this.toolsbarFrame = null;
    }
}