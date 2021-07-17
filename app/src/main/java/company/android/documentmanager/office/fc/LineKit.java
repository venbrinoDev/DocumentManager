package company.android.documentmanager.office.fc;

import java.util.Map;

import company.android.documentmanager.office.mycommsmoms.autoshape.AutoShapeDataKit;
import company.android.documentmanager.office.mycommsmoms.bg.BackgroundAndFill;
import company.android.documentmanager.office.mycommsmoms.borders.Line;
import company.android.documentmanager.office.valconstttaa.MainConstant;
import company.android.documentmanager.office.fc.dom4j.Element;
import company.android.documentmanager.office.fc.openxml4j.opc.PackagePart;
import company.android.documentmanager.office.fc.openxml4j.opc.ZipPackage;
import company.android.documentmanager.office.fc.ppt.reader.BackgroundReader;
import company.android.documentmanager.office.fc.ppt.reader.ReaderKit;
import company.android.documentmanager.office.pg.model.PGMaster;
import company.android.documentmanager.office.system.IControl;

public class LineKit 
{
	/**
	 * smart
	 *  control
	 *  zipPackage
	 *  packagePart
	 *  pgMaster
	 *  ln
	 * @return
	 * @throws Exception
	 */
	public static Line createLine(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGMaster pgMaster, Element ln) throws Exception
    {
    	 int lineWidth = 1;
         boolean dash = false;
         BackgroundAndFill lineFill = null;
         if (ln != null)
         {
             //border                
             if (ln.element("noFill") == null)
             {
             	 //line width
                 if(ln.attributeValue("w") != null)
                 {
                     lineWidth = Math.round(Integer.parseInt(ln.attributeValue("w")) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                 }
                 
                 Element prstDash = ln.element("prstDash");
                 if(prstDash != null && !"solid".equalsIgnoreCase(prstDash.attributeValue("val")))
                 {
                 	dash = true;
                 }                	 
                  
             	
                 lineFill = BackgroundReader.instance().processBackground(control, zipPackage, packagePart, pgMaster, ln);
                 if(lineFill != null)
                 {
                	 Line line = new Line();
                	 line.setBackgroundAndFill(lineFill);
                	 line.setLineWidth(lineWidth);
                	 line.setDash(dash);
                	 return line;
                 }
             }
         }
         
         return null;
    }
	
	/**
	 * ppt and smart
	 *  control
	 *  zipPackage
	 *  packagePart
	 *  pgMaster
	 *  sp
	 * @return
	 * @throws Exception
	 */
	public static Line createShapeLine(IControl control, ZipPackage zipPackage, PackagePart packagePart, PGMaster pgMaster, Element sp) throws Exception
    {
    	 int lineWidth = 1;
         boolean dash = false;
         BackgroundAndFill lineFill = null;
         // border
         Element ln = sp.element("spPr").element("ln");
         Element style = sp.element("style");
         if (ln != null)
         {
             //border                
             if (ln.element("noFill") == null)
             {
             	 //line width
                 if(ln.attributeValue("w") != null)
                 {
                     lineWidth = Math.round(Integer.parseInt(ln.attributeValue("w")) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                 }
                 
                 Element prstDash = ln.element("prstDash");
                 if(prstDash != null && !"solid".equalsIgnoreCase(prstDash.attributeValue("val")))
                 {
                 	dash = true;
                 }                	 
                  
             	
                 lineFill = BackgroundReader.instance().processBackground(control, zipPackage, packagePart, pgMaster, ln);
                 if(lineFill == null && style != null && style.element("lnRef") != null)
                 {
                 	lineFill = new BackgroundAndFill();
                 	lineFill.setFillType(BackgroundAndFill.FILL_SOLID);
                 	lineFill.setForegroundColor(ReaderKit.instance().getColor(pgMaster, style.element("lnRef")));
                 }
             }
         }
         else
         {
             if (style != null && style.element("lnRef") != null)
             {
            	 int color = ReaderKit.instance().getColor(pgMaster, style.element("lnRef"));
            	 if((color & 0xFFFFFF) != 0)
            	 {
            		 lineFill = new BackgroundAndFill();
                  	lineFill.setFillType(BackgroundAndFill.FILL_SOLID);
                  	lineFill.setForegroundColor(color);
            	 }
             }
         }
         
         if(lineFill != null)
         {
        	 Line line = new Line();
        	 line.setBackgroundAndFill(lineFill);
        	 line.setLineWidth(lineWidth);
        	 line.setDash(dash);
        	 return line;
         }
         
         return null;
    }
	
	public static Line createLine(IControl control, ZipPackage zipPackage, PackagePart  drawingPart, 
	        Element ln, Map<String, Integer> schemeColor)
	{
		int lineWidth = 1;
        boolean dash = false;
        BackgroundAndFill lineFill = null;
        // border
        if (ln != null)
        {
        	//line width
            if(ln.attributeValue("w") != null)
            {
                lineWidth = Math.round(Integer.parseInt(ln.attributeValue("w")) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
            }
            
            Element prstDash = ln.element("prstDash");
            if(prstDash != null && !"solid".equalsIgnoreCase(prstDash.attributeValue("val")))
            {
            	dash = true;
            }
            
            if (ln.element("noFill") == null)
            {
            	lineFill = AutoShapeDataKit.processBackground(control, zipPackage, drawingPart, ln, schemeColor);            	
            }
        }
        
        if(lineFill != null)
        {
	       	 Line line = new Line();
	       	 line.setBackgroundAndFill(lineFill);
	       	 line.setLineWidth(lineWidth);
	       	 line.setDash(dash);
	       	 return line;
        }
        
        return  null;
	}
	
	public static Line createShapeLine(IControl control, ZipPackage zipPackage, PackagePart  drawingPart, 
	        Element sp, Map<String, Integer> schemeColor)
	{
		int lineWidth = 1;
        boolean dash = false;
        BackgroundAndFill lineFill = null;
        // border
        Element ln = sp.element("spPr").element("ln");
        Element style = sp.element("style");
        if (ln != null)
        {
        	//line width
            if(ln.attributeValue("w") != null)
            {
                lineWidth = Math.round(Integer.parseInt(ln.attributeValue("w")) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
            }
            
            Element prstDash = ln.element("prstDash");
            if(prstDash != null && !"solid".equalsIgnoreCase(prstDash.attributeValue("val")))
            {
            	dash = true;
            }
            
            if (ln.element("noFill") == null)
            {
            	lineFill = AutoShapeDataKit.processBackground(control, zipPackage, drawingPart, ln, schemeColor);
            	if(lineFill == null && style != null && style.element("lnRef") != null)
                {
                	lineFill = new BackgroundAndFill();
                	lineFill.setFillType(BackgroundAndFill.FILL_SOLID);
                	lineFill.setForegroundColor(AutoShapeDataKit.getColor(schemeColor, style.element("lnRef")));
                }
            }
        }
        else
        {
            if (style != null && style.element("lnRef") != null)
            {
            	
            	lineFill = new BackgroundAndFill();
                lineFill.setFillType(BackgroundAndFill.FILL_SOLID);
                lineFill.setForegroundColor(AutoShapeDataKit.getColor(schemeColor, style.element("lnRef")));
            }
        }
        
        if(lineFill != null)
        {
	       	 Line line = new Line();
	       	 line.setBackgroundAndFill(lineFill);
	       	 line.setLineWidth(lineWidth);
	       	 line.setDash(dash);
	       	 return line;
        }
        
        return  null;
	}
	
	public static Line createChartLine(IControl control, ZipPackage zipPackage, PackagePart  drawingPart, 
	        Element ln, Map<String, Integer> schemeColor)
	{		
        // border
        if (ln != null)
        {
        	int lineWidth = 1;
            boolean dash = false;
            BackgroundAndFill lineFill = null;
        	//line width
            if(ln.attributeValue("w") != null)
            {
                lineWidth = Math.round(Integer.parseInt(ln.attributeValue("w")) * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
            }
            
            Element prstDash = ln.element("prstDash");
            if(prstDash != null && !"solid".equalsIgnoreCase(prstDash.attributeValue("val")))
            {
            	dash = true;
            }
            
            if (ln.element("noFill") == null)
            {
            	lineFill = AutoShapeDataKit.processBackground(control, zipPackage, drawingPart, ln, schemeColor);
            	if(lineFill != null)
                {
        	       	 Line line = new Line();
        	       	 line.setBackgroundAndFill(lineFill);
        	       	 line.setLineWidth(lineWidth);
        	       	 line.setDash(dash);
        	       	 return line;
                }
            }
        }
        else
        {
        	//auto line
        	Line line = new Line();
       	 	BackgroundAndFill lineFill = new BackgroundAndFill();
       	 	lineFill.setFillType(BackgroundAndFill.FILL_SOLID);
       	 	lineFill.setForegroundColor(0xFF747474);
	       	line.setBackgroundAndFill(lineFill);
	       	line.setLineWidth(1);
	       	
	       	return line;
        }
        return  null;
	}
}
