package com.sudhindra.delta_onsites_task_3.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra("text");
        String number = intent.getStringExtra("number");

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, text, null, null);
    }
}
