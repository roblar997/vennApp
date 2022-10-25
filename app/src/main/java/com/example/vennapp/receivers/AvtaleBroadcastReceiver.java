package com.example.vennapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.vennapp.Services.PeriodiskNotificationService;

public class AvtaleBroadcastReceiver  extends BroadcastReceiver {
    public AvtaleBroadcastReceiver(){};

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, PeriodiskNotificationService.class);
        context.startService(i);
    }
}
