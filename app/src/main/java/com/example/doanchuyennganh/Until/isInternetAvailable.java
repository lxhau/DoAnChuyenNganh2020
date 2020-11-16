package com.example.doanchuyennganh.Until;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.content.ContextCompat;

import com.example.doanchuyennganh.Views.MainActivity;

public class isInternetAvailable {

    public static boolean isInternetAvai(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
