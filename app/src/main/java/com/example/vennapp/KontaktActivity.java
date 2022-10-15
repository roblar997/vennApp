package com.example.vennapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.vennapp.database.DBHandlerKontakt;
import com.example.vennapp.database.DBHandlerKontaktAvtale;
import com.example.vennapp.database.models.Kontakt;

import java.util.List;

public class KontaktActivity extends AppCompatActivity {
    EditText fornavnInput;
    EditText etternavnInput;
    EditText friendId;
    EditText telefonInput;
    LinearLayout message;

    DBHandlerKontakt dbHelper;
    DBHandlerKontaktAvtale dbHelperKontaktAvtale;
    SQLiteDatabase db;
    public void leggtil(LinearLayout layout) {
        Kontakt kontakt = new Kontakt(fornavnInput.getText().toString(),etternavnInput.getText().toString(),telefonInput.getText().toString());
        dbHelper.leggTilKontakt(db,kontakt);

        visalle(layout);
    }

    public void slettKontakt(LinearLayout layout) {
        Long kontaktid = (Long.parseLong(friendId.getText().toString()));
        dbHelperKontaktAvtale.fjernAlleAvtalerFraKontakt(db,kontaktid);
        dbHelper.slettKontakt(db,kontaktid);
        visalle(layout);
    }
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
            List<Kontakt> kontakter = dbHelper.finnAlleKontakter(db);
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
                Button velg = new Button(this);
                velg.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                ));
                velg.setText("Velg");
                velg.setBackgroundColor(Color.parseColor("#8BC34A"));
                velg.setTextColor(Color.WHITE);
                velg.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                velg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        friendId.setText(kontakt.get_ID().toString());
                        telefonInput.setText(kontakt.getTelefonNummer());
                        fornavnInput.setText(kontakt.getFornavn());
                        etternavnInput.setText(kontakt.getEtternavn());
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


                velg.setWidth(80);

                layoutetBtn.setBackgroundColor(Color.WHITE);


                layoutet.addView(textTittel);
                layoutet.addView(textFornavn);
                layoutet.addView(textEtternavn);
                layoutet.addView(textTelefon);

                layoutetBtn.addView(velg);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mainIntent2= new Intent(KontaktActivity.this, MainActivity.class);
                startActivity(mainIntent2);
                return true;
            case R.id.home:
                Intent mainIntentHome= new Intent(KontaktActivity.this, MainActivity.class);
                startActivity(mainIntentHome);
                return true;
            case R.id.avtale:
                Intent mainIntentAvtale = new Intent(KontaktActivity.this, AvtaleActivity.class);
                startActivity(mainIntentAvtale);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontakt);
        fornavnInput = (EditText) findViewById(R.id.telefonInput);
        etternavnInput = (EditText) findViewById(R.id.etternavnInput);
        telefonInput = (EditText) findViewById(R.id.fornavnInput);
        Button leggTilBtn =  findViewById(R.id.leggTilBtn);
        Button oppdaterBtn =  findViewById(R.id.oppdaterBtn);
        Button slettBtn =  findViewById(R.id.slettBtn);
        Button visalleBtn =  findViewById(R.id.visalleBtn);
        friendId = (EditText) findViewById(R.id.kontaktId);
        message = (LinearLayout) findViewById(R.id.message);
        dbHelper = new DBHandlerKontakt(this);
        db=dbHelper.getWritableDatabase();

        dbHelperKontaktAvtale = new DBHandlerKontaktAvtale(this);
        db=dbHelperKontaktAvtale.getWritableDatabase();

        leggTilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leggtil(message);
            }
        });
        slettBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slettKontakt(message);
            }
        });
        oppdaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oppdater(message);
            }
        });
        visalleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visalle(message);
            }
        });
    }
    public void oppdater(LinearLayout layout) {
        Kontakt kontakt = new Kontakt();
        kontakt.setFornavn(fornavnInput.getText().toString());
        kontakt.setEtternavn(etternavnInput.getText().toString());
        kontakt.setTelefonNummer(telefonInput.getText().toString());
        kontakt.set_ID(Long.parseLong(friendId.getText().toString()));
        dbHelper.oppdaterKontakt(db, kontakt);
        visalle(layout);
    }
    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
