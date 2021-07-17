/*
 * 文件名称:          TextParagraph.java
 *  
 * 编译器:            android2.2
 * 时间:              上午9:10:21
 */
package company.android.documentmanager.office.ss.model.drawing;

import company.android.documentmanager.office.simpletext.font.Font;
import company.android.documentmanager.office.ss.model.style.Alignment;


/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-3-1
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class TextParagraph
{
    public TextParagraph()
    {
        align = new Alignment();
    }
    
    /**
     * 
     *  textRun
     */
    public void setTextRun(String textRun)
    {
        this.textRun =textRun;
    }
    
    /**
     * 
     * @return
     */
    public String getTextRun()
    {
        return textRun;
    }
    
    /**
     * 
     *  fontIndex
     */
    public void setFont(Font font)
    {
        this.font = font;
    }
    
    /**
     * 
     * @return
     */
    public Font getFont()
    {
        return font;
    }
    
    /**
     * 
     *  horizon
     */
    public void setHorizontalAlign(short horizon)
    {
        align.setHorizontalAlign(horizon);
    }
    
    /**
     * 
     * @return
     */
    public short getHorizontalAlign()
    {
        return align.getHorizontalAlign();
    }
    
    /**
     * 
     *  vertical
     */
    public void setVerticalAlign(short vertical)
    {
        align.setVerticalAlign(vertical);
    }
    
    /**
     * 
     * @return
     */
    public short getVerticalAlign()
    {
        return align.getVerticalAlign();
    }
    
    public void dispose()
    {
        textRun = null;
        font = null;
        
        if(align != null)
        {
            align.dispose();
            align = null;
        }
            
    }
    private String textRun;
    private Font font;
    private Alignment align;
}
