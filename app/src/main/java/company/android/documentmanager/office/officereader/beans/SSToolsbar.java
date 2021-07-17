/*
 * 文件名称:          SSToolsBar.java
 *  
 * 编译器:            android2.2
 * 时间:              下午4:05:23
 */
package company.android.documentmanager.office.officereader.beans;

import company.android.documentmanager.R;
import company.android.documentmanager.office.valconstttaa.EventConstant;
import company.android.documentmanager.office.ss.control.ExcelView;
import company.android.documentmanager.office.ss.control.Spreadsheet;
import company.android.documentmanager.office.ss.model.baseModel.Sheet;
import company.android.documentmanager.office.ss.util.ModelUtil;
import company.android.documentmanager.office.system.IControl;

import android.content.Context;
import android.util.AttributeSet;

public class SSToolsbar extends AToolsbar {
    public SSToolsbar(Context context, IControl iControl) {
        super(context, iControl);
        init();
    }

    public SSToolsbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void init() {
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_copy, EventConstant.FILE_COPY_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_hyperlink, EventConstant.APP_HYPERLINK, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_find, EventConstant.APP_FIND_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_share, EventConstant.APP_SHARE_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_internet_search, EventConstant.APP_INTERNET_SEARCH_ID, true);
        addCheckButton(R.drawable.icon_folder, R.drawable.icon_folder, R.drawable.icon_folder, R.string.file_toolsbar_mark_star, R.string.file_toolsbar_unmark_star, EventConstant.FILE_MARK_STAR_ID, true);
        addButton(R.drawable.icon_folder, R.drawable.icon_folder, R.string.app_toolsbar_draw, EventConstant.APP_DRAW_ID, true);
    }

    public void updateStatus() {
        Spreadsheet spreadsheet = ((ExcelView) this.control.getView()).getSpreadsheet();
        if (spreadsheet.getSheetView() != null) {
            boolean z = true;
            setEnabled(EventConstant.APP_FIND_ID, true);
            setEnabled(EventConstant.APP_SHARE_ID, true);
            setEnabled(15, true);
            Sheet currentSheet = spreadsheet.getSheetView().getCurrentSheet();
            if (currentSheet.getActiveCellType() != 0 || currentSheet.getActiveCell() == null) {
                setEnabled(EventConstant.FILE_COPY_ID, false);
                setEnabled(EventConstant.APP_HYPERLINK, false);
                setEnabled(EventConstant.APP_INTERNET_SEARCH_ID, false);
            } else {
                String formatContents = ModelUtil.instance().getFormatContents(currentSheet.getWorkbook(), currentSheet.getActiveCell());
                setEnabled(EventConstant.FILE_COPY_ID, formatContents != null && formatContents.length() > 0);
                setEnabled(EventConstant.APP_INTERNET_SEARCH_ID, formatContents != null && formatContents.length() > 0);
                if (currentSheet.getActiveCell().getHyperLink() == null || currentSheet.getActiveCell().getHyperLink().getAddress() == null) {
                    z = false;
                }
                setEnabled(EventConstant.APP_HYPERLINK, z);
            }
            postInvalidate();
        }
    }

    public void dispose() {
        super.dispose();
    }
}