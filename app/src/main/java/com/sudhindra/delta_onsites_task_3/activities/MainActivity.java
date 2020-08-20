package com.sudhindra.delta_onsites_task_3.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sudhindra.delta_onsites_task_3.databinding.ActivityMainBinding;
import com.sudhindra.delta_onsites_task_3.services.SmsService;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST = 10;
    private ActivityMainBinding binding;

    private String text, number, sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkSMSPermission();
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
        sec = binding.timeEt.getText().toString();
        if (text.trim().isEmpty() || number.trim().isEmpty() || sec.trim().isEmpty())
            Toast.makeText(this, "Enter all the Fields", Toast.LENGTH_SHORT).show();
        else if (number.length() != 10)
            Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show();
        else
            sendSMS();
    }

    public void sendSMS() {
        Intent intent = new Intent(this, SmsService.class);
        intent.putExtra("text", text);
        intent.putExtra("number", number);
        intent.putExtra("sec", sec);
        ContextCompat.startForegroundService(this, intent);
        Toast.makeText(this, "Your Sms will be sent after " + sec + " seconds", Toast.LENGTH_SHORT).show();
    }
}