package com.example.doanchuyennganh.Views;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.doanchuyennganh.BuildConfig;
import com.example.doanchuyennganh.Database.ItemsDatabase;
import com.example.doanchuyennganh.Models.Items;
import com.example.doanchuyennganh.R;
import com.example.doanchuyennganh.Receiver.TimerClockReceiver;
import com.example.doanchuyennganh.Service.ForegroundService;
import com.example.doanchuyennganh.Service.UpdateDataService;
import com.example.doanchuyennganh.Service.getRSS;
import com.example.doanchuyennganh.Until.FormatUntil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Items> ListItems = new ArrayList<>();
    public static final String SHARED_PREFERENCE = "FILE";
    public static int REST;
    public static int FLAG;
    public static Items itemsclick = new Items();


    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    AlertDialog dialog;
    public static String versionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();

        if(restorePrefData()){
            getAllItems();
        }else{

            showTimePicker();
        }
    }

    private void addControls() {
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        versionNumber = BuildConfig.VERSION_NAME;
        try {
            stopService(new Intent(this, UpdateDataService.class));
            stopService(new Intent(this, ForegroundService.class));
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(115);
            alarmManager.cancel(pendingIntent);
        }catch (Exception e){
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showTimePicker(){
        calendar =Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener listener= new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(calendar.HOUR_OF_DAY,hour);
                calendar.set(calendar.MINUTE,minute);
                setTime(calendar);
                showDialogWaiting();
                countTimerOn();
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                MainActivity.this, listener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),false);
        timePickerDialog.setTitle("Hẹn Giờ Cập Nhật !");
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                showDialogAgain();
            }
        });
        timePickerDialog.show();

    }

    private void showDialogWaiting(){
        android.app.AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_waittinggetdata,(ViewGroup) findViewById(R.id.layout_dialog_waittinggetdata));
        builder.setView(view);

        dialog= builder.create();
        if(dialog.getWindow() !=null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        final LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        lottieAnimationView.setSpeed(1);
        lottieAnimationView.playAnimation();


        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showDialogAgain(){
        android.app.AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_set_a_timer,(ViewGroup) findViewById(R.id.layout_dialog_set_a_timer));
        builder.setView(view);

        dialog= builder.create();

        if(dialog.getWindow() !=null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        final LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottieAnimationView_Clook);
        lottieAnimationView.setSpeed(1);
        lottieAnimationView.playAnimation();

        Button btn_OK_ = view.findViewById(R.id.btn_datgiolai);
        btn_OK_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showTimePicker();
            }
        });



        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void countTimerOn(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FLAG==200){
                    //saveData();
                    getAllItems();
                    Cancel();
                    dialog.dismiss();

                }else{
                    countTimerOn();
                }
            }
        },2000);
    }

    private void Cancel(){
        stopService(new Intent(this, getRSS.class));
    }

    private void setTime(Calendar calendar){

        int ALARM1_ID = 10000;
        final Intent intent= new Intent(MainActivity.this, TimerClockReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this,ALARM1_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT
        );
        long interval = 60 * 60 * 1000 * 6; // Đặt lịch cập nhật sau 6 tiếng.
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        interval, pendingIntent); // Set lap lai hang ngay cho alarm service

        saveTimeUpdate(calendar);

        Toast.makeText(MainActivity.this,"Đã đặt lịch cập nhật lúc: " + FormatUntil.formatTime(calendar.getTime()),Toast.LENGTH_LONG).show();

        Intent intentservice = new Intent(getApplicationContext(), getRSS.class);
        startService(intentservice);
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isOpnend",false);
        return  isIntroActivityOpnendBefore;
    }

    private void saveTimeUpdate(Calendar calendar){
        SharedPreferences pref = this.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("timeUpdate",FormatUntil.formatTime(calendar.getTime()));
        editor.putBoolean("isOpnend",true);
        editor.commit();
    }

    public static void saveData(Context context){
        //Toast.makeText(getApplicationContext(),ListItems.get(ListItems.size()-1).getTitle()+"",Toast.LENGTH_SHORT).show();
        SharedPreferences pref =context.getSharedPreferences(SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("oldData",ListItems.get(ListItems.size()-1).getLinkURL()+"");
        editor.commit();
        try {
            context.stopService(new Intent(context,getRSS.class));
        }catch (Exception e){

        }

    }

    private void getAllItems(){
        @SuppressLint("StaticFieldLeak")
        class GetItemsTask extends AsyncTask<Void,Void, List<Items>> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                MainActivity.ListItems.clear();
            }

            @Override
            protected List<Items> doInBackground(Void... voids) {
                return ItemsDatabase
                        .getDatabase(getApplicationContext())
                        .noteDao()
                        .getAllItems();
            }

            @Override
            protected void onPostExecute(List<Items> items) {
                super.onPostExecute(items);
                ListItems.addAll(items);
                saveData(getApplicationContext());
                Collections.sort(ListItems);
                MainActivity.REST=200;

            }
        }
        new GetItemsTask().execute();
    }


}