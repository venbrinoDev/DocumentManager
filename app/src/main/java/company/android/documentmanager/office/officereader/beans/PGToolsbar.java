/*
 * 文件名称:          PGToolsBar.java
 *  
 * 编译器:            android2.2
 * 时间:              下午5:03:30
 */

package company.android.documentmanager.office.officereader.beans;

import company.android.documentmanager.R;
import company.android.documentmanager.office.valconstttaa.EventConstant;
import company.android.documentmanager.office.pg.control.Presentation;
import company.android.documentmanager.office.system.IControl;

import android.content.Context;
import android.util.AttributeSet;

public class PGToolsbar extends AToolsbar {
    public PGToolsbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PGToolsbar(Context context, IControl iControl) {
        super(context, iControl);
        init();
    }

    private void init() {
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.pg_slideshow, EventConstant.PG_SLIDESHOW_GEGIN, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_find, EventConstant.APP_FIND_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.pg_toolsbar_note, EventConstant.PG_NOTE_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_share, EventConstant.APP_SHARE_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_internet_search, EventConstant.APP_INTERNET_SEARCH_ID, true);
        addCheckButton(R.drawable.icon_folder, R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_mark_star, R.string.file_toolsbar_unmark_star, EventConstant.FILE_MARK_STAR_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_draw, EventConstant.APP_DRAW_ID, true);
    }

    public void updateStatus() {
        Presentation presentation = (Presentation) this.control.getView();
        if (presentation.getCurrentSlide() == null || presentation.getCurrentSlide().getNotes() == null) {
            setEnabled(EventConstant.PG_NOTE_ID, false);
        } else {
            setEnabled(EventConstant.PG_NOTE_ID, true);
        }
        postInvalidate();
    }

    public void dispose() {
        super.dispose();
    }
}
