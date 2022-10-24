package com.example.vennapp.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.vennapp.R;
import com.example.vennapp.ResultatActivity;
import com.example.vennapp.database.DBHandlerAvtale;
import com.example.vennapp.database.DBHandlerKontakt;
import com.example.vennapp.database.DBHandlerKontaktAvtale;
import com.example.vennapp.database.models.Avtale;
import com.example.vennapp.database.models.Kontakt;

import java.util.List;

public class NotifictionSendService  extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}

    DBHandlerAvtale dbHelperAvtale;
    DBHandlerKontaktAvtale dbHelperKontaktAvtale;

    public int onStartCommand(Intent intent, int flags, int startId) {
        SQLiteDatabase db;

        dbHelperAvtale = new DBHandlerAvtale(this);
        db=dbHelperAvtale.getWritableDatabase();

        dbHelperKontaktAvtale = new DBHandlerKontaktAvtale(this);
        db=dbHelperKontaktAvtale.getWritableDatabase();
        long currentTimeInMillis =System.currentTimeMillis();

        //Alle avtaler i dag
        List<Avtale> avtaler = dbHelperAvtale.finnAlleAvtalerMedGittDato(db,new java.sql.Date(currentTimeInMillis).toString());
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String defaultMessage = sharedPreferences.getString("message","hi!");
        //Alle kontakter for denne avtalen skal ha beskjed
        for (Avtale x : avtaler) {
            String melding = x.getMelding();
            if(melding == null || melding.isEmpty()){

                melding = defaultMessage;

            }
            Intent smsIntent =new Intent(getApplicationContext(),NotifictionSendService.class);
            PendingIntent smsPendingIntent =PendingIntent.getActivity(getApplicationContext(), 0, smsIntent,0);

            SmsManager sms=SmsManager.getDefault();

            List<Kontakt> kontakter = dbHelperKontaktAvtale.finnAlleKontakterGittAvtale(db, x.get_ID());
            for(Kontakt y : kontakter){
                    sms.sendTextMessage(y.getTelefonNummer(), null, melding, smsPendingIntent,null);
            };

        };


        NotificationManager notificationManager = (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, ResultatActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
        Notification notifikasjon = new NotificationCompat.Builder(this,"MinKanal")
                .setContentTitle("Snart avtale")
                .setContentText("Er snart en avtale")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pIntent).build();
        notifikasjon.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(88, notifikasjon);
        return super.onStartCommand(intent, flags, startId);
    }
}
