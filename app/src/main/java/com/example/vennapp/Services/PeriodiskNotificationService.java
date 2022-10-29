package com.example.vennapp.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class PeriodiskNotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        java.util.Calendar cal = Calendar.getInstance();
        Intent i = new Intent(this, NotifictionSendService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);

        AlarmManager alarm =(AlarmManager) getSystemService(Context.ALARM_SERVICE);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        int hour = sharedPreferences.getInt("hour",15);
        int minute  = sharedPreferences.getInt("minute",30);

        LocalTime localTime = LocalTime.of(hour, minute);
        LocalDate localDate = LocalDate.now();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,localDate.getYear());
        calendar.set(Calendar.MONTH,localDate.getMonthValue()-1);
        calendar.set(Calendar.DAY_OF_MONTH,localDate.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, localTime.getHour());
        calendar.set(Calendar.MINUTE, localTime.getMinute());

            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);

        return super.onStartCommand(intent, flags, startId);}
}
