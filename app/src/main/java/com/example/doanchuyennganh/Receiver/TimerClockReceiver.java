package com.example.doanchuyennganh.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.doanchuyennganh.Service.UpdateDataService;

public class TimerClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TAG: ", "Receiver Chạy");
        context.startService(new Intent(context, UpdateDataService.class));
    }
}
