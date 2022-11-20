package com.example.vennapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
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

    TextView  responsAvtale;
    DBHandlerKontakt dbHelperKontakt;
    DBHandlerAvtale dbHelperAvtale;
    DBHandlerKontaktAvtale dbHelperKontaktAvtale;
    LinearLayout message;
    public static String PROVIDER_AVTALE ="com.example.vennapp.contentprovider.AvtaleProvider" ;
    public static String PROVIDER_KONTAKTAVTALE ="com.example.vennapp.contentprovider.KontaktAvtaleProvider" ;

    public static final Uri CONTENT_KONTAKTAVTALE_URI = Uri.parse("content://"+ PROVIDER_KONTAKTAVTALE + "/kontaktavtale");

    SQLiteDatabase db;




    public void slettAvtale(String id) {
        if(id == null || id.isEmpty())
            return;
        dbHelperKontaktAvtale.fjernAlleKontaktFraAvtale(db,Long.parseLong(id));
        dbHelperAvtale.slettAvtale(db,Long.parseLong(id));
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        responsAvtale.setText("Avtalen er slettet");

            try {
                getContentResolver().delete(Uri.parse("content://" + PROVIDER_AVTALE + "/avtale/" + id), null, new String[]{id});
            } catch (Exception ex) {

            }
            try {
                getContentResolver().delete(Uri.parse("content://" + PROVIDER_KONTAKTAVTALE + "/kontaktavtale/*/" + id), null, new String[]{String.valueOf(id)});
            }
            catch (Exception ex) {


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
        responsAvtale = (TextView) findViewById(R.id.responsAvtaleList);
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

    @Override
    protected void onDestroy() {
        dbHelperAvtale.close();
        dbHelperKontakt.close();
        dbHelperKontaktAvtale.close();
        super.onDestroy();
    }
}
