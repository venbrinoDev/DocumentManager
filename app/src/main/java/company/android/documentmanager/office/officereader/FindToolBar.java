/*
 * 文件名称:          SearchBar.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:46:54
 */
package company.android.documentmanager.office.officereader;

import company.android.documentmanager.R;
import company.android.documentmanager.office.valconstttaa.EventConstant;
import company.android.documentmanager.office.officereader.beans.AImageButton;
import company.android.documentmanager.office.officereader.beans.AImageFindButton;
import company.android.documentmanager.office.officereader.beans.AToolsbar;
import company.android.documentmanager.office.system.IControl;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;

public class FindToolBar extends AToolsbar {

    public AImageFindButton searchButton;

    public FindToolBar(Context context, IControl iControl) {
        super(context, iControl);
        init();
    }

    private void init() {
        AImageButton addButton = addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_searchbar_backward, EventConstant.APP_FIND_BACKWARD, false);
        addButton.getLayoutParams().width = this.buttonWidth / 2;
        addButton.setEnabled(false);
        AImageButton addButton2 = addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_searchbar_forward, EventConstant.APP_FIND_FORWARD, false);
        addButton2.getLayoutParams().width = this.buttonWidth / 2;
        addButton2.setEnabled(false);
        AImageFindButton aImageFindButton = new AImageFindButton(getContext(), this.control, getContext().getResources().getString(R.string.app_searchbar_find), R.drawable.icon_folder, R.drawable.icon_folder, EventConstant.APP_FINDING, getResources().getDisplayMetrics().widthPixels - ((this.buttonWidth * 3) / 2), this.buttonWidth / 2, this.buttonHeight, new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                boolean z = false;
                FindToolBar.this.setEnabled(EventConstant.APP_FIND_BACKWARD, false);
                FindToolBar.this.setEnabled(EventConstant.APP_FIND_FORWARD, false);
                AImageFindButton access$000 = FindToolBar.this.searchButton;
                if (editable.length() > 0) {
                    z = true;
                }
                access$000.setFindBtnState(z);
            }
        });
        this.searchButton = aImageFindButton;
        this.actionButtonIndex.put(Integer.valueOf(EventConstant.APP_FINDING), Integer.valueOf(this.toolsbarFrame.getChildCount()));
        this.toolsbarFrame.addView(this.searchButton);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.searchButton.resetEditTextWidth(getResources().getDisplayMetrics().widthPixels - ((this.buttonWidth * 3) / 2));
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i == 0) {
            ((InputMethodManager) getContext().getSystemService("input_method")).toggleSoftInput(0, 2);
            setEnabled(EventConstant.APP_FIND_BACKWARD, false);
            setEnabled(EventConstant.APP_FIND_FORWARD, false);
            this.searchButton.reset();
        }
    }

    public void dispose() {
        super.dispose();
        this.searchButton = null;
    }
}