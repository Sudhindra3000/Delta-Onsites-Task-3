package com.sudhindra.delta_onsites_task_3.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.sudhindra.delta_onsites_task_3.databinding.ActivityMainBinding;
import com.sudhindra.delta_onsites_task_3.receivers.AlertReceiver;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST = 10;
    private ActivityMainBinding binding;

    private Calendar calendar = Calendar.getInstance();
    private MaterialTimePicker timePicker = MaterialTimePicker.newInstance();

    private String text, number;
    private int h, m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkSMSPermission();

        binding.timeTv.setText(getCurrentTime());

        timePicker.setListener(listener);

        h = calendar.get(Calendar.HOUR_OF_DAY);
        m = calendar.get(Calendar.MINUTE);
    }

    private void checkSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void checkFields(View view) {
        text = binding.textEt.getText().toString();
        number = binding.numEt.getText().toString();
        if (text.trim().isEmpty() || number.trim().isEmpty())
            Toast.makeText(this, "Enter all the Fields", Toast.LENGTH_SHORT).show();
        else if (number.length() != 10)
            Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show();
        else
            sendSMS();
    }

    public void sendSMS() {
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("text", text);
        intent.putExtra("number", number);


        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Your SMS will be Sent at " + getCalendarTime(calendar), Toast.LENGTH_SHORT).show();
    }

    public void showTimePicker(View view) {
        calendar = Calendar.getInstance();
        timePicker = MaterialTimePicker.newInstance();
        timePicker.setListener(listener);
        timePicker.setHour(h);
        timePicker.setMinute(m);
        timePicker.show(getSupportFragmentManager(), "Picker");
    }

    private String getCurrentTime() {
        calendar = Calendar.getInstance();
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
    }

    private String getCalendarTime(Calendar calendar) {
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
    }

    private MaterialTimePicker.OnTimeSetListener listener = dialog -> {
        h = dialog.getHour();
        m = dialog.getMinute();

        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, 0);

        binding.timeTv.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime()));
    };
}