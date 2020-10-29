package com.example.doanchuyennganh.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class TimerClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TAG: ","Receiver Chạy");

       // Intent intentservice = new Intent(context, getRSS.class);
       // context.startService(intentservice);

/*        Intent serviceIntent = new Intent(context, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Đến Giờ Rồi");

        ContextCompat.startForegroundService(context, serviceIntent);*/


    }
}
