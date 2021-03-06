package com.example.doanchuyennganh.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.doanchuyennganh.R;
import com.example.doanchuyennganh.Receiver.TimerClockReceiver;
import com.example.doanchuyennganh.Until.FormatUntil;
import com.example.doanchuyennganh.Views.MainActivity;

import java.util.Calendar;

public class SettingFragment extends Fragment {

    View view;
    String time = "";
    TextView txt_hengio, txt_version;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    ImageView btn_back_to_Home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        addControls();
        addEvents();
        getDataCache();
        return view;
    }

    private void addControls() {
        txt_hengio = view.findViewById(R.id.txt_hengio);
        btn_back_to_Home = view.findViewById(R.id.btn_back_to_Home);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        txt_version = view.findViewById(R.id.txt_numberver);

        txt_version.setText("Phiên Bản: " + MainActivity.versionNumber);
    }

    private void addEvents() {
        txt_hengio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
        btn_back_to_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
    }

    private boolean getDataCache() {

        SharedPreferences pref = getActivity().getSharedPreferences(MainActivity.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        time = pref.getString("timeUpdate", "NULL");
        txt_hengio.setText(time);
        return true;
    }

    private void showTimePicker() {
        calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(calendar.HOUR_OF_DAY, hour);
                calendar.set(calendar.MINUTE, minute);
                setTime(calendar);
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(), listener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false);


        timePickerDialog.show();

    }

    private void setTime(Calendar calendar) {

        int ALARM1_ID = 10000;

        final Intent intent = new Intent(getActivity(), TimerClockReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                getActivity(), ALARM1_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        long interval = 60 * 60 * 1000 * 6; // Đặt lịch cập nhật sau 6 tiếng.
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pendingIntent);

        saveTimeUpdate(calendar);

        Toast.makeText(getActivity(), "Đã thay đổi lịch cập nhật : " + FormatUntil.formatTime(calendar.getTime()), Toast.LENGTH_LONG).show();

    }

    private void saveTimeUpdate(Calendar calendar) {
        SharedPreferences pref = getActivity().getSharedPreferences(MainActivity.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("timeUpdate", FormatUntil.formatTime(calendar.getTime()));
        txt_hengio.setText(FormatUntil.formatTime(calendar.getTime()));
        editor.commit();
    }
}