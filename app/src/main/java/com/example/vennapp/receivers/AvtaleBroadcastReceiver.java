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
        Toast.makeText(context.getApplicationContext(), "I BroadcastReceiver", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, PeriodiskNotificationService.class);
        context.startService(i);
    }
}
