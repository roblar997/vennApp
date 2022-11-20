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

import com.example.vennapp.database.DBHandlerKontakt;
import com.example.vennapp.database.DBHandlerKontaktAvtale;
import com.example.vennapp.database.models.Kontakt;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class NyKontaktActivity extends AppCompatActivity {
    EditText fornavnInput;
    EditText etternavnInput;
    EditText friendId;
    EditText telefonInput;
    TextView fornavnError;
    TextView etternavnError;

    TextView telefonError;
    LinearLayout message;
    public static String PROVIDER_KONTAKT ="com.example.vennapp.contentprovider.KontaktProvider" ;
    public static final Uri CONTENT_KONTAKT_URI = Uri.parse("content://"+ PROVIDER_KONTAKT + "/kontakt");
    TextView  responsKontakt;
    DBHandlerKontakt dbHelper;
    DBHandlerKontaktAvtale dbHelperKontaktAvtale;
    SQLiteDatabase db;
    public void leggtil(LinearLayout layout) {

        Pattern patternFornavn = Pattern.compile( "^[A-Za-z]{2,30}$");
        Matcher matcherFornavn = patternFornavn.matcher(fornavnInput.getText().toString());



        boolean found = true;
        if (!matcherFornavn.find()) {
            fornavnError.setText("Ugyldig fornavn");

            found = false;
        }
        else
            fornavnError.setText("");
        Pattern patternEtternavn = Pattern.compile( "^[A-Za-z]{2,30}$");
        Matcher matcherEtternavn = patternEtternavn.matcher(etternavnInput.getText().toString());
        if (!matcherEtternavn.find()) {
            etternavnError.setText("Ugyldig etternavn");
            found = false;
        }
        else
            etternavnError.setText("");

        Pattern patternTelefon = Pattern.compile( "^[+]?[0-9]{3,40}$");
        Matcher matcherTelefon = patternTelefon.matcher(telefonInput.getText().toString());
        if (!matcherTelefon.find()) {
            telefonError.setText("Ugyldig telefon nummer");
            found = false;
        }
        else
            telefonError.setText("");
        if(!found ){
            responsKontakt.setText("");
            return;
        }
        Kontakt kontakt = new Kontakt(fornavnInput.getText().toString(),etternavnInput.getText().toString(),telefonInput.getText().toString());
        dbHelper.leggTilKontakt(db,kontakt);
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        responsKontakt.setText("Kontakten er laget");


            try {
                ContentValues v = new ContentValues();


                v.put("Fornavn", fornavnInput.getText().toString());
                v.put("Etternavn", etternavnInput.getText().toString());
                v.put("Telefon", telefonInput.getText().toString());
                getContentResolver().insert(CONTENT_KONTAKT_URI, v);
            } catch (Exception ex) {

            }

        telefonInput.setText("");
        fornavnInput.setText("");
        etternavnInput.setText("");

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
                Intent mainIntent2= new Intent(NyKontaktActivity.this, KontaktListActivity.class);
                startActivity(mainIntent2);
                return true;
            case R.id.home:
                Intent mainIntentHome= new Intent(NyKontaktActivity.this, KontaktListActivity.class);
                startActivity(mainIntentHome);
                return true;
            case R.id.avtale:
                Intent i2 = new Intent(this, ResultatActivity.class);
                Intent mainIntentAvtale = new Intent(NyKontaktActivity.this, AvtaleActivity.class);
                startActivity(mainIntentAvtale);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nykontakt);
        fornavnInput = (EditText) findViewById(R.id.fornavnInput);
        etternavnInput = (EditText) findViewById(R.id.etternavnInput);
        telefonInput = (EditText) findViewById(R.id.telefonInput);

        fornavnError= (TextView) findViewById(R.id.fornavnError);
        etternavnError = (TextView) findViewById(R.id.etternavnError);
        telefonError = (TextView) findViewById(R.id.telefonError);

        Button leggTilBtn =  findViewById(R.id.leggTilBtn);

        responsKontakt = (TextView) findViewById(R.id.responsNyKontakt);
        friendId = (EditText) findViewById(R.id.kontaktId);
        message = (LinearLayout) findViewById(R.id.message);
        dbHelper = new DBHandlerKontakt(this);
        db=dbHelper.getWritableDatabase();
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        dbHelperKontaktAvtale = new DBHandlerKontaktAvtale(this);
        db=dbHelperKontaktAvtale.getWritableDatabase();
        String friendIdText = getIntent().getStringExtra("friendId");
        if(friendId != null)
            friendId.setText(friendIdText);

        String telefonText = getIntent().getStringExtra("telefon");

        if(telefonText != null )
            telefonInput.setText(telefonText);

        String fornavnText  = getIntent().getStringExtra("fornavn");
        fornavnInput.setText(fornavnText);

        String etternavnText = getIntent().getStringExtra("etternavn");
        etternavnInput.setText(etternavnText);

        leggTilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leggtil(message);
            }
        });


    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
