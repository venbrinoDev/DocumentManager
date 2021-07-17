package company.android.documentmanager.FileReaders;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;

public class xlsApiText
{
   public FileInputStream fis = null;
   public HSSFWorkbook workbook = null;
   public HSSFSheet sheet = null;
   public HSSFRow row = null;
   public HSSFCell cell = null;
   String xlFilePath;

   public xlsApiText(String xlFilePath) throws Exception
   {
       this.xlFilePath = xlFilePath;
       fis = new FileInputStream(xlFilePath);
       workbook = new HSSFWorkbook(fis);
       fis.close();
   }
 
   public int getRowCount(String sheetName)
   {
       sheet = workbook.getSheet(sheetName);
       int rowCount = sheet.getLastRowNum()+1;
       return rowCount;
   }
 
   public int getColumnCount(String sheetName)
   {
       sheet = workbook.getSheet(sheetName);
       row = sheet.getRow(0);
       int colCount = row.getLastCellNum();
       return colCount;
   }
}