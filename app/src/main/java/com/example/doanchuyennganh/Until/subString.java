package com.example.doanchuyennganh.Until;

public class subString {

    public static String subTrimTitle(String link,String source,String date){
        return source.substring(link.length() + 32, source.length() - (date.length()+4));
    }

    public static String subDate(String date){
        return date.substring(1,(date.length()-1));
    }

    public static String convertDate(String date){
        String month = date.substring(0,date.indexOf("."));
        String year = date.substring(date.lastIndexOf(".")+1,date.length());
        String day = date.substring(date.indexOf(".")+1,date.lastIndexOf("."));
        String result = day+"."+month+"."+year;
        return result;
    }
}
