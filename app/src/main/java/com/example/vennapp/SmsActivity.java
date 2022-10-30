package com.example.vennapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.vennapp.Services.NotifictionSendService;
import com.example.vennapp.database.DBHandlerAvtale;
import com.example.vennapp.database.DBHandlerKontaktAvtale;
import com.example.vennapp.database.models.Avtale;
import com.example.vennapp.database.models.Kontakt;

import java.util.List;

public class SmsActivity extends AppCompatActivity {
    private static int MY_PERMISSIONS_REQUEST_SEND_SMS =1 ;
    private static int MY_PHONE_STATE_PERMISSION = 1;
    DBHandlerAvtale dbHelperAvtale;
    DBHandlerKontaktAvtale dbHelperKontaktAvtale;

    public void SendSMS() {
        MY_PERMISSIONS_REQUEST_SEND_SMS = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        MY_PHONE_STATE_PERMISSION = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (MY_PERMISSIONS_REQUEST_SEND_SMS == PackageManager.PERMISSION_GRANTED&&
                MY_PHONE_STATE_PERMISSION ==
                        PackageManager.PERMISSION_GRANTED) {
            SmsManager smsMan = SmsManager.getDefault();
            SQLiteDatabase db;

            dbHelperAvtale = new DBHandlerAvtale(this);
            db = dbHelperAvtale.getWritableDatabase();

            dbHelperKontaktAvtale = new DBHandlerKontaktAvtale(this);
            db = dbHelperKontaktAvtale.getWritableDatabase();
            long currentTimeInMillis = System.currentTimeMillis();

            //Alle avtaler i dag
            List<Avtale> avtaler = dbHelperAvtale.finnAlleAvtalerMedGittDato(db, new java.sql.Date(currentTimeInMillis).toString());
            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            String defaultMessage = sharedPreferences.getString("message", "snart en avtale");

            //Alle kontakter for denne avtalen skal ha beskjed

            for (Avtale x : avtaler) {
                String melding = x.getMelding();
                if (melding == null || melding.isEmpty()) {

                    melding = defaultMessage;

                }

                List<Kontakt> kontakter = dbHelperKontaktAvtale.finnAlleKontakterGittAvtale(db, x.get_ID().longValue());

                for (Kontakt y : kontakter) {
                    smsMan.sendTextMessage(y.getTelefonNummer(), null,melding, null, null);

                };

            };


        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SendSMS();
        Intent mainIntentHome= new Intent(SmsActivity.this, MainActivity.class);
        startActivity(mainIntentHome);
    }
}
