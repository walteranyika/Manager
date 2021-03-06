package io.walter.manager.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {

    public static String dateFormat = "dd-MM-yyyy hh:mm";
    public static String tareheFormat = "dd-MM-yyyy";
    public static String yearFormat = "yyyy";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private static SimpleDateFormat simpleTareheFormat = new SimpleDateFormat(tareheFormat);
    private static SimpleDateFormat simpleYearFormat = new SimpleDateFormat(yearFormat);

    public static String monthFormat = "M";
    private static SimpleDateFormat simpleMonthFormat = new SimpleDateFormat(monthFormat);

    public static String ConvertToDateString(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String ConvertToYearString(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleYearFormat.format(calendar.getTime());
    }


    public static String ConvertToPureDateString(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleTareheFormat.format(calendar.getTime());
    }

    public static String ConvertToMonthString(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleMonthFormat.format(calendar.getTime());
    }
    public  static  long ConvertFromDateToLong(String date){
        long time= System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date startDate = df.parse(date);
            time=startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  time;
    }
    public static String ConvertToMonthInWords(String month){
        String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
        int i=Integer.valueOf(month)-1;
        return months[i];
    }
}