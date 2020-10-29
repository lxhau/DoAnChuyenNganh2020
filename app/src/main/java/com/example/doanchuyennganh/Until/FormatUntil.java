package com.example.doanchuyennganh.Until;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUntil {

    static SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");

    static SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");

    static SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm aa");

    public static String formatDateTime(Date han) {
        return sdfDateTime.format(han);
    }
    public static String formatDate(Date han) {
        return sdfDate.format(han);
    }
    public static String formatTime(Date han) {
        return sdfTime.format(han);
    }
}
