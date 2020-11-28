package com.example.doanchuyennganh.Receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.doanchuyennganh.Service.UpdateDataService;
import com.example.doanchuyennganh.Until.FormatUntil;
import com.example.doanchuyennganh.Views.MainActivity;

import java.util.Calendar;

public class WakeUpAlarmReceiver extends BroadcastReceiver {

    public static final String SHARED_PREFERENCE = "FILE";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            String time = getDataCache(context);
            if (time.equals("NULL")) {
                time = "08:00 AM";
            }
            // Quote in Morning

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getHours(time)));
            calendar.set(Calendar.MINUTE, Integer.parseInt(getMinutes(time)));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Calendar cur = Calendar.getInstance();

            if (cur.after(calendar)) {
                calendar.add(Calendar.DATE, 1);
            }

            Intent myIntent = new Intent(context, UpdateDataService.class);
            int ALARM1_ID = 10000;

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


            long interval = 60 * 60 * 1000 * 6; // Đặt lịch cập nhật sau 6 tiếng.
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    interval, pendingIntent);
        }
    }

    private String getDataCache(Context context) {

        SharedPreferences pref = context.getSharedPreferences(MainActivity.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        String time = pref.getString("timeUpdate", "NULL");

        return time;
    }

    private String getHours(String time) {
        if (time.substring(0, 1) == "0") {
            return time.substring(1, 2);
        }
        return time.substring(0, 2);
    }

    private String getMinutes(String time) {
        return time.substring(3, 5);
    }
}
