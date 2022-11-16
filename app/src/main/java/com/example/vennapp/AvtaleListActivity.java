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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AvtaleListActivity extends AppCompatActivity {
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
        Pattern patternTid = Pattern.compile( "^[0-9]{2}:[0-9]{2}:[0-9]{2}$");
        Matcher matcherTid = patternTid.matcher(tid);
        if (!matcherTid.find()) {
            timeError.setText("Må være formatert HH:mm:ss");
            found = false;
        }
        if(!found ){
            return;
        }
        Avtale avtale = new Avtale(datoInput.getText().toString(),tidInput.getText().toString(),meldingInput.getText().toString());
        dbHelperAvtale.leggTilAvtale(db,avtale);
        //Prøv å legg til
        try{
            Long id = dbHelperAvtale.getMaxId(db);
            ContentValues v=new ContentValues();
            v.put("_ID",id);
            v.put("tid",datoInput.getText().toString());
            v.put("dato",tidInput.getText().toString());
            v.put("melding",meldingInput.getText().toString());
            getContentResolver().insert(CONTENT_AVTALE_URI,v);
            visalle(layout);
        }
        catch(Exception ex){

        }

    }


    public void visalleKontakter(LinearLayout layout) {
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
                Button leggtil = new Button(this);
                leggtil.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                leggtil.setText("Legg til");
                leggtil.setBackgroundColor(Color.parseColor("#8BC34A"));
                leggtil.setTextColor(Color.WHITE);
                leggtil.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                leggtil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String avtaleInp = avtaleId.getText().toString();
                        if(!avtaleInp.isEmpty()){

                        }
                    }
                });

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

                leggtil.setWidth(80);

                layoutetBtn.setBackgroundColor(Color.WHITE);



                layoutet.addView(textTittel);
                layoutet.addView(textFornavn);
                layoutet.addView(textEtternavn);
                layoutet.addView(textTelefon);

                layoutetBtn.addView(leggtil);
                layoutetBtn.addView(space2);

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
    public void slettAvtale(String id) {
        if(id == null || id.isEmpty())
            return;
        dbHelperKontaktAvtale.fjernAlleKontaktFraAvtale(db,Long.parseLong(id));
        dbHelperAvtale.slettAvtale(db,Long.parseLong(id));
        try{
            getContentResolver().delete(Uri.parse("content://"+ PROVIDER_AVTALE + "/avtale/"+avtaleId.getText().toString()),null, new String[]{avtaleId.getText().toString()});
        }
        catch (Exception ex){

        }

        visalle(message);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void visalle(LinearLayout layout) {
        layout.removeAllViews();
        String tekst = "";
        try {
            TextView tittelTekst= new TextView(this);
            tittelTekst.setText("MINE AVTALER");

            tittelTekst.setTextColor(Color.BLACK);
            tittelTekst.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
            tittelTekst.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));

            layout.addView(tittelTekst);
            List<Avtale> avtaler = dbHelperAvtale.finnAlleAvtaler(db);
            for (Avtale avtale: avtaler) {

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
                textTittel.setText("AVTALE");
                textTittel.setBackgroundColor(Color.BLACK);
                textTittel.setTextColor(Color.WHITE);
                textTittel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                textTittel.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));

                TextView textDato = new TextView(this);
                SpannableString spannableStringDato = new SpannableString("Dato: " + avtale.getDato());

                spannableStringDato.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textDato.setText(spannableStringDato);

                textDato.setBackgroundColor(Color.WHITE);
                textDato.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textDato.setTextColor(Color.BLACK);

                textDato.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));

                SpannableString spannableStringTid= new SpannableString("Tid: " + avtale.getTid());

                spannableStringTid.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                TextView textTid = new TextView(this);
                textTid.setText(spannableStringTid);
                textTid.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textTid.setTextColor(Color.BLACK);
                textTid.setBackgroundColor(Color.WHITE);
                textTid.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));

                TextView textMelding = new TextView(this);

                SpannableString spannableStringMelding = new SpannableString("Melding: " + avtale.getMelding());

                spannableStringMelding.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                textMelding.setText(spannableStringMelding);
                textMelding.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                textMelding.setTextColor(Color.BLACK);
                textMelding.setBackgroundColor(Color.WHITE);
                textMelding.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));

                Button velg = new Button(this);

                velg.setLayoutParams(new LinearLayout.LayoutParams(
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

                velg.setWidth(80);

                velg.setText("Velg");
                velg.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                layoutetBtn.setBackgroundColor(Color.WHITE);

                velg.setBackgroundColor(Color.parseColor("#8BC34A"));
                velg.setTextColor(Color.WHITE);

                velg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent mainIntent2= new Intent(AvtaleListActivity.this, AvtaleActivity.class);
                        mainIntent2.putExtra("avtaleId",avtale.get_ID().toString());
                        mainIntent2.putExtra("dato",avtale.getDato());
                        mainIntent2.putExtra("tid",avtale.getTid());
                        mainIntent2.putExtra("melding",avtale.getMelding());
                        startActivity(mainIntent2);

                    }
                });
                Button slett = new Button(this);
                slett.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                slett.setText("Slett");
                
                slett.setBackgroundColor(Color.parseColor("#BF2519"));
                slett.setTextColor(Color.WHITE);
                slett.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                slett.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        slettAvtale(String.valueOf(avtale.get_ID()));

                    }
                });

                layoutet.addView(textTittel);
                layoutet.addView(textDato);
                layoutet.addView(textTid);
                layoutet.addView(textMelding);


                layoutetBtn.addView(space2);
                layoutetBtn.addView(velg);
                layoutetBtn.addView(space1);
                layoutetBtn.addView(slett);
                layoutet.addView(layoutetBtn);
                cardView.addView(layoutet);

                layout.addView(space);
                layout.addView(cardView);
            }

        }
        catch (Exception ex){

        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mainIntent2= new Intent(AvtaleListActivity.this, MainActivity.class);
                startActivity(mainIntent2);
                return true;
            case R.id.home:
                Intent mainIntentHome= new Intent(AvtaleListActivity.this, MainActivity.class);
                startActivity(mainIntentHome);
                return true;
            case R.id.kontakt:
                Intent kontaktIntent = new Intent(AvtaleListActivity.this, KontaktActivity.class);
                startActivity(kontaktIntent);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avtalelist);

        dbHelperAvtale = new DBHandlerAvtale(this);
        db=dbHelperAvtale.getWritableDatabase();

        dbHelperKontakt = new DBHandlerKontakt(this);
        db=dbHelperKontakt.getWritableDatabase();

        dbHelperKontaktAvtale = new DBHandlerKontaktAvtale(this);
        db=dbHelperKontaktAvtale.getWritableDatabase();

        message = (LinearLayout) findViewById(R.id.message);
        Button nyAvtaleBtn =  findViewById(R.id.nyAvtale);
        nyAvtaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent2= new Intent(AvtaleListActivity.this, NyAvtaleActivity.class);

                startActivity(mainIntent2);
            }
        });
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        visalle(message);

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
            getContentResolver().update(Uri.parse("content://"+ PROVIDER_AVTALE + "/avtale/"+avtaleId.getText().toString()) ,v,null, null);

        }
        catch (Exception ex){

        }

        visalle(layout);
    }
    @Override
    protected void onDestroy() {
        dbHelperAvtale.close();
        dbHelperKontakt.close();
        dbHelperKontaktAvtale.close();
        super.onDestroy();
    }
}