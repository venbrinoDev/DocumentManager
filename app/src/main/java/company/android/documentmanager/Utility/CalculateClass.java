package company.android.documentmanager.Utility;

import android.text.format.DateFormat;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalculateClass {

    public static float  calculateSize(float length){

        float kilobyte = length/1024;
        BigDecimal value = new BigDecimal(kilobyte);


        value = value.setScale(2,BigDecimal.ROUND_HALF_UP);

        return value.floatValue();
    }
    public static String readableFileSize(long j) {
        if (j <= 0) {
            return "0";
        }
        String[] strArr = {"B", "KB", "MB", "GB", "TB"};
        double d = (double) j;
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        StringBuilder sb = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        double pow = Math.pow(1024.0d, (double) log10);
        Double.isNaN(d);
        sb.append(decimalFormat.format(d / pow));
        sb.append(" ");
        sb.append(strArr[log10]);
        return sb.toString();
    }

        public static ArrayList<Integer> findWord(String textString, String word) {
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        String lowerCaseTextString = textString.toLowerCase();
        String lowerCaseWord = word.toLowerCase();

        int index = 0;
        while(index != -1){
            index = lowerCaseTextString.indexOf(lowerCaseWord, index);
            if (index != -1) {
                indexes.add(index);
                index++;
            }
        }
        return indexes;
    }


    public static String getDateOuOfTimeStanp(long timeStamp){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timeStamp);
        String dataIme = DateFormat.format("MMM dd yyyy , hh:mm:ss a", calendar).toString();
  return dataIme;
    }

    public static String getMimeType(String name){

        String lastThree = "error";
        if (name.length() > 3) {
            lastThree = name.substring(name.length() - 3);
        }
        switch (lastThree) {
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "xls":
                return "application/vnd.ms-excel";
            case "doc":
                return "application/msword";
            case "zip":
                return "application/zip";
            case "rar":
                return "application/vnd.rar";

            default:
                return "application";

    }
}
    }
