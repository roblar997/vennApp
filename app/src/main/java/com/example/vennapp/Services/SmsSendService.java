package com.example.vennapp.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.vennapp.R;
import com.example.vennapp.ResultatActivity;
import com.example.vennapp.SmsActivity;

public class SmsSendService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent iSMS = new Intent(this, SmsActivity.class);


        PendingIntent pIntentsms = PendingIntent.getActivity(this, 0, iSMS, 0);
        try {
            pIntentsms.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
