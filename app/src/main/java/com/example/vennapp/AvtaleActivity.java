package com.example.vennapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.vennapp.database.DBHandlerAvtale;
import com.example.vennapp.database.DBHandlerKontakt;
import com.example.vennapp.database.DBHandlerKontaktAvtale;
import com.example.vennapp.database.models.Avtale;
import com.example.vennapp.database.models.Kontakt;
import com.example.vennapp.database.models.KontaktAvtale;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AvtaleActivity extends AppCompatActivity {
    EditText tidInput;
    EditText datoInput;
    TextView timeError;
    TextView dateError;
    EditText meldingInput;
    EditText avtaleId;
    DBHandlerKontakt dbHelperKontakt;
    DBHandlerAvtale dbHelperAvtale;
    DBHandlerKontaktAvtale dbHelperKontaktAvtale;
    LinearLayout message;
    public static String PROVIDER_AVTALE ="com.example.vennapp.contentprovider.AvtaleProvider" ;
    public static final Uri CONTENT_AVTALE_URI = Uri.parse("content://"+ PROVIDER_AVTALE + "/avtale");

    SQLiteDatabase db;



    public void slettAvtale(LinearLayout layout) {
        if(avtaleId.getText().toString().isEmpty())
            return;
        dbHelperKontaktAvtale.fjernAlleKontaktFraAvtale(db,Long.parseLong(avtaleId.getText().toString()));
        dbHelperAvtale.slettAvtale(db,Long.parseLong(avtaleId.getText().toString()));

            getContentResolver().delete(Uri.parse("content://"+ PROVIDER_AVTALE + "/avtale/"+avtaleId.getText().toString()),null, new String[]{"_ID"});



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); inflater.inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mainIntent2= new Intent(AvtaleActivity.this, MainActivity.class);
                startActivity(mainIntent2);
                return true;
            case R.id.home:
                Intent mainIntentHome= new Intent(AvtaleActivity.this, MainActivity.class);
                startActivity(mainIntentHome);
                return true;
            case R.id.kontakt:
                Intent kontaktIntent = new Intent(AvtaleActivity.this, KontaktActivity.class);
                startActivity(kontaktIntent);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avtale);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        dbHelperAvtale = new DBHandlerAvtale(this);
        db=dbHelperAvtale.getWritableDatabase();

        dbHelperKontakt = new DBHandlerKontakt(this);
        db=dbHelperKontakt.getWritableDatabase();

        dbHelperKontaktAvtale = new DBHandlerKontaktAvtale(this);
        db=dbHelperKontaktAvtale.getWritableDatabase();

        datoInput = (EditText) findViewById(R.id.datoInput);
        tidInput = (EditText) findViewById(R.id.tidInput);
        meldingInput = (EditText) findViewById(R.id.meldingInput);

        avtaleId = (EditText) findViewById(R.id.avtaleId);
        message = (LinearLayout) findViewById(R.id.message);
        dateError = (TextView) findViewById(R.id.dateError);
        timeError = (TextView) findViewById(R.id.timeError);


        Button oppdaterBtn =  findViewById(R.id.oppdaterBtn);
        Button slettBtn =  findViewById(R.id.slettBtn);
        Button visalleKontakterBtn =  findViewById(R.id.visalleKontakterBtn);

        String avtaleIdText = getIntent().getStringExtra("avtaleId");
        if(avtaleIdText != null)
            avtaleId.setText(avtaleIdText);

        String datoText = getIntent().getStringExtra("dato");

        if(datoText != null )
            datoInput.setText(datoText);

        String tidText  = getIntent().getStringExtra("tid");
        tidInput.setText(tidText);

        String meldingText = getIntent().getStringExtra("melding");
        meldingInput.setText(meldingText);


        slettBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                slettAvtale(message);
            }
        });
        oppdaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oppdater(message);
            }
        });


        visalleKontakterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String avtaleInp = avtaleId.getText().toString();
                Intent mainIntent2= new Intent(AvtaleActivity.this, AvtaleKontaktActivity.class);
                mainIntent2.putExtra("avtaleId",avtaleId.getText().toString());
                mainIntent2.putExtra("dato",datoInput.getText().toString());
                mainIntent2.putExtra("tid",tidInput.getText().toString());
                mainIntent2.putExtra("melding",meldingInput.getText().toString());
                startActivity(mainIntent2);


            }
        });

    }
    public void oppdater(LinearLayout layout) {
        if(avtaleId.getText().toString().isEmpty())
            return;
        Avtale avtale = new Avtale();
        String dato = datoInput.getText().toString();
        String tid = tidInput.getText().toString();
        Pattern patternDato = Pattern.compile( "^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
        Matcher matcherDato = patternDato.matcher(dato);
        boolean found = true;
        if (!matcherDato.find()) {
            dateError.setText("Må være formatert yyyy-MM-dd");
            found = false;
        }
        Pattern patternTid = Pattern.compile( "^[0-9]{2}:[0-9]{2}:[0-9]{2}$");
        Matcher matcherTid = patternTid.matcher(tid);
        if (!matcherTid.find()) {
            timeError.setText("Må være formatert HH:mm:ss");
            found = false;
        }
        if(!found ){
            return;
        }

        avtale.setTid(tidInput.getText().toString());
        avtale.setDato(datoInput.getText().toString());
        avtale.setMelding(meldingInput.getText().toString());
        avtale.set_ID(Long.parseLong(avtaleId.getText().toString()));
        dbHelperAvtale.oppdaterAvtale(db, avtale);
        try{
            ContentValues v=new ContentValues();
            v.put("_ID",Long.parseLong(avtaleId.getText().toString()));
            v.put("tid",tidInput.getText().toString());
            v.put("dato",datoInput.getText().toString());
            v.put("melding",meldingInput.getText().toString());
            getContentResolver().update(Uri.parse("content://"+ PROVIDER_AVTALE + "/avtale/"+avtaleId.getText().toString()) ,v,null, new String[]{"_ID"});

        }
        catch (Exception ex){

        }


    }
    @Override
    protected void onDestroy() {
        dbHelperAvtale.close();
        dbHelperKontakt.close();
        dbHelperKontaktAvtale.close();
        super.onDestroy();
    }
}
