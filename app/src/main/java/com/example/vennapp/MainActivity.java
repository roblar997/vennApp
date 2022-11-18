package com.example.vennapp;

import static com.example.vennapp.contentprovider.AvtaleProvider.CONTENT_URI;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.example.vennapp.Services.NotifictionSendService;
import com.example.vennapp.Services.PeriodiskNotificationService;
import com.example.vennapp.database.DBHandlerAvtale;
import com.example.vennapp.database.DBHandlerKontakt;
import com.example.vennapp.database.DBHandlerKontaktAvtale;
import com.example.vennapp.database.models.Avtale;
import com.example.vennapp.database.models.Kontakt;
import com.example.vennapp.database.models.KontaktAvtale;
import com.example.vennapp.receivers.AvtaleBroadcastReceiver;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText fornavnInput;
    EditText etternavnInput;
    EditText friendId;
    EditText telefonInput;
    LinearLayout message;
    String CHANNEL_ID = "MinKanal3";

    DBHandlerKontakt dbHelperKontakt;
    DBHandlerAvtale dbHelperAvtale;
    DBHandlerKontaktAvtale dbHelperKontaktAvtale;

    public static String PROVIDER_AVTALE ="com.example.vennapp.contentprovider.AvtaleProvider" ;
    public static final Uri CONTENT_AVTALE_URI = Uri.parse("content://"+ PROVIDER_AVTALE + "/avtale");
    public static String PROVIDER_KONTAKT ="com.example.vennapp.contentprovider.KontaktProvider" ;
    public static final Uri CONTENT_KONTAKT_URI = Uri.parse("content://"+ PROVIDER_KONTAKT + "/kontakt");
    public static String PROVIDER_KONTAKTAVTALE ="com.example.vennapp.contentprovider.KontaktAvtaleProvider" ;

    public static final Uri CONTENT_KONTAKTAVTALE_URI = Uri.parse("content://"+ PROVIDER_KONTAKTAVTALE + "/kontaktavtale");

    SQLiteDatabase db;



    public void visalle(LinearLayout layout) {
        layout.removeAllViews();
        String tekst = "";
        try {
            TextView tittelTekst= new TextView(this);
            tittelTekst.setText("MINE KONTAKTER");

            tittelTekst.setTextColor(Color.BLACK);
            tittelTekst.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
            tittelTekst.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));

            layout.addView(tittelTekst);
            List<Kontakt> kontakter = dbHelperKontakt.finnAlleKontakter(db);
            for (Kontakt kontakt : kontakter) {

                CardView cardView = new CardView(this);
                cardView.setBackgroundColor(Color.BLACK);

                cardView.setContentPadding(10,10,10,10);

                Space space = new Space(this);
                space.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                space.setMinimumHeight(10);
                LinearLayout layoutet = new LinearLayout(this);
                layoutet.setOrientation(LinearLayout.VERTICAL);

                layoutet.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                TextView textTittel= new TextView(this);
                textTittel.setText("KONTAKT");
                textTittel.setBackgroundColor(Color.BLACK);
                textTittel.setTextColor(Color.WHITE);
                textTittel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                textTittel.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));

                TextView textFornavn = new TextView(this);
                SpannableString spannableStringFornavn = new SpannableString("Fornavn: " + kontakt.getFornavn());

                spannableStringFornavn.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textFornavn.setText(spannableStringFornavn);

                textFornavn.setBackgroundColor(Color.WHITE);
                textFornavn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textFornavn.setTextColor(Color.BLACK);

                textFornavn.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));

                SpannableString spannableStringEtternavn = new SpannableString("Etternavn: " + kontakt.getEtternavn());

                spannableStringEtternavn.setSpan(new StyleSpan(Typeface.BOLD), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                TextView textEtternavn = new TextView(this);
                textEtternavn.setText(spannableStringEtternavn);
                textEtternavn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textEtternavn.setTextColor(Color.BLACK);
                textEtternavn.setBackgroundColor(Color.WHITE);
                textEtternavn.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));

                TextView textTelefon = new TextView(this);

                SpannableString spannableStringTelefon = new SpannableString("Telefon: " + kontakt.getTelefonNummer());

                spannableStringTelefon.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                textTelefon.setText(spannableStringTelefon);
                textTelefon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                textTelefon.setTextColor(Color.BLACK);
                textTelefon.setBackgroundColor(Color.WHITE);
                textTelefon.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                Button endre = new Button(this);
                endre.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                endre.setText("Endre");
                endre.setBackgroundColor(Color.parseColor("#FFF599"));
                endre.setTextColor(Color.BLACK);
                endre.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                endre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        friendId.setText(kontakt.get_ID().toString());
                        telefonInput.setText(kontakt.getTelefonNummer());
                        fornavnInput.setText(kontakt.getFornavn());
                        etternavnInput.setText(kontakt.getEtternavn());
                    }
                });
                Button slett = new Button(this);

                slett.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                LinearLayout layoutetBtn = new LinearLayout(this);
                layoutetBtn.setOrientation(LinearLayout.HORIZONTAL);
                layoutetBtn.setGravity(Gravity.RIGHT);
                layoutetBtn.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                Space space1 = new Space(this);
                space1.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                Space space2 = new Space(this);
                space2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                space.setMinimumHeight(10);
                space1.setMinimumWidth(5);
                space2.setMinimumWidth(5);

                slett.setWidth(80);
                endre.setWidth(80);
                slett.setText("Slett");
                slett.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                layoutetBtn.setBackgroundColor(Color.WHITE);

                slett.setBackgroundColor(Color.parseColor("#BF2519"));
                slett.setTextColor(Color.BLACK);

                slett.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        friendId.setText(kontakt.get_ID().toString());
                        telefonInput.setText(kontakt.getTelefonNummer());
                        fornavnInput.setText(kontakt.getFornavn());
                        etternavnInput.setText(kontakt.getEtternavn());
                    }
                });

                layoutet.addView(textTittel);
                layoutet.addView(textFornavn);
                layoutet.addView(textEtternavn);
                layoutet.addView(textTelefon);

                layoutetBtn.addView(endre);
                layoutetBtn.addView(space2);
                layoutetBtn.addView(slett);
                layoutetBtn.addView(space1);
                layoutet.addView(layoutetBtn);
                cardView.addView(layoutet);

                layout.addView(space);
                layout.addView(cardView);
            }

        }
        catch (Exception ex){

        }


    }
    public void stoppPeriodisk(View v) {
        Intent i = new Intent(this, NotifictionSendService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if(alarm != null) {
            alarm.cancel(pintent);
        }
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("canShare",false);
        editor.apply();
        try{
            getContentResolver().delete(Uri.parse("content://"+ PROVIDER_KONTAKT + "/kontakt/#"),null, null);

        }
        catch (Exception ex){

        }
        try{
            getContentResolver().delete(Uri.parse("content://"+ PROVIDER_AVTALE + "/avtale/#"),null, null);

        }
        catch (Exception ex){

        }
        try{
            getContentResolver().delete(Uri.parse("content://"+ PROVIDER_KONTAKTAVTALE + "/kontaktavtale/#"),null, null);

        }
        catch (Exception ex){

        }
    }
    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    public void startService(View v) {
        Intent intent = new Intent(this, PeriodiskNotificationService.class);
        this.startService(intent);
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Resources res = getResources();
        int hour  = res.getInteger(R.integer.hourNotify);
        int minute = res.getInteger(R.integer.minuteNotify);
        String message = res.getString(R.string.message);
        // write all the data entered by the user in SharedPreference and apply
        editor.putInt("hour", hour);
        editor.putInt("minute", minute);
        editor.putString("message", message);
        editor.putBoolean("canShare",true);
        editor.apply();


        //Slett alt innhold fra kontakt tabellen
        try{
            getContentResolver().delete(Uri.parse("content://"+ PROVIDER_KONTAKT + "/kontakt/#"),null, null);

        }
        catch (Exception ex){

        }
        try{
            getContentResolver().delete(Uri.parse("content://"+ PROVIDER_AVTALE + "/avtale/#"),null, null);

        }
        catch (Exception ex){

        }
        try{
            getContentResolver().delete(Uri.parse("content://"+ PROVIDER_KONTAKTAVTALE + "/kontaktavtale/#"),null, null);

        }
        catch (Exception ex){

        }
        List<Kontakt> kontakter = dbHelperKontakt.finnAlleKontakter(db);
        List<Avtale> avtaler = dbHelperAvtale.finnAlleAvtaler(db);
        List<KontaktAvtale> kontaktAvtale = dbHelperKontaktAvtale.finnAlleKontaktAvtaler(db);
        //Initialiser contentprovider
        kontakter.forEach((x)->{
            //Prøv å legg til
            try{
                ContentValues values=new ContentValues();

                values.put("_ID",x.get_ID());
                values.put("Fornavn",x.getFornavn());
                values.put("Etternavn",x.getEtternavn());
                values.put("Telefon",x.getTelefonNummer());
                getContentResolver().insert(CONTENT_KONTAKT_URI,values);
            }
            catch (Exception ex){

            }
        });
        avtaler.forEach((x)->{
            ContentValues values=new ContentValues();
            values.put("_ID",x.get_ID());
            values.put("tid",x.getTid());
            values.put("dato",x.getDato());
            values.put("melding",x.getMelding());
            try{


                getContentResolver().insert(CONTENT_AVTALE_URI,values);

            }
            catch(Exception ex){


            }
        });
        kontaktAvtale.forEach((x)->{
            //Prøv å legg til
            try{

                ContentValues values=new ContentValues();
                values.put("kontaktId",x.getKontaktId());
                values.put("avtaleId",x.getAvtaleId());

                getContentResolver().insert(CONTENT_KONTAKTAVTALE_URI,values);

            }
            catch(Exception ex){

            }
        });


        Intent periodiskIntent = new Intent();
        periodiskIntent.setAction("com.example.service.PeriodiskNotificationService");
        sendBroadcast(periodiskIntent);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button avtaleBtn = findViewById(R.id.avtaleBtn);
        Button kontaktBtn = findViewById(R.id.kontaktBtn);
        Button stopPeriodiskServiceBtn = findViewById(R.id.stopPeriodiskServiceBtn);
        Button startPeriodiskServiceBtn = findViewById(R.id.startPeriodiskServiceBtn);


        dbHelperAvtale = new DBHandlerAvtale(this);
        dbHelperKontakt = new DBHandlerKontakt(this);
        db=dbHelperKontakt.getWritableDatabase();




        dbHelperKontaktAvtale = new DBHandlerKontaktAvtale(this);
        BroadcastReceiver myBroadcastReceiver = new AvtaleBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.example.service.PeriodiskNotificationService");
        filter.addAction("com.example.service.PeriodiskNotificationService");
        this.registerReceiver(myBroadcastReceiver, filter);
        createNotificationChannel();
        avtaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent avtaleIntent = new Intent(MainActivity.this, AvtaleListActivity.class);
                startActivity(avtaleIntent);

            }
        });
        stopPeriodiskServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               stoppPeriodisk(view);

            }
        });
        startPeriodiskServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(view);

            }
        });

        kontaktBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kontaktIntent = new Intent(MainActivity.this, KontaktListActivity.class);
                startActivity(kontaktIntent);

            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelperKontakt.close();
        dbHelperAvtale.close();
        dbHelperKontaktAvtale.close();
        super.onDestroy();
    }
}
