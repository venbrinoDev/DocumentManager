/*
 * 文件名称:          PDFToolsbar.java
 *  
 * 编译器:            android2.2
 * 时间:              下午6:04:29
 */
package company.android.documentmanager.office.officereader.beans;

import company.android.documentmanager.R;
import company.android.documentmanager.office.valconstttaa.EventConstant;
import company.android.documentmanager.office.system.IControl;

import android.content.Context;
import android.view.View;

public class PDFToolsbar extends AToolsbar {
    public void updateStatus() {
    }

    public PDFToolsbar(Context context, IControl iControl) {
        super(context, iControl);
        init();
    }

    private void init() {
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_find, EventConstant.APP_FIND_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_share, EventConstant.APP_SHARE_ID, true);
        addCheckButton(R.drawable.icon_folder, R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_mark_star, R.string.file_toolsbar_unmark_star, EventConstant.FILE_MARK_STAR_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_draw, EventConstant.APP_DRAW_ID, true);
    }

    public void onClick(View view) {
        if (view instanceof AImageButton) {
            this.control.actionEvent(((AImageButton) view).getActionID(), null);
        }
    }

    public void dispose() {
        super.dispose();
    }
}