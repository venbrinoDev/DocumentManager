package company.android.documentmanager.office.macro;

import company.android.documentmanager.office.fc.pdf.PDFLib;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class PDFLibKit 
{
	private static PDFLibKit kit = new PDFLibKit();
	private static PDFLib lib = PDFLib.getPDFLib();
    /**
     * 
     */
    public static PDFLibKit instance()
    {
        return kit;
    }
    
	/**
     * Construct
     * 
     *  filename
     * 
     * @throws Exception
     */
    public synchronized void openFileSync(String filename) throws Exception
    {
    	lib.openFileSync(filename);
    }
    
    /**
     * get page count
     * 
     * @return page count
     */
    public int getPageCountSync()
    {
    	return lib.getPageCountSync();
    }
    
    /**
     * 
     * @return
     */
    public Rect[] getAllPagesSize()
    {
        return lib.getAllPagesSize();
    }
    
    /**
     * draw page to bitmap
     * 
     *  bitmap        Bitmap instance
     *  pageIndex     The page index (base 0)
     *  pageWidth     The page width of after scaling
     *  pageHeight    The page height of after scaling
     *  paintX        The paint X axis
     *  paintY        The paint Y axis
     *  paintWidth    The paint width
     *  paintHeight   The paint height
     */
    public synchronized void drawPageSync(Bitmap bitmap, int pageIndex, float pageWidth, float pageHeight, 
        int paintX, int paintY, int paintWidth, int paintHeight, int drawObject)
    {
    	lib.drawPageSync(bitmap, pageIndex, pageWidth, pageHeight, paintX, paintY, paintWidth, paintHeight, drawObject);
    }

//    /**
//     * get hyperlink count assign location
//     * 
//     *  pageIndex  page index (base 0)
//     *  x     x axis value
//     *  y     y axis value
//     * @return hyperlink count 
//     */
//    public synchronized int getHyperlinkCountSync(int pageIndex, float x, float y)
//    {
//        return lib.getHyperlinkCountSync(pageIndex, x, y);
//    }
//
//    /**
//     * get hyperlink information assign page index
//     * 
//     *  page      page index (base 0)
//     * @return hyperlink information
//     */
//    public synchronized PDFHyperlinkInfo[] getHyperlinkInfoSync(int pageIndex)
//    {
//        return lib.getHyperlinkInfoSync(pageIndex);
//    }
//
//    /**
//     * search content is this PDF document
//     * 
//     *  pageIndex     page index (base 0)
//     *  str  search content
//     * @return  content is page location
//     */
//    public synchronized RectF[] searchContentSync(int pageIndex, String text)
//    {
//    	return lib.searchContentSync(pageIndex, text);
//    }

    /**
     * is this PDF document password? 
     * 
     * @return      = true     have password
     *               = false    no password         
     */
    public synchronized boolean hasPasswordSync()
    {
        return lib.hasPasswordSync();
    }

    /**
     * Authenticate password the PDF document 
     * 
     *    password
     * @return  = true  correct
     *           = false    wrong
     */
    public synchronized boolean authenticatePasswordSync(String password)
    {
        return lib.authenticatePasswordSync(password);
    }
    
    /**
     * 
     *  flag
     */
    public void setStopFlagSync(int flag)
    {
    	lib.setStopFlagSync(flag);
    }
    
    /**
     * dispose memory
     */
    public synchronized void dispose()
    {
        lib = null;
    }
}
