package com.example.vennapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.vennapp.database.models.Avtale;
import com.example.vennapp.database.models.Kontakt;
import com.example.vennapp.database.models.KontaktAvtale;

import java.util.ArrayList;
import java.util.List;

public class DBHandlerKontaktAvtale extends SQLiteOpenHelper {

    static String KEY_ID1 = "kontaktId";
    static String KEY_REF_ID1 = "_ID";
    static String KEY_REF_TABLE1 = "Kontakter";
    static String KEY_ID2 = "avtaleId";
    static String KEY_REF_ID2 = "_ID";
    static String KEY_REF_TABLE2 = "Avtaler";
    static String KEY_ID = "_ID";
    static String TABLE_KONTAKTAVTALE = "KontaktAvtale";
    static String KEY_FORNAVN = "Fornavn";
    static String KEY_ETTERNAVN = "Etternavn";
    static String KEY_TELEFON = "Telefon";

    static String TABLE_KONTAKTER = "Kontakter";

    static int DATABASE_VERSION = 3;static String DATABASE_NAME = "telefonkontakt";
    public DBHandlerKontaktAvtale(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String LAG_TABELLKONTAKT = "CREATE TABLE IF NOT EXISTS " + TABLE_KONTAKTER + "(" + KEY_ID + " INTEGER PRIMARY KEY ," + KEY_FORNAVN + " TEXT," + KEY_ETTERNAVN + " TEXT," + KEY_TELEFON + " TEXT" + ")";
        Log.d("SQL", LAG_TABELLKONTAKT);
        db.execSQL(LAG_TABELLKONTAKT);
        String LAG_TABELL ="CREATE TABLE IF NOT EXISTS " + TABLE_KONTAKTAVTALE + "("
                + KEY_ID1 + " INTEGER,"
                + KEY_ID2 + " INTEGER,"
                + "FOREIGN KEY("+ KEY_ID1 + ") REFERENCES " + KEY_REF_TABLE1 + "(" + KEY_REF_ID1 + "),"
                + "FOREIGN KEY("+ KEY_ID2 + ") REFERENCES " + KEY_REF_TABLE2 + "(" + KEY_REF_ID2 + "),"
                + "PRIMARY KEY(" + KEY_ID1 + "," + KEY_ID2 + "))";


        Log.d("SQL", LAG_TABELL);
        db.execSQL(LAG_TABELL);
    }
    public void fjernKontaktFraAvtale(SQLiteDatabase db, KontaktAvtale avtaleKontakt) {
        onCreate(db);//Unngå sjeldne bugs, at tabellen ikke finnes
        db.delete(TABLE_KONTAKTAVTALE , KEY_ID1 + " =? AND " + KEY_ID2 + "=?",
                new String[]{Long.toString(avtaleKontakt.getKontaktId()),Long.toString(avtaleKontakt.getAvtaleId()) });
    }
    public void fjernAlleKontaktFraAvtale(SQLiteDatabase db, Long avtaleId) {
        onCreate(db);//Unngå sjeldne bugs, at tabellen ikke finnes
        db.delete(TABLE_KONTAKTAVTALE , KEY_ID2 + " =?",
                new String[]{Long.toString(avtaleId) });
    }
    public void fjernAlleAvtalerFraKontakt(SQLiteDatabase db, Long kontaktId) {
        onCreate(db);//Unngå sjeldne bugs, at tabellen ikke finnes
        db.delete(TABLE_KONTAKTAVTALE , KEY_ID1 + " =?",
                new String[]{Long.toString(kontaktId) });
    }
    public List<Kontakt> finnAlleKontakterGittAvtale(SQLiteDatabase db, Long avtaleIdInn) {

        onCreate(db);//Unngå sjeldne bugs, at tabellen ikke finnes
        List<Kontakt> kontaktListe = new ArrayList<Kontakt>();
        String selectQuery = "SELECT t2._ID," + KEY_FORNAVN + ","+ KEY_ETTERNAVN + "," + KEY_TELEFON +" FROM " +  TABLE_KONTAKTAVTALE  + " t1 INNER JOIN " + KEY_REF_TABLE1 + " t2 ON t1." + KEY_ID1 + " =t2._ID WHERE t1." + KEY_ID2 + "= " + avtaleIdInn.toString();
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KONTAKTAVTALE );

    }

    public void leggTilKontaktTilAvtale(SQLiteDatabase db, KontaktAvtale avtaleKontakt) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID1, avtaleKontakt.getKontaktId());
        values.put(KEY_ID2, avtaleKontakt.getAvtaleId());
        db.insert(TABLE_KONTAKTAVTALE , null, values);
    }


}
