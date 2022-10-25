package com.example.vennapp.Services;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.vennapp.R;
import com.example.vennapp.ResultatActivity;
import com.example.vennapp.SmsActivity;
import com.example.vennapp.database.DBHandlerAvtale;
import com.example.vennapp.database.DBHandlerKontakt;
import com.example.vennapp.database.DBHandlerKontaktAvtale;
import com.example.vennapp.database.models.Avtale;
import com.example.vennapp.database.models.Kontakt;

import java.util.List;

public class NotifictionSendService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent i2 = new Intent(this, ResultatActivity.class);
        Intent iSMS = new Intent(this, SmsActivity.class);


        NotificationManager notificationManager = (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);
        Toast.makeText(getApplicationContext(), "Du har snart en avtale ", Toast.LENGTH_SHORT).show();

        PendingIntent pIntent2 = PendingIntent.getActivity(this, 0, i2, 0);
        PendingIntent pIntentsms = PendingIntent.getActivity(this, 0, iSMS, 0);
        try {
            pIntentsms.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        Notification notifikasjon = new NotificationCompat.Builder(this,"MinKanal")
                .setContentTitle("Snart avtale")
                .setContentText("Er snart en avtale")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pIntent2).build();
     //   Notification notifikasjonSms = new NotificationCompat.Builder(this,"MinKanal").setContentIntent(pIntentsms).build();
        notifikasjon.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(88, notifikasjon);
     //   notificationManager.notify(88, notifikasjonSms);
        return super.onStartCommand(intent, flags, startId);
    }
}
