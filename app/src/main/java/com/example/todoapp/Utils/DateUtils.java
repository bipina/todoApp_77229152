package com.example.todoapp.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static Date convertToDate(String date) {
        String dtStart = date;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date vdate = null;
        try {
            vdate = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return vdate;
    }


    public static String convertToString(Date date,String dateFormat){
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern(dateFormat);
        return simpleDateFormat.format(date);

    }

}
