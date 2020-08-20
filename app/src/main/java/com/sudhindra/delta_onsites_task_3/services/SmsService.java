package com.sudhindra.delta_onsites_task_3.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.sudhindra.delta_onsites_task_3.MyApp.FAKE_CHANNEL;

public class SmsService extends Service {

    private static final String TAG = "SmsService";

    public Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            Log.i(TAG, "onStartCommand: thread start");

            String text = intent.getStringExtra("text");
            String number = intent.getStringExtra("number");
            String sec = intent.getStringExtra("sec");

            Log.i(TAG, "startService: sec=" + sec);

            handler.postDelayed(() -> {
                Log.i(TAG, "sms sent");
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(number, null, text, null, null);
                stopSelf();
            }, Integer.parseInt(sec) * 1000);
        }).start();

        Notification notification = new NotificationCompat.Builder(this, FAKE_CHANNEL)
                .setPriority(Notification.PRIORITY_MIN)
                .build();
        startForeground(2, notification);

        return Service.START_NOT_STICKY;
    }
}
