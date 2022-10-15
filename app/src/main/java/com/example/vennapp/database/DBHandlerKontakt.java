package com.example.vennapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.vennapp.database.models.Avtale;
import com.example.vennapp.database.models.Kontakt;

import java.util.ArrayList;
import java.util.List;

public class DBHandlerKontakt extends SQLiteOpenHelper {
    static String TABLE_KONTAKTER = "Kontakter";

    static String KEY_ID = "_ID";
    static String KEY_FORNAVN = "Fornavn";
    static String KEY_ETTERNAVN = "Etternavn";
    static String KEY_TELEFON = "Telefon";



    static int DATABASE_VERSION = 3;static String DATABASE_NAME = "telefonkontakt";
    public DBHandlerKontakt(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String LAG_TABELL = "CREATE TABLE IF NOT EXISTS " + TABLE_KONTAKTER + "(" + KEY_ID +" INTEGER PRIMARY KEY ," + KEY_FORNAVN + " TEXT," + KEY_ETTERNAVN + " TEXT," + KEY_TELEFON + " TEXT" + ")";
        Log.d("SQL", LAG_TABELL);
        db.execSQL(LAG_TABELL);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KONTAKTER );
        onCreate(db);
    }
    public void slettKontakt(SQLiteDatabase db, Long inn_id) {
        db.delete(TABLE_KONTAKTER , KEY_ID + " =? ",
                new String[]{Long.toString(inn_id)});
    }
    public List<Kontakt> finnAlleKontakter(SQLiteDatabase db) {

        List<Kontakt> kontaktListe = new ArrayList<Kontakt>();
        String selectQuery = "SELECT * FROM " + TABLE_KONTAKTER;
        Log.d("SQL", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Kontakt kontakt = new Kontakt();
                kontakt.set_ID(cursor.getLong(0));
                kontakt.setFornavn(cursor.getString(1));
                kontakt.setEtternavn(cursor.getString(2));
                kontakt.setTelefonNummer(cursor.getString(3));
                kontaktListe.add(kontakt);}
            while (cursor.moveToNext());
            cursor.close();
        }
        return kontaktListe;
    }
    public int oppdaterKontakt(SQLiteDatabase db, Kontakt kontakt) {
        ContentValues values = new ContentValues();
        values.put(KEY_FORNAVN, kontakt.getFornavn());
        values.put(KEY_ETTERNAVN, kontakt.getEtternavn());
        values.put(KEY_TELEFON, kontakt.getTelefonNummer());
        int endret = db.update(TABLE_KONTAKTER , values, KEY_ID + "= ?",
                new String[]{String.valueOf(kontakt.get_ID())});
        return endret;
    }
    public void leggTilKontakt(SQLiteDatabase db, Kontakt kontakt) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID,  kontakt.get_ID());
        values.put(KEY_FORNAVN,  kontakt.getFornavn());
        values.put(KEY_ETTERNAVN, kontakt.getEtternavn());
        values.put(KEY_TELEFON, kontakt.getTelefonNummer());
        db.insert(TABLE_KONTAKTER , null, values);


    }

}
