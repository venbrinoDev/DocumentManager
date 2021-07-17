/*
 * 文件名称:          WPToolsBar.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:31:24
 */
package company.android.documentmanager.office.officereader.beans;

import company.android.documentmanager.R;
import company.android.documentmanager.office.valconstttaa.EventConstant;
import company.android.documentmanager.office.system.IControl;
import company.android.documentmanager.office.pdfffwp.filecontrol.Word;

import android.content.Context;
import android.view.View;

public class WPToolsbar extends AToolsbar {
    public WPToolsbar(Context context, IControl iControl) {
        super(context, iControl);
        init();
    }

    private void init() {
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_copy, EventConstant.FILE_COPY_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_find, EventConstant.APP_FIND_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.wp_toolsbar_switch_view, EventConstant.WP_SWITCH_VIEW, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.wp_toolsbar_print_mode, EventConstant.WP_PRINT_MODE, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_share, EventConstant.APP_SHARE_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_internet_search, EventConstant.APP_INTERNET_SEARCH_ID, true);
        addCheckButton(R.drawable.icon_folder, R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_mark_star, R.string.file_toolsbar_unmark_star, EventConstant.FILE_MARK_STAR_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_draw, EventConstant.APP_DRAW_ID, true);
    }

    public void updateStatus() {
        Word word = (Word) this.control.getView();
        setEnabled(EventConstant.APP_INTERNET_SEARCH_ID, word.getHighlight().isSelectText());
        setEnabled(EventConstant.FILE_COPY_ID, word.getHighlight().isSelectText());
        setEnabled(EventConstant.APP_DRAW_ID, word != null && word.getCurrentRootType() == 2);
    }

    public void onClick(View view) {
        if (view instanceof AImageButton) {
            int actionID = ((AImageButton) view).getActionID();
            if (actionID != 805306368) {
                this.control.actionEvent(actionID, null);
            } else {
                this.control.actionEvent(actionID, null);
            }
        }
    }

    public void dispose() {
        super.dispose();
    }
}