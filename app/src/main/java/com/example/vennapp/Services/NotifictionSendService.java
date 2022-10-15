package com.example.vennapp.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
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

        //Alle kontakter for denne avtalen skal ha beskjed
        for (Avtale x : avtaler) {
            String melding = x.getMelding();
            List<Kontakt> kontakter = dbHelperKontaktAvtale.finnAlleKontakterGittAvtale(db, x.get_ID());
        }

        Toast.makeText(getApplicationContext(), "Du har snart en avtale ", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, ResultatActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
        Notification notifikasjon = new NotificationCompat.Builder(this,"MinKanal")
                .setContentTitle("MinNotifikasjon")
                .setContentText("Tekst Teskt tekst")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pIntent).build();
        notifikasjon.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(88, notifikasjon);
        return super.onStartCommand(intent, flags, startId);
    }
}
