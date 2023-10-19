package com.fanstatic.util;




import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CommonUtils {
     public static String convertToCurrencyString(Object number, String unit) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0", symbols);

        String formattedNumber = decimalFormat.format(number);
        return formattedNumber + unit;
    }


    
}
