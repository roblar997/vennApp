package com.example.vennapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vennapp.database.DBHandlerAvtale;
import com.example.vennapp.database.DBHandlerKontakt;
import com.example.vennapp.database.DBHandlerKontaktAvtale;
import com.example.vennapp.database.models.Avtale;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NyAvtaleActivity extends AppCompatActivity {
    EditText tidInput;
    EditText datoInput;
    TextView timeError;
    TextView dateError;
    TextView  responsAvtale;
    EditText meldingInput;
    EditText avtaleId;
    DBHandlerKontakt dbHelperKontakt;
    DBHandlerAvtale dbHelperAvtale;
    DBHandlerKontaktAvtale dbHelperKontaktAvtale;
    LinearLayout message;
    public static String PROVIDER_AVTALE ="com.example.vennapp.contentprovider.AvtaleProvider" ;
    public static final Uri CONTENT_AVTALE_URI = Uri.parse("content://"+ PROVIDER_AVTALE + "/avtale");

    SQLiteDatabase db;
    public void leggtil(LinearLayout layout) {
        String dato = datoInput.getText().toString();
        String tid = tidInput.getText().toString();
        Pattern patternDato = Pattern.compile( "^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
        Matcher matcherDato = patternDato.matcher(dato);



        boolean found = true;
        if (!matcherDato.find()) {
            dateError.setText("Må være formatert yyyy-MM-dd");
            found = false;
        }
        else
            dateError.setText("");
        Pattern patternTid = Pattern.compile( "^[0-9]{2}:[0-9]{2}:[0-9]{2}$");
        Matcher matcherTid = patternTid.matcher(tid);
        if (!matcherTid.find()) {
            timeError.setText("Må være formatert HH:mm:ss");
            found = false;
        }
        else
            timeError.setText("");
        if(!found ){
            responsAvtale.setText("");
            return;
        }
        responsAvtale.setText("Avtalen er laget");
        Avtale avtale = new Avtale(datoInput.getText().toString(),tidInput.getText().toString(),meldingInput.getText().toString());
        dbHelperAvtale.leggTilAvtale(db,avtale);
        //Prøv å legg til
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

            ContentValues v = new ContentValues();

            v.put("tid", datoInput.getText().toString());
            v.put("dato", tidInput.getText().toString());
            v.put("melding", meldingInput.getText().toString());
            try {


                getContentResolver().insert(CONTENT_AVTALE_URI, v);

            } catch (Exception ex) {



            datoInput.setText("");
            tidInput.setText("");
            meldingInput.setText("");
        }

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
                Intent mainIntent2= new Intent(NyAvtaleActivity.this, AvtaleListActivity.class);
                startActivity(mainIntent2);
                return true;
            case R.id.home:
                Intent mainIntentHome= new Intent(NyAvtaleActivity.this, AvtaleListActivity.class);
                startActivity(mainIntentHome);
                return true;
            case R.id.kontakt:
                Intent kontaktIntent = new Intent(NyAvtaleActivity.this, KontaktActivity.class);
                startActivity(kontaktIntent);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyavtale);
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
        responsAvtale = (TextView) findViewById(R.id.responsNyAvtale);
        avtaleId = (EditText) findViewById(R.id.avtaleId);

        dateError = (TextView) findViewById(R.id.dateError);
        timeError = (TextView) findViewById(R.id.timeError);

        Button leggTilBtn =  findViewById(R.id.leggTilBtn);


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

        leggTilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leggtil(message);
            }
        });


    }

    @Override
    protected void onDestroy() {
        dbHelperAvtale.close();
        dbHelperKontakt.close();
        dbHelperKontaktAvtale.close();
        super.onDestroy();
    }
}
