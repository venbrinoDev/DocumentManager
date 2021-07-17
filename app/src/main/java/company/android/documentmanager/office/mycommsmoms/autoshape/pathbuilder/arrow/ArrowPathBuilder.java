/*
 * 文件名称:          SimpleArrow.java
 *  
 * 编译器:            android2.2
 * 时间:              下午3:07:55
 */
package company.android.documentmanager.office.mycommsmoms.autoshape.pathbuilder.arrow;

import company.android.documentmanager.office.mycommsmoms.shape.AutoShape;

import android.graphics.Rect;

/**
 * TODO: LeftArrow, RightArrow, UpArrow, DownArrow, LeftRightArrow, UpDownArrow
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-9-17
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class ArrowPathBuilder
{    
    /**
     * get autoshape path
     *  shape
     *  rect
     * @return
     */
    public static Object getArrowPath(AutoShape shape, Rect rect)
    {
        if(shape.isAutoShape07())
        {
            return LaterArrowPathBuilder.getArrowPath(shape, rect);
        }
        else
        {
            return EarlyArrowPathBuilder.getArrowPath(shape, rect);
        }
    }
}
