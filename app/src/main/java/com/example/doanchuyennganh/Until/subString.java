package com.example.doanchuyennganh.Until;

public class subString {

    public static String subTrimTitle(String link,String source,String date){
        return source.substring(link.length() + 32, source.length() - (date.length()+4));
    }

    public static String subDate(String date){
        return date.substring(1,(date.length()-1));
    }
}
